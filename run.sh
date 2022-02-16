if [ "$1" != "" ]
then
    java -jar target/ArrayV-5.0-SNAPSHOT.jar "$1"
else
    java -jar target/ArrayV-5.0-SNAPSHOT.jar
fi
