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
        val vectors = Vectors.dense(properties.get[Double]("fecha"), properties.get[Double]("agencia_id"), properties.get[Double]("canal_id"), properties.get[Double]("producto_id"),properties.get[Double]("Week"),properties.get[Double]("Month"),properties.get[Double]("lag1month"),properties.get[Double]("lag05month"),properties.get[Double]("fechaLab"),properties.get[Double]("season"),properties.get[Double]("isholiday"),properties.get[Double]("isholidayyesterday"),properties.get[Double]("isholidaytomorrow"),properties.get[Double]("price"),properties.get[Double]("lag05rollavg2"),properties.get[Double]("lag05rollavg3"),properties.get[Double]("lag2rollavg3"),properties.get[Double]("lag05ewma3"),properties.get[Double]("lag05ewma8"),properties.get[Double]("lag2ewma3"),properties.get[Double]("lag4ewma3"),properties.get[Double]("lag05ewma3lag05ewma8"),properties.get[Double]("lag2ewma3lag4ewma3"))
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
    val features = Vectors.dense(dateStringToMillis(query.fecha), query.agencia_id, query.canal_id, query.producto_id,query.Month,query.Week,query.lag1month,query.lag05month,query.fechaLab,query.season,query.isholiday,query.isholidayyesterday,query.isholidaytomorrow,query.price,query.lag05rollavg2,query.lag05rollavg3,query.lag2rollavg3,query.lag05ewma3,query.lag05ewma8,query.lag2ewma3,query.lag4ewma3,query.lag05ewma3lag05ewma8,query.lag2ewma3lag4ewma3)
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
