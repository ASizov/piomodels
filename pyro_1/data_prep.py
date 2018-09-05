import pandas as pd
import numpy as np
import datetime
from math import sqrt

df = pd.read_csv('iter10_test.csv')
df2 = pd.read_csv('iter10_train.csv')
df = df[df['venta_uni'] != 0]
df2 = df2[df2['venta_uni'] != 0]
df.to_csv('iter10_test_no_zeros.csv')
df2.to_csv('iter10_train_no_zeros.csv')
