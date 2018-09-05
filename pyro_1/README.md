# Welcome to Pyro4 implementation for algorithm services!

## First of all, build the docker image

`docker build -t pyro .`

## Now with the image ready, lift up the Pyro Name Server (it runs once and always)

`docker-compose up`

## Finally, enter the docker compose and execute the current executor script

`docker exec -it pyro bash`
`python3 request_forecast.py`


#### What happens in the background explained:
1. Lift up a name server, which is where Pyro manages events and Daemons
2. Register in the name server ServiceCreator class, which runs always in the background
3. Use ServiceCreator to register ServiceProvider classes (one for each model)
4. Request the ServiceProvider to execute with all the parameters and get the results and error metrics
5. ServiceProvider unregisters by himself
