#!/bin/sh
java -Xms64m -Xmx4096m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=~/java_talent-aio-im-client_pid.hprof -jar talent-aio-im-client.jar