if [ "$1" != "" ]
then
    java -Dsun.java2d.d3d=false -cp bin:lib/classgraph-4.8.47.jar main.ArrayVisualizer "$1"
else
    java -Dsun.java2d.d3d=false -cp bin:lib/classgraph-4.8.47.jar main.ArrayVisualizer
fi
