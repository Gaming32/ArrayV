@echo off
call mvnw dependency:build-classpath -Dmdep.outputFile=target\classpath -Dmdep.regenerateFile=true -q
<NUL set /p=-cp target\classes;> target\cmdargs
type target\classpath >> target\cmdargs
title ArrayV
java @target\cmdargs io.github.arrayv.main.ArrayVisualizer %1
