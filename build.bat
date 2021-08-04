mkdir dist

call mvn install
@echo on

copy target\arrayv-4.21.6.jar dist\arrayv-latest.jar
