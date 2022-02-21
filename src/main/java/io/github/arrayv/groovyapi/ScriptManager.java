package io.github.arrayv.groovyapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        final List<Runnable> closers;

        private ScriptThread(File path, Script script) {
            super(script::run, "Script-" + script.getClass().getName());
            this.path = path;
            this.script = script;
            this.closers = new ArrayList<>();
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
    private Map<String, Script> installedScripts;

    public ScriptManager() {
        final CompilerConfiguration compilerConfig = new CompilerConfiguration();
        compilerConfig.addCompilationCustomizers(
            new ImportCustomizer()
                .addStarImports("io.github.arrayv.sortdata")
                .addStarImports("io.github.arrayv.groovyapi")
                .addStaticStars("io.github.arrayv.groovyapi.GroovyLocals")
        );
        compilerConfig.setScriptBaseClass("io.github.arrayv.groovyapi.ArrayVScript");
        compilerConfig.getClasspath().add(INSTALLED_SCRIPTS_ROOT.getPath());
        this.shell = new GroovyShell(compilerConfig);
        this.events = new EnumMap<>(ArrayVEventHandler.EventType.class);
        this.installedScripts = null;
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

    public ScriptThread runInThread(File path) throws IOException {
        Script script = shell.parse(path);
        ScriptThread thread = new ScriptThread(path, script);
        thread.start();
        return thread;
    }

    public Map<String, Script> loadInstalledScripts() throws IOException {
        if (installedScripts != null) {
            throw new IllegalStateException("Cannot load installed scripts more than once (i.e. you are not a privileged caller)");
        }
        if (!INSTALLED_SCRIPTS_ROOT.exists()) {
            INSTALLED_SCRIPTS_ROOT.mkdir();
            return installedScripts = Collections.emptyMap(); // There are definitely no scripts in a newly created directory
        }
        installedScripts = new HashMap<>();
        for (File subFile : INSTALLED_SCRIPTS_ROOT.listFiles()) {
            if (!subFile.isFile() || !subFile.getPath().endsWith(".groovy")) {
                continue;
            }
            Script script = loadScript(subFile);
            installedScripts.put(script.getClass().getName(), script);
        }
        runEventHandlers(ArrayVEventHandler.EventType.SCRIPTS_INSTALLED);
        return installedScripts;
    }

    public Map<String, Script> getInstalledScripts() {
        if (installedScripts == null) {
            throw new Error("Cannot return installed scripts before they're loaded");
        }
        return installedScripts;
    }
}
