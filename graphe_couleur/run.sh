#!/usr/bin/env bash
export ARGS=`echo "$@"`
mvn package
clear
java -cp ./target/graphe*.jar launcher.App "$ARGS"
