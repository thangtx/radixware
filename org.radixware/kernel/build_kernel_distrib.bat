set ANT_OPTS = -Xmx1024m -XX:MaxPermSize=512m
time /t > build.log
call ant clean distributive -Dnetbeans.installation.dir=%NETBEANS_PROPS%
time /t >> build.log
call ant clean-devtools distributive-devtools -Dnetbeans.installation.dir=%NETBEANS_PROPS% >> build.log
time /t >> build.log
