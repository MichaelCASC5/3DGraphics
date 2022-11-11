@echo off
cls
cd src
if exist *.class del *.class
javac Driver.java
java Driver.java
del *.class