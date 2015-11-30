#!/bin/bash

mvn package
java -cp /opt/zookeeper/zookeeper-3.4.6.jar:/opt/zookeeper/lib/slf4j-log4j12-1.6.1.jar:/opt/zookeeper/lib/slf4j-api-1.6.1.jar:/opt/zookeeper/lib/log4j-1.2.16.jar:target/pizzaria-1.0-SNAPSHOT.jar br.edu.ufabc.pizzaria.App

