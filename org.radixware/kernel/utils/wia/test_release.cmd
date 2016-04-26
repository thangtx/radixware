@set JAR_FILE=.\bin\java\java-wia.jar
@set JAVA_CLASS=org.radixware.kernel.utils.wia.TestWiaScanner
@set NATIVE_LIB_LOCATION_PROP_NAME=org.radixware.kernel.utils.wia.native-library-path
@set NATIVE_LIB_LOCATION=C:\wia\bin\X64\release\jwia.dll
java -classpath %JAR_FILE% -D%NATIVE_LIB_LOCATION_PROP_NAME%=%NATIVE_LIB_LOCATION% %JAVA_CLASS%