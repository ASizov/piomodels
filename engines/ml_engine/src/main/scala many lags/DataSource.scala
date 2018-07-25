package org.template.regression

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.{EmptyEvaluationInfo, PDataSource, Params, EmptyParams}
import org.apache.predictionio.data.storage.PropertyMap
import org.apache.predictionio.data.store.PEventStore
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.json4s._
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }

case class DataSourceParams() extends Params

class DataSource(ep: EmptyParams)
  extends PDataSource[TrainingData, EmptyEvaluationInfo, Query, ActualResult] {

  @transient lazy val logger: Logger = Logger[this.type]

  override def readTraining(sc: SparkContext): TrainingData = {
    TrainingData(selectEvents(sc))
  }

  override def readEval(sc: SparkContext): Seq[(TrainingData, EmptyEvaluationInfo, RDD[(Query, ActualResult)])] = {
    val events = selectEvents(sc)
    val rdd = events.map {
      case (_, properties) =>
        Query(
            fecha = properties.get[String]("fecha"),
            agencia_id = properties.get[Int]("agencia_id"),
            canal_id = properties.get[Int]("canal_id"),
            producto_id = properties.get[Int]("producto_id"),
	    lag1month = properties.get[Int]("lag1month"),
	    lag2month = properties.get[Int]("lag2month"),
		lag4month = properties.get[Int]("lag4month"),
		lag8month = properties.get[Int]("lag8month"),
		Week = properties.get[Int]("Week"),
		Month = properties.get[Int]("Month"),
		lag8monthnoagencia = properties.get[Int]("lag8monthnoagencia"),
		lag4monthnoagencia = properties.get[Int]("lag4monthnoagencia"),
		lag2monthnoagencia = properties.get[Int]("lag2monthnoagencia"),
		lag1monthnoagencia = properties.get[Int]("lag1monthnoagencia"),
		lag8monthnocanalandnoagencia = properties.get[Int]("lag8monthnocanalandnoagencia"),
		lag4monthnocanalandnoagencia = properties.get[Int]("lag4monthnocanalandnoagencia"),
		lag2monthnocanalandnoagencia = properties.get[Int]("lag2monthnocanalandnoagencia"),
		lag1monthnocanalandnoagencia = properties.get[Int]("lag1monthnocanalandnoagencia"),
		lag1monthnocanal = properties.get[Int]("lag1monthnocanal"),
		lag2monthnocanal = properties.get[Int]("lag2monthnocanal"),
		lag4monthnocanal = properties.get[Int]("lag4monthnocanal"),
		lag8monthnocanal = properties.get[Int]("lag8monthnocanal")			 

        ) -> ActualResult(properties.get[Double]("label"))
    }
    val eval = (TrainingData(events), new EmptyEvaluationInfo(), rdd)
    Seq(eval)
  }

 object MyUtils{
    val dtFormatter = DateTimeFormat.forPattern("yyyy-MM-dd" )
 }
 import MyUtils._
  def dateStringToMillis(dateStr: String) = dtFormatter.parseDateTime(dateStr).getMillis()

  def selectEvents(sc: SparkContext): RDD[(String, PropertyMap)] = {
    val grades = PEventStore.aggregateProperties(
      appName = "Barcel_App",
      entityType = "user",
      required = Some(List("fecha", "agencia_id", "canal_id", "producto_id", "venta_uni","lag1month","lag2month","lag4month","lag8month","lag1monthnoagencia","lag2monthnoagencia","lag4monthnoagencia","lag8monthnoagencia","lag1monthnocanalandnoagencia","lag2monthnocanalandnoagencia","lag4monthnocanalandnoagencia","lag8monthnocanalandnoagencia","lag1monthnocanal","lag2monthnocanal","lag4monthnocanal","lag8monthnocanal","Week","Month"))
    )(sc)
    print("propertyMap")

    val events = grades.map {
      case (entityId, properties) =>
        val fields = Map(
          "fecha" -> JDouble(dateStringToMillis(properties.get[String]("fecha"))),
          "agencia_id" ->   JDouble(properties.get[Double]("agencia_id")),
          "canal_id" ->   JDouble(properties.get[Double]("canal_id")),
          "producto_id" ->   JDouble(properties.get[Double]("producto_id")),
		"lag1month" ->   JDouble(properties.get[Double]("lag1month")),
		"lag2month" ->   JDouble(properties.get[Double]("lag2month")),
		"lag4month" ->   JDouble(properties.get[Double]("lag4month")),
		"lag8month" ->   JDouble(properties.get[Double]("lag8month")),
		"lag1monthnoagencia" ->   JDouble(properties.get[Double]("lag1monthnoagencia")),
		"lag2monthnoagencia" ->   JDouble(properties.get[Double]("lag2monthnoagencia")),
		"lag4monthnoagencia" ->   JDouble(properties.get[Double]("lag4monthnoagencia")),
		"lag8monthnoagencia" ->   JDouble(properties.get[Double]("lag8monthnoagencia")),
		"lag1monthnocanalandnoagencia" ->   JDouble(properties.get[Double]("lag8monthnocanalandnoagencia")),
		"lag2monthnocanalandnoagencia" ->   JDouble(properties.get[Double]("lag8monthnocanalandnoagencia")),
		"lag4monthnocanalandnoagencia" ->   JDouble(properties.get[Double]("lag8monthnocanalandnoagencia")),
		"lag8monthnocanalandnoagencia" ->   JDouble(properties.get[Double]("lag8monthnocanalandnoagencia")),
		"lag1monthnocanal" ->   JDouble(properties.get[Double]("lag1monthnocanal")),
		"lag2monthnocanal" ->   JDouble(properties.get[Double]("lag2monthnocanal")),
		"lag4monthnocanal" ->   JDouble(properties.get[Double]("lag4monthnocanal")),
		"lag8monthnocanal" ->   JDouble(properties.get[Double]("lag8monthnocanal")),
		"Week" ->   JDouble(properties.get[Double]("Week")),
		"Month" ->   JDouble(properties.get[Double]("Month")),			
           "label" -> JDouble(properties.get[Double]("venta_uni"))
        )

        val propertyMap = PropertyMap(fields, properties.firstUpdated, properties.lastUpdated)
        print(propertyMap)
        entityId -> propertyMap
    }

    events.cache
  }
}

case class TrainingData(values: RDD[(String, PropertyMap)]) {
  override def toString: String = {
    s"values: [${values.count()}] (${values.take(3).toList}...)"
  }
}
