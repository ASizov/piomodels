#!/bin/bash
ENGINE=${PWD##*/}
# Install virtual env

[ -d /env/$ENGINE ] || virtualenv /env/$ENGINE

source /env/$ENGINE/bin/activate
cat environment.txt | xargs pip install

