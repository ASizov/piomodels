package org.jpioug.template.bhp

import org.apache.predictionio.controller.{P2LAlgorithm, Params}
import org.apache.spark.SparkContext
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.functions.{unix_timestamp, col}
import org.apache.spark.sql.SparkSession
import org.jpioug.template.python.{Engine, PreparedData}

case class AlgorithmParams(name: String) extends Params

case class Query(fecha: String)

case class PredictedResult(label: Double) extends Serializable

class Algorithm(val ap: AlgorithmParams)
  extends P2LAlgorithm[PreparedData, PipelineModel, Query, PredictedResult] {

  def train(sc: SparkContext, data: PreparedData): PipelineModel = {
    Engine.modelRef.get()
  }

  def predict(model: PipelineModel, query: Query): PredictedResult = {
    val spark = SparkSession
      .builder
      .getOrCreate()
    import spark.implicits._
    val data = Seq((query.fecha))
    val df = spark.createDataset(data).toDF("fecha")
    val df_parsed = df.withColumn("fecha", unix_timestamp(col("fecha"), "yyyy-MM-dd").cast("int"))
    val labelDf = model.transform(df_parsed)
    PredictedResult(labelDf.select("prediction").first().getAs(0))
  }
}

