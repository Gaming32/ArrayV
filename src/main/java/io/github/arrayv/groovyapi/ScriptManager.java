package io.github.arrayv.groovyapi;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.GroovyShell;

public final class ScriptManager {
    private final GroovyShell shell;

    public ScriptManager() {
        final CompilerConfiguration compilerConfig = new CompilerConfiguration();
        compilerConfig.addCompilationCustomizers(
            new ImportCustomizer()
                .addStarImports("io.github.arrayv.groovyapi")
                .addStaticStars("io.github.arrayv.groovyapi.GroovyLocals")
        );
        this.shell = new GroovyShell(compilerConfig);
    }

    public GroovyShell getShell() {
        return shell;
    }
}
