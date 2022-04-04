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

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;

/**
 * This class is used to load and run Groovy scripts.
 * All ArrayV Groovy scripts are executed from this class.
 */
public final class ScriptManager {
    /**
     * A {@link Thread} subclass that manages the execution of a Groovy script.
     * A common use for this class is using it to get the current thread's
     * {@link Script} or the path to the script file.
     * @see Thread
     */
    public static final class ScriptThread extends Thread {
        private final File path;
        private final Script script;
        final Set<Runnable> closers;

        private ScriptThread(File path, Script script) {
            super(script::run, "Script-" + script.getClass().getName());
            this.path = path;
            this.script = script;
            this.closers = Collections.newSetFromMap(new IdentityHashMap<>());
        }

        static void runClosers(Set<Runnable> closers) {
            RuntimeException ex = null;
            for (Runnable closer : closers) {
                try {
                    closer.run();
                } catch (RuntimeException e) {
                    if (ex == null) {
                        ex = e;
                    } else {
                        ex.addSuppressed(e);
                    }
                }
            }
            if (ex != null) {
                throw ex;
            }
        }

        /**
         * The path to the .groovy script file running in this thread
         * @return The path to the script
         */
        public File getPath() {
            return path;
        }

        /**
         * The {@link Script} running in this thread
         * @return The {@link Script} running in this thread
         * @see Script
         */
        public Script getScript() {
            return script;
        }

        /**
         * Run the thread. This is an extension of {@link Thread#run()} that
         * runs closers at the end. See {@link GroovyLocals#registerCloser}
         * for a description on what closers are.
         */
        @Override
        public void run() {
            super.run();
            runClosers(closers);
        }
    }

    private static final File INSTALLED_SCRIPTS_ROOT = new File("scripts");

    private final GroovyShell shell;
    private final Map<ArrayVEventHandler.EventType, Set<ArrayVEventHandler>> events;
    private Map<String, Script> defaultScripts;

    /**
     * Construct a ScriptManager instance. This may only be called from {@link ArrayVisualizer}.
     * If you wish to get a reference to the ArrayV ScriptManager, use
     * {@code ArrayVisualizer.getInstance().getScriptManager()}.
     * @throws IllegalStateException When you call this constructor.
     */
    public ScriptManager() throws IllegalStateException {
        // Index 0 is Thread, and index 1 is ScriptManager
        if (!Thread.currentThread().getStackTrace()[2].getClassName().equals(ArrayVisualizer.class.getName())) {
            throw new IllegalStateException("Only ArrayVisualizer can create a ScriptManager instance. "
                + "As such, there can only one ScriptManager instance. You can get obtain it with "
                + "ArrayVisualizer.getInstance().getScriptManager()");
        }
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

    /**
     * The {@link GroovyShell} associated with the script manager
     * @return The GroovyShell associated with the script manager
     * @see GroovyShell
     */
    public GroovyShell getGroovyShell() {
        return shell;
    }

    private Set<ArrayVEventHandler> getEventHandlers0(ArrayVEventHandler.EventType eventType) {
        return events.computeIfAbsent(eventType, k -> new HashSet<>());
    }

    /**
     * Get the event handlers for the specified event type
     * @param eventType The type of event to obtain handlers for
     * @return The event handlers
     */
    public Set<ArrayVEventHandler> getEventHandlers(ArrayVEventHandler.EventType eventType) {
        return Collections.unmodifiableSet(getEventHandlers0(eventType));
    }

    /**
     * Run the event handlers for the specified event type
     * @param eventType The type of event to handle
     */
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

    /**
     * Register event handlers. {@link ArrayVEventHandler#register} is preferred
     * over this unless you're registering multiple handlers at once.
     * @param handlers The event handlers to register
     */
    public void registerEventHandlers(ArrayVEventHandler... handlers) {
        for (ArrayVEventHandler handler : handlers) {
            getEventHandlers0(handler.getEventType()).add(handler);
        }
    }

    /**
     * Unegister event handlers. {@link ArrayVEventHandler#unregister} is preferred
     * over this unless you're unregistering multiple handlers at once.
     * @param handlers The event handlers to unregister
     */
    public void unregisterEventHandlers(ArrayVEventHandler... handlers) {
        for (ArrayVEventHandler handler : handlers) {
            getEventHandlers0(handler.getEventType()).remove(handler);
        }
    }

    private Script handleCompilationFailure(CompilationFailedException e) {
        if (e instanceof MultipleCompilationErrorsException) {
            JErrorPane.invokeMonospaceErrorMessage(e.getMessage(), "Run Script");
        } else {
            JErrorPane.invokeErrorMessage(e, "Run Script");
        }
        throw e;
    }

    /**
     * Compile and run the script at the specified path
     * @param path The path of the script
     * @return The loaded {@link Script} object
     */
    public Script loadScript(File path) throws IOException {
        Script script;
        try {
            script = shell.parse(path);
        } catch (CompilationFailedException e) {
            return handleCompilationFailure(e);
        }
        script.run();
        return script;
    }

    /**
     * Compile and run the script at the specified URL
     * @param url The URL of the script
     * @return The loaded {@link Script} object
     */
    public Script loadScript(URL url) throws IOException {
        Script script;
        try {
            script = shell.parse(url.toURI());
        } catch (URISyntaxException e) {
            throw new Error(e);
        } catch (CompilationFailedException e) {
            return handleCompilationFailure(e);
        }
        script.run();
        return script;
    }

    /**
     * Compile and run the specified script in a new thread
     * @param path The path of the script
     * @return The {@link ScriptThread} the script was run in
     * @see ScriptThread
     */
    public ScriptThread runInThread(File path) throws IOException {
        Script script;
        try {
            script = shell.parse(path);
        } catch (CompilationFailedException e) {
            handleCompilationFailure(e);
            return null; // UNREACHABLE
        }
        ScriptThread thread = new ScriptThread(path, script);
        thread.start();
        return thread;
    }

    /**
     * <p>Load the default scripts. These are the union of the scripts embedded
     * in the ArrayV JAR and the scripts in the {@code scripts} directory.</p>
     *
     * <p>This method may only be called once, from {@link ArrayVisualizer}</p>
     *
     * @throws IllegalStateException When you call this method.
     */
    public Map<String, Script> loadDefaultScripts() throws IOException, IllegalStateException {
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

    /**
     * The default scripts. See {@link #loadDefaultScripts()} for a description
     * of what default scripts are
     * @return The default scripts
     * @see #loadDefaultScripts()
     */
    public Map<String, Script> getDefaultScripts() {
        if (defaultScripts == null) {
            throw new Error("Cannot return default scripts before they're loaded");
        }
        return Collections.unmodifiableMap(defaultScripts);
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
