package io.github.arrayv.groovyapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

public final class ScriptManager {
    public final class ScriptThread extends Thread {
        private final File path;
        private final Script script;
        final Set<Runnable> closers;

        private ScriptThread(File path, Script script) {
            super(script::run, "Script-" + script.getClass().getName());
            this.path = path;
            this.script = script;
            this.closers = Collections.newSetFromMap(new IdentityHashMap<>());
        }

        public File getPath() {
            return path;
        }

        public Script getScript() {
            return script;
        }

        @Override
        public void run() {
            super.run();
            for (Runnable closer : closers) {
                closer.run();
            }
        }
    }

    private static final File INSTALLED_SCRIPTS_ROOT = new File("scripts");

    private final GroovyShell shell;
    private final Map<ArrayVEventHandler.EventType, Set<ArrayVEventHandler>> events;
    private Map<String, Script> defaultScripts;

    public ScriptManager() {
        final CompilerConfiguration compilerConfig = new CompilerConfiguration();
        compilerConfig.addCompilationCustomizers(
            new ImportCustomizer()
                .addStarImports("io.github.arrayv.sortdata")
                .addStarImports("io.github.arrayv.groovyapi")
                .addStaticStars("io.github.arrayv.groovyapi.GroovyLocals")
                .addImports("io.github.arrayv.groovyapi.ArrayVEventHandler.EventType")
        );
        compilerConfig.setScriptBaseClass("io.github.arrayv.groovyapi.ArrayVScript");
        compilerConfig.getClasspath().add(INSTALLED_SCRIPTS_ROOT.getPath());
        this.shell = new GroovyShell(compilerConfig);
        this.events = new EnumMap<>(ArrayVEventHandler.EventType.class);
        this.defaultScripts = null;
    }

    public GroovyShell getGroovyShell() {
        return shell;
    }

    private Set<ArrayVEventHandler> getEventHandlers0(ArrayVEventHandler.EventType eventType) {
        return events.computeIfAbsent(eventType, k -> new HashSet<>());
    }

    public Set<ArrayVEventHandler> getEventHandlers(ArrayVEventHandler.EventType eventType) {
        return Collections.unmodifiableSet(getEventHandlers0(eventType));
    }

    public void runEventHandlers(ArrayVEventHandler.EventType eventType) {
        RuntimeException e = null;
        for (ArrayVEventHandler handler : getEventHandlers0(eventType)) {
            try {
                handler.handle();
            } catch (Exception e1) {
                if (e == null) {
                    e = new RuntimeException(e1);
                } else {
                    e.addSuppressed(e1);
                }
            }
        }
        if (e != null) {
            throw e;
        }
    }

    public void registerEventHandlers(ArrayVEventHandler... handlers) {
        for (ArrayVEventHandler handler : handlers) {
            getEventHandlers0(handler.getEventType()).add(handler);
        }
    }

    public void unregisterEventHandlers(ArrayVEventHandler... handlers) {
        for (ArrayVEventHandler handler : handlers) {
            getEventHandlers0(handler.getEventType()).remove(handler);
        }
    }

    public Script loadScript(File path) throws IOException {
        Script script = shell.parse(path);
        script.run();
        return script;
    }

    public Script loadScript(URL url) throws IOException {
        Script script;
        try {
            script = shell.parse(url.toURI());
        } catch (URISyntaxException e) {
            throw new Error(e);
        }
        script.run();
        return script;
    }

    public ScriptThread runInThread(File path) throws IOException {
        Script script = shell.parse(path);
        ScriptThread thread = new ScriptThread(path, script);
        thread.start();
        return thread;
    }

    public Map<String, Script> loadDefaultScripts() throws IOException {
        if (defaultScripts != null) {
            throw new IllegalStateException("Cannot load default scripts more than once (i.e. you should not be calling this method)");
        }
        defaultScripts = new HashMap<>();
        loadBuiltinScripts();
        loadInstalledScripts();
        return defaultScripts;
    }

    private void loadInstalledScripts() throws IOException {
        if (!INSTALLED_SCRIPTS_ROOT.exists()) {
            INSTALLED_SCRIPTS_ROOT.mkdir();
            return;
        }
        for (File subFile : INSTALLED_SCRIPTS_ROOT.listFiles()) {
            if (!subFile.isFile() || !subFile.getPath().endsWith(".groovy")) {
                continue;
            }
            Script script = loadScript(subFile);
            defaultScripts.put(script.getClass().getName(), script);
        }
    }

    public Map<String, Script> getDefaultScripts() {
        if (defaultScripts == null) {
            throw new Error("Cannot return default scripts before they're loaded");
        }
        return defaultScripts;
    }

    private void loadBuiltinScripts() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        for (String scriptPath : findBuiltinScripts(classLoader)) {
            URL scriptUrl = classLoader.getResource(scriptPath);
            Script script = loadScript(scriptUrl);
            defaultScripts.put(script.getClass().getName(), script);
        }
    }

    // Modified from https://github.com/apache/groovy/blob/master/src/main/java/org/codehaus/groovy/control/SourceExtensionHandler.java
    private static Set<String> findBuiltinScripts(ClassLoader loader) throws IOException {
        Set<String> scripts = new LinkedHashSet<String>();
        Enumeration<URL> globalServices = loader.getResources("META-INF/arrayv/io.github.arrayv.groovyapi.BuiltinScripts");
        if (!globalServices.hasMoreElements()) {
            globalServices = loader.getResources("META-INF/arrayv/io.github.arrayv.groovyapi.BuiltinScripts");
        }
        while (globalServices.hasMoreElements()) {
            URL service = globalServices.nextElement();
            try (BufferedReader svcIn = new BufferedReader(new InputStreamReader(service.openStream()))) {
                String scriptBasePath = svcIn.readLine();
                while (scriptBasePath != null) {
                    scriptBasePath = scriptBasePath.trim();
                    if (!scriptBasePath.isEmpty() && !scriptBasePath.startsWith("#")) {
                        scripts.add(scriptBasePath);
                    }
                    scriptBasePath = svcIn.readLine();
                }
            }
        }
        return scripts;
    }
}
