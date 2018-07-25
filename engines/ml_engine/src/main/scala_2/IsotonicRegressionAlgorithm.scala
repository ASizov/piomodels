package org.template.regression

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{IsotonicRegression, IsotonicRegressionModel, LabeledPoint}
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.storage.PropertyMap
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }
case class IsotonicRegressionParams(numIterations: Int, stepSize: Double) extends Params

class IsotonicRegressionAlgorithm(val ap: IsotonicRegressionParams)
  extends P2LAlgorithm[PreparedData, IsotonicRegressionModel, Query, PredictedResult] {

  @transient lazy val logger: Logger = Logger[this.type]

  override def train(sc: SparkContext, data: PreparedData): IsotonicRegressionModel = {
    def toWeightedVector(item: (String, PropertyMap)) = item match {
      case (_, properties) =>
        val label = properties.get[Double]("label")
        val features = properties.get[Array[Double]]("vector")
        (label, features(0), 1.0)
    }

    val parsedData = data.values.map(toWeightedVector).cache

    val isoReg = new org.apache.spark.mllib.regression.IsotonicRegression()
    isoReg.setIsotonic(true).run(parsedData)
  }

  object MyUtils{
        val dtFormatter = DateTimeFormat.forPattern("yyyyMMdd" )
     }
  import MyUtils._

  def dateStringToMillis(dateStr: String) = dtFormatter.parseDateTime(dateStr).getMillis()
  override def predict(model: IsotonicRegressionModel, query: Query): PredictedResult = {
    val features = Vectors.dense(dateStringToMillis(query.fecha), query.agencia_id, query.canal_id, query.producto_id)
    val prediction = model.predict(features(0))
    print("\n")
    print("IsotonicR")
    print("prediction=")
    print(prediction)
    print("\n")
    PredictedResult(prediction)
  }
}
