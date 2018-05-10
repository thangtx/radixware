set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_71
set ANT_HOME=C:\Program Files\apache-ant-1.9.6
set ANT_BIN=%ANT_HOME%\bin
set NETBEANS_PROPS=%APPDATA%\NetBeans\8.0.2\build.properties
set RADIX_HOME=..\..\..\
call "%ANT_BIN%"\ant -f %RADIX_HOME%\org.radixware\kernel\build.xml actualize_check-3party-licenses -Duser.properties.file=%NETBEANS_PROPS%