package org.template.regression

import org.apache.predictionio.controller.{EmptyEvaluationInfo, Engine, EngineFactory}

case class Query(fecha: String, agencia_id: Long, canal_id: Long, producto_id: Long, vector:Array[Double]=Array(),Week:Long,Month:Long,lag05month:Long,lag1month:Long,fechaLab:Long,season:Long,isholiday:Long,isholidayyesterday:Long,isholidaytomorrow:Long,price:Long,lag05rollavg3:Long,lag2rollavg3:Long,lag05ewma3:Long,lag05ewma8:Long,lag2ewma3:Long,lag4ewma3:Long,lag05ewma3lag05ewma8:Long,lag2ewma3lag4ewma3:Long,lag05rollavg2:Long)
case class PredictedResult(
  prediction: Double
)
case class ActualResult(label: Double)

object RegressionEngine extends EngineFactory {
  type Type = Engine[TrainingData, EmptyEvaluationInfo, PreparedData, Query, PredictedResult, ActualResult]

  def apply(): Type = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map(
        "tree" -> classOf[DecisionTreeRegression],
	"LassoRegression" -> classOf[LassoRegression],
	"randomforest" -> classOf[RandomForestAlgorithm]
      ),
      classOf[Serving]
    )
  }
}
