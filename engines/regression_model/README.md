# Poisson Regression Engine


```
cd /engines/regression_model
```
## Initialize

We have to create the environment used for running import, build, train, deploy.
```
./initialize.sh
```

## Run Commands
This script will run all steps and end with a model that is deployed and responds to queries.
```
./poisson_regression.sh
```

### Get predictions
To test the engine run this command
```
curl -s -H "Content-Type: application/json" -d '{"fecha":"2018-06-06"}' http://localhost:8000/queries.json
```

The result returned should be 
```
{"label":448360.91039487766}
```