#!/usr/bin/env bash
export ARGS=`echo "$@"`
mvn package
clear
java -cp ./target/g*.jar launcher.App "$ARGS"
