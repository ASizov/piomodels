# coding: utf-8

# Search pio event
import atexit
import platform

import py4j
import sys

import pyspark
from pyspark.context import SparkContext
from pyspark.sql import SparkSession, SQLContext
from pyspark.storagelevel import StorageLevel
from pypio.utils import new_string_array
from pypio.data import PEventStore


SparkContext._ensure_initialized()
try:
    SparkContext._jvm.org.apache.hadoop.hive.conf.HiveConf()
    spark = SparkSession.builder.enableHiveSupport().getOrCreate()
except py4j.protocol.Py4JError:
    spark = SparkSession.builder.getOrCreate()
except TypeError:
    spark = SparkSession.builder.getOrCreate()

sc = spark.sparkContext
sql = spark.sql
atexit.register(lambda: sc.stop())

sqlContext = spark._wrapped
sqlCtx = sqlContext

p_event_store = PEventStore(spark._jsparkSession, sqlContext)


def run_pio_workflow(model):
    template_engine = sc._jvm.org.jpioug.template.python.Engine
    template_engine.modelRef().set(model._to_java())
    main_args = new_string_array(sys.argv, sc._gateway)
    create_workflow = sc._jvm.org.apache.predictionio.workflow.CreateWorkflow
    sc.stop()
    create_workflow.main(main_args)

from pyspark.sql.functions import col
from pyspark.sql.functions import unix_timestamp
from pyspark.sql.functions import monotonically_increasing_id
from pyspark.ml.feature import VectorAssembler
from pyspark.ml.evaluation import RegressionEvaluator
from pyspark.ml.regression import GeneralizedLinearRegression
from pyspark.ml import Pipeline

# Import events
event_df = p_event_store.find('Barcel_App')
event_df.show(5)

# Import data
def get_field_type(name):
    if name == 'venta_uni':
        return 'int'
    else:
        return 'string'

field_names = ['fecha', 'venta_uni']
exprs = [col('fields').getItem(k).cast(get_field_type(k)).alias(k) for k in field_names]
data_df = event_df.select(*exprs)
data_df = data_df.withColumnRenamed("venta_uni", "label")
data_df.show(5)


# Convert date to number
parsed_data_df = data_df.withColumn('fecha', unix_timestamp(col('fecha'), format='yyyy-MM-dd').cast('int'))
parsed_data_df = parsed_data_df.orderBy("fecha")

# Divide in training and test data
n = parsed_data_df.count()
train_n = int(round(0.9*n))
test_n = n - train_n
df = parsed_data_df.withColumn('index', monotonically_increasing_id())
train_df = df.sort('index').limit(train_n).drop('index')
test_df = df.sort('index', ascending=False).limit(test_n).drop('index')

# Construct model
featureAssembler = VectorAssembler(inputCols=[x for x in field_names if x != 'venta_uni'],
                                   outputCol='features')
glm = GeneralizedLinearRegression(featuresCol='features', labelCol='label', predictionCol='prediction',
    family='poisson', link='log', regParam=0.3)
pipeline = Pipeline(stages=[featureAssembler, glm])
model = pipeline.fit(train_df)

# Print summary of model
print(model.stages[1].summary)

# Test model
predict_df = model.transform(test_df)
predict_df.select("prediction", "label").show(5)

rmse_evaluator = RegressionEvaluator(labelCol="label", predictionCol="prediction", metricName="rmse")
mae_evaluator = RegressionEvaluator(labelCol="label", predictionCol="prediction", metricName="mae")
rmse = rmse_evaluator.evaluate(predict_df)
mae = mae_evaluator.evaluate(predict_df)
print("Root Mean Squared Error (RMSE) on test data = %g" % rmse)
print("Mean Absolute Error (MAE) on test data = %g" % mae)

# Add model to PredictionIO:

run_pio_workflow(model)
