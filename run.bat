@echo off
PATH %PATH%;%JAVA_HOME%\bin\
for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -fullversion 2^>^&1') do set "jver=%%j.%%k"
if %jver%==1.8 (
    java -Dsun.java2d.d3d=false -cp bin;lib/classgraph-4.8.47.jar main.ArrayVisualizer %1
) else (
    java --add-opens=java.desktop/com.sun.media.sound=ALL-UNNAMED -Dsun.java2d.d3d=false -cp bin;lib/classgraph-4.8.47.jar main.ArrayVisualizer %1
)
