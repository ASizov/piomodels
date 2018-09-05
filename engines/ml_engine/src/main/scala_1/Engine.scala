package org.template.regression

import org.apache.predictionio.controller.{EmptyEvaluationInfo, Engine, EngineFactory}

case class Query(fecha: String, agencia_id: Long, canal_id: Long, producto_id: Long, vector:Array[Double]=Array())
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
