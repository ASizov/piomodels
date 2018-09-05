import pandas as pd
import numpy as np
import datetime

df = pd.read_csv('iter10_test.csv')
from sklearn.metrics import mean_squared_error
from sklearn.metrics import mean_absolute_error
import requests


predicted=[]
import json
for index, row in df.iterrows():
    headers = {"Content-Type": "application/json"}
    #payload = '{"fecha": "'+str(row.fecha)+'", "agencia_id":' +str(row.agencia_id)+',"canal_id":'+ str(row.canal_id)+', "producto_id":'+ str(row.producto_id)+'}'
    #payload = '{"fecha": "'+str(row.fecha)+'", "agencia_id":' +str(row.agencia_id)+',"canal_id":'+ str(row.canal_id)+', "producto_id":'+ str(row.producto_id)+',"lag1month":'+str(row.lag1month)+',"lag2month":'+str(row.lag2month)+',"lag4month":'+str(row.lag4month)+',"lag8month":'+str(row.lag8month)+'}'
    payload = '{"fecha": "'+str(row.fecha)+'", "agencia_id":' +str(row.agencia_id)+',"canal_id":'+ str(row.canal_id)+', "producto_id":'+ str(row.producto_id)+',"lag1month":'+str(row.lag1month)+',"Week":'+str(row.Week)+',"Month":'+str(row.Month)+',"lag05month":'+str(row.lag05month)+',"fechaLab":'+str(row.fechaLab)+',"isholidaytomorrow":'+str(row.isholidaytomorrow)+',"isholidayyesterday":'+str(row.isholidayyesterday)+',"isholiday":'+str(row.isholiday)+',"price":'+str(row.price)+',"lag05rollavg2":'+str(row.lag05rollavg2)+',"lag2rollavg3":'+str(row.lag2rollavg3)+',"lag05ewma3":'+str(row.lag05ewma3)+',"lag2ewma3":'+str(row.lag2ewma3)+',"lag05rollavg3":'+str(row.lag05rollavg3)+',"lag05ewma8":'+str(row.lag05ewma8)+',"lag4ewma3":'+str(row.lag4ewma3)+',"lag05ewma3lag05ewma8":'+str(row.lag05ewma3lag05ewma8)+',"lag2ewma3lag4ewma3":'+str(row.lag2ewma3lag4ewma3)+'}'
    r = requests.post("http://localhost:8000/queries.json", data=payload, headers=headers)
    #print(json.loads(r.text)['prediction'])
    print(index)
    predicted.append(json.loads(r.text)['prediction'])
dfList = df['venta_uni'].tolist()
print(predicted)
print(mean_squared_error(dfList, predicted),' - RMSE')
print(mean_absolute_error(dfList, predicted),' - MAE')

#df.to_csv('data_for_training1.csv')
