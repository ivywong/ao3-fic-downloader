#!/bin/bash

javac -cp .;./lib/jars/* Downloader.java && (
    java -cp .;./lib/jars/* Downloader
) || (
    javac -cp .:./lib/jars/* Downloader.java
    java -cp .:./lib/jars/* Downloader
)

