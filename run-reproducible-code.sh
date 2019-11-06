#!/bin/sh -eu

if [ $# != 3 ]
then
  echo "Usage: $0 <JDK 11 javac> <JRE 11 java> <JRE 13 java>"

  exit 0
fi

JAVAC_11="$1"
JAVA_11="$2"
JAVA_13="$3"

echo "Preparing the reproducible environment."
rm -f -- test/*.class *.jar
${JAVAC_11} test/JavacFileManagerClosedArchiveContainers.java
jar cf reproducible-code.jar test

printf "Expected behavior:"
${JAVA_11} -cp reproducible-code.jar test.JavacFileManagerClosedArchiveContainers

printf "Actual behavior:"
${JAVA_13} -cp reproducible-code.jar test.JavacFileManagerClosedArchiveContainers
