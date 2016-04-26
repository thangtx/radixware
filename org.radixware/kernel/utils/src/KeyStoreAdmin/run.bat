setlocal
set RDX_KERNEL_PATH=..\..\..
set LIB_PATH=..\..\lib
set cp=
set cp=%cp%;dist\keyStoreAdmin.jar
set cp=%cp%;%LIB_PATH%\swing-worker-1.1.jar
set cp=%cp%;%LIB_PATH%\appframework-1.0.3.jar
set cp=%cp%;%RDX_KERNEL_PATH%\common\bin\general.jar
set cp=%cp%;%RDX_KERNEL_PATH%\common\lib\bcprov-jdk16-144.jar

start java -classpath "%cp%" org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdmin