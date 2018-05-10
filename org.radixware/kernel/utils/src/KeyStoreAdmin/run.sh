#!/bin/sh

CLASS="org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdmin"
RDX_KERNEL_PATH="../../.."
LIB_PATH="../../lib"
CLASSPATH="dist/keyStoreAdmin.jar"
CLASSPATH=$CLASSPATH:$LIB_PATH"/swing-worker-1.1.jar"
CLASSPATH=$CLASSPATH:$LIB_PATH"/appframework-1.0.3.jar"
CLASSPATH=$CLASSPATH:$RDX_KERNEL_PATH"/common/bin/general.jar"
CLASSPATH=$CLASSPATH:$RDX_KERNEL_PATH"/common/lib/bcprov.jar"
CLASSPATH=$CLASSPATH:$RDX_KERNEL_PATH"/common/lib/bcpkix.jar"
CLASSPATH=$CLASSPATH:$RDX_KERNEL_PATH"/common/lib/commons-lang-2.6.jar"

java -classpath $CLASSPATH $CLASS
