package org.template.regression

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel, LinearRegressionWithSGD}
import org.apache.spark.rdd.RDD
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.storage.PropertyMap
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }
case class AlgorithmParams(numIterations: Int, stepSize: Double) extends Params

class LinearRegressionWithSGD(val ap: AlgorithmParams)
  extends P2LAlgorithm[PreparedData, LinearRegressionModel, Query, PredictedResult] {

  @transient lazy val logger: Logger = Logger[this.type]

  override def train(sc: SparkContext, data: PreparedData): LinearRegressionModel = {
    def toLabelPoint(item: (String, PropertyMap)): LabeledPoint = item match {
      case (_, properties) =>
        val label = properties.get[Double]("label")
       val vectors = Vectors.dense(properties.get[Double]("fecha"), properties.get[Double]("agencia_id"), properties.get[Double]("canal_id"), properties.get[Double]("producto_id"),properties.get[Double]("Week"),properties.get[Double]("Month"),properties.get[Double]("lag1month"),properties.get[Double]("lag05month"),properties.get[Double]("fechaLab"),properties.get[Double]("season"),properties.get[Double]("isholiday"),properties.get[Double]("isholidayyesterday"),properties.get[Double]("isholidaytomorrow"),properties.get[Double]("price"),properties.get[Double]("lag05rollavg2"),properties.get[Double]("lag05rollavg3"),properties.get[Double]("lag2rollavg3"),properties.get[Double]("lag05ewma3"),properties.get[Double]("lag05ewma8"),properties.get[Double]("lag2ewma3"),properties.get[Double]("lag4ewma3"),properties.get[Double]("lag05ewma3lag05ewma8"),properties.get[Double]("lag2ewma3lag4ewma3"))
        print(label)
        LabeledPoint(label, vectors)
    }

    val labeledPoints: RDD[LabeledPoint] = data.values.map(toLabelPoint).cache

    LinearRegressionWithSGD.train(
      labeledPoints,
      ap.numIterations,
      ap.stepSize
    )
  }

  object MyUtils{
        val dtFormatter = DateTimeFormat.forPattern("yyyy-MM-dd" )
     }
  import MyUtils._

  def dateStringToMillis(dateStr: String) = dtFormatter.parseDateTime(dateStr).getMillis()

  override def predict(model: LinearRegressionModel, query: Query): PredictedResult = {
     val features = Vectors.dense(dateStringToMillis(query.fecha), query.agencia_id, query.canal_id, query.producto_id,query.Month,query.Week,query.lag1month,query.lag05month,query.fechaLab,query.season,query.isholiday,query.isholidayyesterday,query.isholidaytomorrow,query.price,query.lag05rollavg2,query.lag05rollavg3,query.lag2rollavg3,query.lag05ewma3,query.lag05ewma8,query.lag2ewma3,query.lag4ewma3,query.lag05ewma3lag05ewma8,query.lag2ewma3lag4ewma3)
    val prediction = model.predict(features)
    print("\n")
    print("LinarR")
    print("prediction=")
    print(prediction)
    PredictedResult(prediction)
  }
}
