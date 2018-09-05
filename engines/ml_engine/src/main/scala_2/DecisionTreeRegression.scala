package org.template.regression

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint}
import org.apache.spark.rdd.RDD
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.storage.PropertyMap

import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.util.MLUtils
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }

case class DecisionTreeParams(impurity: String, maxDepth: Int, maxBins: Int) extends Params

class DecisionTreeRegression(val ap: DecisionTreeParams)
  extends P2LAlgorithm[PreparedData, DecisionTreeModel, Query, PredictedResult] {

  @transient lazy val logger: Logger = Logger[this.type]

  override def train(sc: SparkContext, data: PreparedData): DecisionTreeModel = {
    def toLabelPoint(item: (String, PropertyMap)): LabeledPoint = item match {
      case (_, properties) =>
        val label = properties.get[Double]("label")
        val vectors = Vectors.dense(properties.get[Double]("fecha"), properties.get[Double]("agencia_id"), properties.get[Double]("canal_id"), properties.get[Double]("producto_id"),properties.get[Double]("Week"),properties.get[Double]("Month"),properties.get[Double]("lag8monthnoagencia"),properties.get[Double]("lag1month"),properties.get[Double]("lag4monthnoagencia"),properties.get[Double]("lag2month"),properties.get[Double]("lag2monthnoagencia"),properties.get[Double]("lag4month"),properties.get[Double]("lag1monthnoagencia"),properties.get[Double]("lag8month"),properties.get[Double]("lag8monthnocanalandnoagencia"),properties.get[Double]("lag4monthnocanalandnoagencia"),properties.get[Double]("lag2monthnocanalandnoagencia"),properties.get[Double]("lag1monthnocanal"),properties.get[Double]("lag1monthnocanalandnoagencia"),properties.get[Double]("lag2monthnocanal"),properties.get[Double]("lag4monthnocanal"),properties.get[Double]("lag8monthnocanal"))
        print(label)

        print("\n")
        print(vectors)
        LabeledPoint(label, vectors)
    }
    print(data)
    val labeledPoints: RDD[LabeledPoint] = data.values.map(toLabelPoint).cache
    print(labeledPoints)
    val categoricalFeaturesInfo = Map[Int, Int]()
    print(categoricalFeaturesInfo)
    DecisionTree.trainRegressor(labeledPoints, categoricalFeaturesInfo, ap.impurity, ap.maxDepth, ap.maxBins)
  }

  object MyUtils{
      val dtFormatter = DateTimeFormat.forPattern("yyyy-MM-dd" )
   }
  import MyUtils._

  def dateStringToMillis(dateStr: String) = dtFormatter.parseDateTime(dateStr).getMillis()

  override def predict(model: DecisionTreeModel, query: Query): PredictedResult = {
    println("1888")
    val features = Vectors.dense(dateStringToMillis(query.fecha), query.agencia_id, query.canal_id, query.producto_id,query.Month,query.Week,query.lag8monthnocanal,query.lag4monthnocanal,query.lag2monthnocanal,query.lag1monthnocanal,query.lag8monthnocanalandnoagencia,query.lag4monthnocanalandnoagencia,query.lag2monthnocanalandnoagencia,query.lag1monthnocanalandnoagencia,query.lag4monthnoagencia,query.lag2monthnoagencia,query.lag1monthnoagencia,query.lag8monthnoagencia,query.lag1month,query.lag2month,query.lag4month,query.lag8month)
    print("\n")
    println(features)


    val prediction = model.predict(features)
    print("\n")
    print("DT")
    print("prediction=")
    print(prediction)
    print("\n")
    PredictedResult(prediction)
  }
}
