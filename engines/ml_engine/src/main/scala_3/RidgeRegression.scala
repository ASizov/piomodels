package org.template.regression

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, RidgeRegressionModel, RidgeRegressionWithSGD}
import org.apache.spark.rdd.RDD
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.storage.PropertyMap
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }
case class RidgeParams(numIterations: Int, stepSize: Double, regParam: Double) extends Params

class RidgeRegression(val ap: RidgeParams)
  extends P2LAlgorithm[PreparedData, RidgeRegressionModel, Query, PredictedResult] {

  @transient lazy val logger: Logger = Logger[this.type]

  override def train(sc: SparkContext, data: PreparedData): RidgeRegressionModel = {
    def toLabelPoint(item: (String, PropertyMap)): LabeledPoint = item match {
      case (_, properties) =>
        val label = properties.get[Double]("label")
        val vectors = Vectors.dense(properties.get[Array[Double]]("vector"))
        LabeledPoint(label, vectors)
    }

    val labeledPoints: RDD[LabeledPoint] = data.values.map(toLabelPoint).cache

    RidgeRegressionWithSGD.train(
      labeledPoints,
      ap.numIterations,
      ap.stepSize,
      ap.regParam
    )
  }
  object MyUtils{
          val dtFormatter = DateTimeFormat.forPattern("yyyy-MM-dd" )
       }
  import MyUtils._

  def dateStringToMillis(dateStr: String) = dtFormatter.parseDateTime(dateStr).getMillis()

  override def predict(model: RidgeRegressionModel, query: Query): PredictedResult = {
    val features = Vectors.dense(dateStringToMillis(query.fecha), query.agencia_id, query.canal_id, query.producto_id)
    val prediction = model.predict(features)
    print("\n")
    print("RidgeR")
    print("prediction=")
    print(prediction)
    PredictedResult(prediction)
  }
}
