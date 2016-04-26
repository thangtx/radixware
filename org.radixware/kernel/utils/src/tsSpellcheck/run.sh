#!/bin/sh

CLASS="org.radixware.utils.tsspellcheck.Spellchecker"
CLASSPATH="dist/tsSpellcheck.jar"
CLASSPATH=$CLASSPATH:"dist/lib/general.jar"
CLASSPATH=$CLASSPATH:"dist/lib/qtjambi.jar"
CLASSPATH=$CLASSPATH:"dist/lib/starter.jar"
CLASSPATH=$CLASSPATH:"dist/lib/utils.jar"

java -classpath $CLASSPATH $CLASS