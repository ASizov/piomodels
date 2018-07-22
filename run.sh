#!/usr/bin/env bash
docker run -v $PWD/data:/data -v $PWD/env:/env -v $PWD/engines:/engines -it -p 9000:9000 -p 8000:8000 -p 7070:7070 pio-0.12.1 /engines/initialize.sh
