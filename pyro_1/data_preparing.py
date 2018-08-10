import pandas as pd
import numpy as np
import datetime

df = pd.read_csv('big_data.csv')
print(df)
#df = df.sample(frac=1)
df['agencia_orig']=df['agencia_id']
df['producto_orig']=df['producto_id']
df['canal_orig']=df['canal_id']
df['agencia_id'] = df.groupby('agencia_id').ngroup()
df['producto_id'] = df.groupby('producto_id').ngroup()
df['canal_id'] = df.groupby('canal_id').ngroup()
df['gerencia_detalle'] = df.groupby('gerencia_detalle').ngroup()
df['gerencia_autoservicio'] = df.groupby('gerencia_autoservicio').ngroup()
df['fechaLab'] = df.groupby('fecha').ngroup()
df['fecha'] = pd.to_datetime(df['fecha'])
df['Weekday'] = df['fecha'].dt.weekday
holiday_mexico = ['2017-01-01', '2017-01-02', '2017-02-06', '2017-03-20', '2017-04-13', '2017-04-14', '2017-05-01', '2017-05-05', '2017-05-10', '2017-06-18', '2017-07-18', '2017-09-16', '2017-10-12', '2017-11-02', '2017-11-20', '2017-12-12', '2017-12-25', '2018-01-01', '2018-01-06', '2018-02-05', '2018-02-14', '2018-02-24', '2018-03-19', '2018-03-21', '2018-03-29', '2018-03-30', '2018-04-01', '2018-05-01', '2018-05-10', '2018-06-17', '2018-07-01']
df['isholiday']=0
df['isholidayyesterday']=0
df['isholidaytomorrow']=0


df.isholiday[df.fecha.isin(holiday_mexico)] = 1
#df['isholidayyesterday']=(df.fecha-np.timedelta64(1,'D')).isin(holiday_mexico)
#df['isholidaytomorrow']=(df.fecha+np.timedelta64(1,'D')).isin(holiday_mexico)
df.isholidayyesterday[(df.fecha-np.timedelta64(1,'D')).isin(holiday_mexico)] = 1
df.isholidaytomorrow[(df.fecha+np.timedelta64(1,'D')).isin(holiday_mexico)] = 1
#df['lags1month'] = df.venta_uni[df.fecha<=(df.fecha+np.timedelta64(1,'D'))].sum()
#df['lag1month'] = 0
dfgrouped=df.groupby(['agencia_id', 'producto_id','canal_id'])
df['lag1']=dfgrouped['venta_uni'].apply(lambda x:x.shift(1))
df['lag2']=dfgrouped['venta_uni'].apply(lambda x:x.shift(2))
df['lag3']=dfgrouped['venta_uni'].apply(lambda x:x.shift(3))
df['lag4']=dfgrouped['venta_uni'].apply(lambda x:x.shift(4))
#df['lag5']=dfgrouped['venta_uni'].apply(lambda x:x.shift(5))
#df['lag6']=dfgrouped['venta_uni'].apply(lambda x:x.shift(6))
#df['lag7']=dfgrouped['venta_uni'].apply(lambda x:x.shift(7))
#df['lag8']=dfgrouped['venta_uni'].apply(lambda x:x.shift(8))
#df['lag9']=dfgrouped['venta_uni'].apply(lambda x:x.shift(9))
#df['lag10']=dfgrouped['venta_uni'].apply(lambda x:x.shift(10))
#df['lag11']=dfgrouped['venta_uni'].apply(lambda x:x.shift(11))
#df['lag12']=dfgrouped['venta_uni'].apply(lambda x:x.shift(12))
sum05m_list= ['lag1','lag2']
sum1m_list= ['lag1','lag2','lag3','lag4']
#sum2m_list= ['lag1','lag2','lag3','lag4','lag5','lag6','lag7','lag8']
#sum3m_list= ['lag1','lag2','lag3','lag4','lag5','lag6','lag7','lag8','lag9','lag10','lag11','lag12']
df['sum05m_list'] = df[sum05m_list].sum(axis=1)
df['sum1m_list'] = df[sum1m_list].sum(axis=1)
df['avg05m_list'] = df[sum05m_list].mean(axis=1)
df['avg1m_list'] = df[sum1m_list].mean(axis=1)
df['median05m_list'] = df[sum05m_list].median(axis=1)
df['median1m_list'] = df[sum1m_list].median(axis=1)
df['std05m_list'] = df[sum05m_list].std(axis=1)
df['std1m_list'] = df[sum1m_list].std(axis=1)
df['min05m_list'] = df[sum05m_list].min(axis=1)
df['min1m_list'] = df[sum1m_list].min(axis=1)
df['max05m_list'] = df[sum05m_list].max(axis=1)
df['max1m_list'] = df[sum1m_list].max(axis=1)
#df['sum2m_list'] = df[sum2m_list].sum(axis=1)
#df['sum3m_list'] = df[sum3m_list].sum(axis=1)
df['Week'] = df['fecha'].dt.week
df['Month'] = df['fecha'].dt.month
df['lag05rollavg2'] = df['lag2'].rolling(2).mean().round()
df['lag05rollavg3'] = df['lag2'].rolling(3).mean().round()
df['lag2rollavg3'] = df['lag4'].rolling(2).mean().round()
df['lag05ewma3']=df['lag2'].ewm(span=3).mean().round()
df['lag05ewma8']=df['lag2'].ewm(span=8).mean().round()
df['lag2ewma3']=df['lag4'].ewm(span=3).mean().round()
df['lag4ewma3']=df['lag4'].ewm(span=3).mean().round()
df['lag05ewma3-lag05ewma8']=df['lag05ewma3']-df['lag05ewma8']
df['lag2ewma3-lag4ewma3']=df['lag2ewma3']-df['lag4ewma3']
df.to_csv('iter.csv',index=False)
