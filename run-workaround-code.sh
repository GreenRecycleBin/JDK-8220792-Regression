#!/bin/sh -eu

if [ $# != 3 ]
then
  echo "Usage: $0 <JDK 11 javac> <JRE 11 java> <JRE 13 java>"

  exit 0
fi

JAVAC_11="$1"
JAVA_11="$2"
JAVA_13="$3"

echo "Preparing the workaround environment."
rm -f test/*.class
${JAVAC_11} test/JavacFileManagerClosedArchiveContainersWorkaround.java
jar cf workaround-code.jar test

printf "Expected behavior:"
${JAVA_11} -cp workaround-code.jar test.JavacFileManagerClosedArchiveContainersWorkaround

printf "Actual behavior:"
${JAVA_13} -cp workaround-code.jar test.JavacFileManagerClosedArchiveContainersWorkaround
