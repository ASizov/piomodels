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
		lag05month = properties.get[Int]("lag05month"),
		lag3month = properties.get[Int]("lag3month"),
		Week = properties.get[Int]("Week"),
		fechaLab = properties.get[Int]("fechaLab"),
		Month = properties.get[Int]("Month"),
		season = properties.get[Int]("season"),
		isholiday = properties.get[Int]("isholiday"),
		isholidayyesterday = properties.get[Int]("isholidayyesterday"),
		isholidaytomorrow = properties.get[Int]("isholidaytomorrow"),
		price = properties.get[Int]("price"),
		popularity = properties.get[Int]("popularity")			 

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
      required = Some(List("fecha", "agencia_id", "canal_id", "producto_id", "venta_uni","lag1month","lag2month","lag4month","lag3month","lag05month","Week","Month","fechaLab","season","isholiday","isholidayyesterday","isholidaytomorrow","price","popularity"))
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
		"lag3month" ->   JDouble(properties.get[Double]("lag3month")),
		"lag05month" ->   JDouble(properties.get[Double]("lag05month")),
		"Week" ->   JDouble(properties.get[Double]("Week")),
		"Month" ->   JDouble(properties.get[Double]("Month")),
		"fechaLab" ->   JDouble(properties.get[Double]("fechaLab")),
		"popularity" ->   JDouble(properties.get[Double]("popularity")),
		"price" ->   JDouble(properties.get[Double]("price")),
		"isholidaytomorrow" ->   JDouble(properties.get[Double]("isholidaytomorrow")),
		"isholidayyesterday" ->   JDouble(properties.get[Double]("isholidayyesterday")),
		"isholiday" ->   JDouble(properties.get[Double]("isholiday")),
		"season" ->   JDouble(properties.get[Double]("season")),	
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
