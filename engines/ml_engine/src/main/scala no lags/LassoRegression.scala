package org.template.regression

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LassoModel, LassoWithSGD}
import org.apache.spark.rdd.RDD
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.storage.PropertyMap
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }
case class LassoParams(numIterations: Int, stepSize: Double, regParam: Double) extends Params

class LassoRegression(val ap: LassoParams)
  extends P2LAlgorithm[PreparedData, LassoModel, Query, PredictedResult] {

  @transient lazy val logger: Logger = Logger[this.type]

  override def train(sc: SparkContext, data: PreparedData): LassoModel = {
    def toLabelPoint(item: (String, PropertyMap)): LabeledPoint = item match {
      case (_, properties) =>
        val label = properties.get[Double]("label")
        val vectors = Vectors.dense(properties.get[Array[Double]]("vector"))
        LabeledPoint(label, vectors)
    }

    val labeledPoints: RDD[LabeledPoint] = data.values.map(toLabelPoint).cache

    LassoWithSGD.train(
      labeledPoints,
      ap.numIterations,
      ap.stepSize,
      ap.regParam
    )
  }
  object MyUtils{
          val dtFormatter = DateTimeFormat.forPattern("yyyyMMdd" )
       }
  import MyUtils._
  def dateStringToMillis(dateStr: String) = dtFormatter.parseDateTime(dateStr).getMillis()
  override def predict(model: LassoModel, query: Query): PredictedResult = {
   val features = Vectors.dense(dateStringToMillis(query.fecha), query.agencia_id, query.canal_id, query.producto_id)
   val prediction = model.predict(features)
    print("\n")
    print("LassoR")
    print("prediction=")
    print(prediction)
    PredictedResult(prediction)
  }
}
