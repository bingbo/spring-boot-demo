#!/usr/bin/env bash

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home
export CLASSPATH=$JAVA_HOME/lib:$JAVA_HOME/jre/lib
export PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH:$HOME/bin

cd `dirname $0`
bin_dir=`pwd`
echo $bin_dir
cd ..
app_dir=`pwd`
echo $app_dir
conf_dir=$app_dir/conf
echo $conf_dir
lib_dir=$app_dir/lib
echo $lib_dir
lib_jars=`ls $lib_dir|grep .jar|awk '{print "'$lib_dir'/"$0}'|tr "\n" ":"`
echo $lib_jars
jmx_port=8900

java_jmx_opts=" -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$jmx_port -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
java_jvm_opts=" -server -Xms1g -Xmx1g -Xmn256m -XX:PermSize=256m -XX:MaxPermSize=256m -Xss256K -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=60 "
java_gc_opts=" -verbose:gc -Xloggc:./gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps "
java_classpath_opts=" -classpath $conf_dir:$lib_jars"


#java $java_jmx_opts $java_jvm_opts $java_gc_opts $java_classpath_opts -jar $lib_dir/spring-boot-demo-1.0-SNAPSHOT.jar
$JAVA_HOME/bin/java $java_jmx_opts $java_jvm_opts $java_gc_opts $java_classpath_opts com.ibingbo.boot.Application
