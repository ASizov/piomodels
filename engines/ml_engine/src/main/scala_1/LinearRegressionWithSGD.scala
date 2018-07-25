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
        val vectors = Vectors.dense(properties.get[Double]("fecha"), properties.get[Double]("agencia_id"), properties.get[Double]("canal_id"), properties.get[Double]("producto_id"))
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
    val features = Vectors.dense(dateStringToMillis(query.fecha), query.agencia_id, query.canal_id, query.producto_id)
    val prediction = model.predict(features)
    print("\n")
    print("LinarR")
    print("prediction=")
    print(prediction)
    PredictedResult(prediction)
  }
}
