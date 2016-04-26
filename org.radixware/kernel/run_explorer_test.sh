#!/bin/bash

run_testcases=0
run_explorer_tests=0
use_debug=0
instance_id=
usage="Usage: $0 [-t|--testCases] [-e|--explorerTests] -i|--instance <instance_id>"


if [ -z "$*" ] ; then echo $usage ; exit 1; fi

PARSED_ARGS=`getopt -o tedi: --longoptions testCases,explorerTests,debug,instance: -- "$@"`

echo "Parsed args: $PARSED_ARGS"

if [ $? != 0 ] ; then echo 'Error while parsing arguments' ; exit 1 ; fi

eval set -- "$PARSED_ARGS"

while true ; do
    case "$1" in
        -t|--testCases) echo 'Will run testcases' ; run_testcases=1 ; shift ;;
        -e|--explorerTests) echo 'Will run explorer tests' ; run_explorer_tests=1 ; shift ;;
        -d|--debug) echo 'Will run with debug agent' ; use_debug=1 ; shift ;;
        -i|--instance) echo "Will use instance #$2" ; instance_id=$2 ;  shift 2 ;;
        --) shift ; break ;;
        *) echo "errornous parameter: $1" ; echo $usage;  exit 1 ;;
    esac
done

if [ -z $instance_id ] ; then echo 'No instance id given' ; echo $usage ; exit 1 ; fi

ulimit -c unlimited

#for arg in "$@"
#do
#    case $arg in
#        -testCases)     
#                        run_testcases=1
#                        run_all=0;;
#        -explorerTests) 
#                        run_explorer_tests=1
#                        run_all=0;;
#        *)              
#                        echo "Unknown argument: $arg\nUsage: $0 [-testCases] [-explorerTests]"
#                        exit 0;;
#                        
#    esac
#done

#echo "run_all: $run_all"
#echo "run_testcases: $run_testcases"
#echo "run_explorer_test: $run_explorer_tests"

#if [ $run_all -eq 1 ]; then
#    echo 'No parameters specified, running both tescases and explorer tree traversing'
#    run_testcases=1
#    run_explorer_tests=1
#fi

if [ -z "$CONFIG_PATH" ]; then
    CONFIG_PATH=$HOME/.radixware.org/explorer
fi 

if [ -z "$WORK_DIR" ]; then
    #consider that current directory is (...)/trunk/org.radixware/kernel
    WORK_DIR=../..
fi 

function kill_server {
    #echo 'killing background jobs' 
    #jobs
    #bg_pids=$(jobs -p)
    #echo "bg_pids:"
    #echo "$bg_pids"
    #pids=( $bg_pids )
    #echo ${pids[0]}
    #echo ${pids[1]}
    #echo "current_proc_pid: $$"
    #if [ ! -z "$bg_pids" ]; then
    #   kill $bg_pids
    #    sleep 3
    #fi 
    jobs
    kill %1
    sleep 5
}

#trap to kill server in case of unexpected process termination
trap 'echo caught_signal; kill_server; exit' SIGHUP SIGINT SIGTERM

CP=$WORK_DIR/org.radixware/kernel/starter/bin/dist/starter.jar
SERVER_JAR=$WORK_DIR/org.radixware/kernel/server/lib/ojdbc6.jar
MAIN_CLASS=org.radixware.kernel.starter.Starter

TOP_LAYER_URI=org.radixware

if [ $use_debug -eq 1 ];
then
    DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=localhost:8000,server=y,suspend=y"
fi

SERVER_JVM_OPTIONS="-server -Xmx768m -XX:MaxPermSize=256m $DEBUG_OPTS"
SERVER_OPTIONS="-dbUrl jdbc:oracle:thin:@vdev3:1521/x -user RADIX -pwd RADIX -dbSchema RADIX -instance $instance_id -autostart -switchGuiOff -development -kspwd keystore"
java $SERVER_JVM_OPTIONS  -classpath $CP:$SERVER_JAR $MAIN_CLASS -workDir=$WORK_DIR -topLayerUri=$TOP_LAYER_URI org.radixware.kernel.server.Server $SERVER_OPTIONS &

echo `date`

sleep 60 #wait for server to start

echo `date`

#SERVER_PROCESS=`ps -aef | grep org.radixware.kernel.server.Server | grep -v grep | awk '{print $2}'`

#test ads


if [ -z $HUDSON_AAS_ADDRESS ]; then
    HUDSON_AAS_ADDRESS="localhost:20469"
fi

if [ $run_testcases -eq 1 ];
then 

    echo 'Running testcases'

    TEST_RUNNER_OPTIONS="-aasAddress $HUDSON_AAS_ADDRESS -resultFileName $CONFIG_PATH/adsTestingResult.xml -testCasePid 841 -userName Hudson"

    java -classpath $CP $MAIN_CLASS -workDir=$WORK_DIR -topLayerUri=$TOP_LAYER_URI org.radixware.kernel.server.test.IsTestsRemoteRunner $TEST_RUNNER_OPTIONS 

    tc_result=$?
    echo "tc_result: $tc_result"

    if [ $tc_result -ne 0 ]; then
        echo 'Testcases execution failed'
        kill_server
	exit $tc_result
    fi
    echo 'Testcases execution finished'

fi

if [ $run_explorer_tests -eq 1 ];
then
#open all explorer tree

    echo 'Running explorer tree traversing'

    EXPLORER_ROOT=parROGSM5O5ZZC4FMUSVYLVBTE5TI #RADIX


    if [ -z "$TEST_OPTIONS_FILE" ]; then
        TEST_OPTIONS_FILE=./tester.ini
    fi 

    if [ -z "$EXPLORER_TEST_RESULT_FILE" ]; then
        EXPLORER_TEST_RESULT_FILE=explorerTesting.xml
    fi 

    EXPLORER_OPTIONS="-traceProfile Debug -connection Radix-Hudson -pwd Hudson -configPath $CONFIG_PATH -root $EXPLORER_ROOT -autoTest $TEST_OPTIONS_FILE -junitReport $EXPLORER_TEST_RESULT_FILE"

    java -classpath $CP $MAIN_CLASS -workDir=$WORK_DIR -topLayerUri=$TOP_LAYER_URI org.radixware.kernel.explorer.Explorer $EXPLORER_OPTIONS

    echo 'Explorer tree traversing finished'

fi

kill_server
