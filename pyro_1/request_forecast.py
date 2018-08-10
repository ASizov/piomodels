from service_provider import ServiceProvider
import pandas as pd
import numpy as np
import datetime
from math import sqrt
from sklearn.metrics import mean_squared_error
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import explained_variance_score
from sklearn.metrics import r2_score
service_provider = ServiceProvider()
csv_path = "./sample_data_barcel.csv"
# csv_path = "/home/victor/Documents/data/barcel_data_spliced.csv"
import time

############### Test automl
#service_provider.first_interaction(csv_path=csv_path, label="venta_uni", lag_col="fecha", aggregation=["agencia_id","canal_id","producto_id","season","isholidayyesterday","isholidaytomorrow","Week","Month"], freq="W", lag=2, model_params={}, model_name="automl")
'''
df = pd.read_csv('resultados.csv')
dfList = df['venta_uni'].tolist()
predicted = df['prediction'].tolist()
	#print(predicted)
print(sqrt(mean_squared_error(dfList, predicted)),' - RMSE')
print(mean_absolute_error(dfList, predicted),' - MAE')
print(explained_variance_score(dfList, predicted),' - variance')
print(r2_score(dfList, predicted),' - r2_score')
'''

for x in range(1000):
	print(x)
	############### xgboost model
	service_provider2 = ServiceProvider()
	model_params = {"max_depth": 4,
	"learning_rate": 0.43,
	"n_estimators": 500,
	"booster": "dart", 
	"gamma": 5,
	"min_child_weight": 27,#12
	"max_delta_step": 0,
	"subsample": 0.44, #0.36
	"colsample_bytree": 1,
	"colsample_bylevel": 1,
	"base_score": 0.5
	}

	service_provider2.first_interaction(csv_path=csv_path, label="venta_uni", lag_col="fecha", aggregation=["fecha", "producto_id"], freq="W", lag=2, model_params=model_params, model_name="xgbregressor")


	df = pd.read_csv('resultados.csv')
	dfList = df['venta_uni'].tolist()
	predicted = df['prediction'].tolist()
	print('------')
	print(sqrt(mean_squared_error(dfList, predicted)),' - RMSE')
	print(mean_absolute_error(dfList, predicted),' - MAE')
	print(explained_variance_score(dfList, predicted),' - variance')
	print(r2_score(dfList, predicted),' - r2_score')
	



