#!/bin/sh

# Compile
javac org/seyster/seystertron/*.java

# Create a handy jar file
jar -cf seystertron.jar org/seyster/seystertron/*.class
