#!/bin/bash

current_dir=$(pwd)
script_dir=$(dirname "$0")
docker_compose_file="$current_dir/$script_dir/kube.yaml"

minikube kubectl -- delete -f $docker_compose_file