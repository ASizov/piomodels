#!/usr/bin/env bash
######################
## Start virtualenv ##
######################
ENGINE=${PWD##*/}
source /env/$ENGINE/bin/activate

######################
#### Create new app ##
######################
KEY='LYH_SlbnDpJIfHLiPYPx-sZaC4swYn0hmNEI7f5bgznabP_SoNcbshzDMZTg9tFY'
pio app new Barcel_App --access-key=$KEY

######################
##### Import data ####
######################
python import_data.py --access-key $KEY --file sample_data_barcel.csv --group-by fecha
# Expected result:
# Importing data...
# 56 events are imported.

######################
#### Build engine ####
######################
pio build --verbose
# [INFO] [Engine$] Build finished successfully.
# [INFO] [Pio$] Your engine is ready for training.

######################
#### Run training ####
######################
pio train
# Root Mean Squared Error (RMSE) on test data = 90542.8
# Mean Absolute Error (MAE) on test data = 67778.7
# [INFO] [CoreWorkflow$] Training completed successfully.

######################
### Deploy engine ####
######################
pio deploy &
# [INFO] [MasterActor] Engine is deployed and running. Engine API is live at http://0.0.0.0:8000.

######################
### Make prediction ##
######################

#curl -s -H "Content-Type: application/json" -d '{"fecha":"2018-06-06"}' http://localhost:8000/queries.json
# {"label":448360.91039487766}


######################
## Batch prediction ##
######################
#pio batchpredict --input barcel_batchpredict_input.json --output barcel_batchpredict_output.json
# That generates a folder named barcel_batchpredict_output.json
#cat barcel_batchpredict_output.json/part-* > barcel_batchpredict_output_all.json
# That concatenates the distint parts of the query
#rm -r barcel_batchpredict_output.json