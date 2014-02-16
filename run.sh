#!/bin/bash

echo "Compiling..."
javac -cp .:./lib/jars/* Downloader.java
echo "Compiled."
echo "Running..."
java -cp .:./lib/jars/* Downloader
