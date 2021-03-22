#!/bin/bash

current_dir=$(pwd)
script_dir=$(dirname "$0")
docker_compose_file="$current_dir/$script_dir/docker-compose.yml"

docker-compose  -f $docker_compose_file down