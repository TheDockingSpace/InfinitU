#!/bin/bash
# Calls zkCli.sh inside a container linked to the first instance started by docker-compose up

docker run -it --rm --net infinitu_default --link infinitu_zoo1_1:zookeeper zookeeper zkCli.sh -server zookeeper

