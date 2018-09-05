package org.template.regression

import org.apache.predictionio.controller.{EmptyEvaluationInfo, Engine, EngineFactory}

case class Query(fecha: String, agencia_id: Long, canal_id: Long, producto_id: Long, vector:Array[Double]=Array(),Week:Long,Month:Long,lag8month:Long,lag4month:Long,lag2month:Long,lag1month:Long,lag8monthnoagencia:Long,lag4monthnoagencia:Long,lag2monthnoagencia:Long,lag1monthnoagencia:Long,lag8monthnocanalandnoagencia:Long,lag4monthnocanalandnoagencia:Long,lag2monthnocanalandnoagencia:Long,lag1monthnocanalandnoagencia:Long,lag1monthnocanal:Long,lag2monthnocanal:Long,lag4monthnocanal:Long,lag8monthnocanal:Long)
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
        "tree" -> classOf[DecisionTreeRegression]
      ),
      classOf[Serving]
    )
  }
}
