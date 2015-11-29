#!/bin/bash

mvn package
java -cp /opt/zookeeper/zookeeper-3.4.6.jar:target/pizzaria-1.0-SNAPSHOT.jar br.edu.ufabc.pizzaria.App
