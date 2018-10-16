#!/bin/bash
#
# Environment Variableœs
#
#   JAVA_OPTS     jvm run options
#
#	CLASSPATH	  jvm runtime library
#
#	CLASS	  	  Application entry
#
# resolve links - $0 may be a softlink

if [ -e nohup.out ]; then
	echo "backup file nohup.out"
	mv nohup.out nohup.out.`date +%Y-%m-%d_%H-%M-%S`
fi

PROJECT_HOME=`pwd`

CLASSPATH=$PROJECT_HOME/classes
CLASSPATH=${CLASSPATH}:$PROJECT_HOME/lib
#CLASSPATH=${CLASSPATH}:$JAVA_HOME/lib/tools.jar

# add libs to CLASSPATH
for f in $PROJECT_HOME/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done

# figure out which class to run
CLASS='com.cellsgame.NetTest'

# Mem
JAVA_OPTS="-server -Djava.net.preferIPv4Stack=true -Xms128m -Xmx128m -XX:AlwaysPreTouch"
# Performance
JAVA_OPTS=$JAVA_OPTS" -XX:+UseFastAccessorMethods -XX:+DisableExplicitGC "
# GC (ParNew and CMS)
# CMSInitiatingOccupancyFraction = ((mx - mn)-(mn - mn / (SurvivorRatio + 2))) / (mx - mn) * 100
# if CMSInitiatingOccupancyFraction less than 70, please adjust parameter (-Xmn and SurvivorRatior)
JAVA_OPTS=$JAVA_OPTS" -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=80"
# GC LOG
JAVA_OPTS=$JAVA_OPTS" -Xnoclassgc -verbose:gc -Xloggc:logs/gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCApplicationStoppedTime"
# Heap Dump (Memory Analyzer)
# jmap -dump:format=b,file=$PROJECT_HOME/logs/heamdump.out
JAVA_OPTS=$JAVA_OPTS" -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$PROJECT_HOME/logs/dump.hprof"
# DEV ENV
JAVA_OPTS=$JAVA_OPTS" -XX:+PrintHeapAtGC"
# JIT (JITWatch or Jarscan - MaxInlineSize - FreqInlineSize)
JAVA_OPTS=$JAVA_OPTS" -XX:+UnlockDiagnosticVMOptions -XX:+LogCompilation -XX:+PrintCompilation"
# Netty [allocator type, use direct buffer, try unsafe]
# io.netty.allocator.type can be pooled or unpooled, it means byte buffer is pooled or not.
# if tryUnsafe is false or noPreferDirect is true, the buffer for write and read will use heap buffer.
# JAVA_OPTS=$JAVA_OPTS" -Dio.netty.allocator.type=pooled -Dio.netty.noPreferDirect=false -Dio.netty.tryUnsafe=true -Dio.netty.leakDetectionLevel=simple"

# log config
if [ "$LOG_DIR" = "" ]; then
  LOG_DIR="$PROJECT_HOME/logs"
fi
if [ "$LOG_FILE" = "" ]; then
  LOG_FILE='run.log'
fi

LOG_OPTS="-Dlog.dir=$LOG_DIR -Dlog.file=$LOG_FILE"

echo $JAVA_OPTS
echo $CLASSPATH

num=0
while [ $num -lt 1 ]
do
if [ -f "error" ];then
num=1
else
java $JAVA_OPTS $LOG_OPTS  -cp "$CLASSPATH" $CLASS "$@"
#nohup "$JAVA" $JAVA_OPTS $LOG_OPTS  -cp "$CLASSPATH" $CLASS &
num=`expr $num + 1`
sleep 10
fi
done
