# pio-algorithms-orax

Docker based on https://github.com/steveny2k/docker-predictionio/tree/pio-0.12.0

## Descriptions

- pio-container: has files for building an image with all necessary linux libraries
- env: will be attached as a volume and have  all python environments
- data: will be attached as a volume and have data used to load into Apps
- engines: will be attached as a volume and have all engines. Engines that use a python environment should write it to /env and sample data should be placed in /data and not pushed to this repository


## Getting Started

### Build Image

```
cd pio-container
./build-image.sh
```

You should have an image named pio-0.12.1 now. You can verify with:

```
docker images
```

### Run Container

From the main directory run:

```
./run.sh
```

This will start the container and pio. There should be a message confirming that: Your system is all ready to go


## Engines

