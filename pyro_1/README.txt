agencia_id - labeled agencia_id
canal_id - labeled canal_id
producto_id - labeled producto_id
fechaLab - labeled fecha
Weekday
isholiday
isholidayyesterday
isholidaytomorrow
lag1 - shift 1 row back grouped by canal, agencia, producto
lag2 - shift 2 row back grouped by canal, agencia, producto
lag3 - shift 3 row back grouped by canal, agencia, producto
lag4 - shift 4 row back grouped by canal, agencia, producto
sum05m_list = sum lag1 + lag2
sum1m_list = sum 'lag1','lag2','lag3','lag4'
avg05m_list = sum lag1 + lag2
avg1m_list = 'lag1','lag2','lag3','lag4'
std05m_list = std lag1 + lag2
std1m_list = std 'lag1','lag2','lag3','lag4'
median05m_list = median lag1 + lag2
median1m_list = median 'lag1','lag2','lag3','lag4'
min05m_list = min lag1 + lag2
min1m_list = min 'lag1','lag2','lag3','lag4'
max05m_list = max lag1 + lag2
max1m_list = max'lag1','lag2','lag3','lag4'
lag05rollavg2 = df['lag2'].rolling(2).mean().round()
lag05rollavg3 = df['lag2'].rolling(3).mean().round()
lag2rollavg3 = df['lag4'].rolling(2).mean().round()
lag05ewma3=df['lag2'].ewm(span=3).mean().round()
lag05ewma8=df['lag2'].ewm(span=8).mean().round()
lag2ewma3=df['lag4'].ewm(span=3).mean().round()
lag4ewma3=df['lag4'].ewm(span=3).mean().round()
lag05ewma3-lag05ewma8=lag05ewma3-dflag05ewma8

