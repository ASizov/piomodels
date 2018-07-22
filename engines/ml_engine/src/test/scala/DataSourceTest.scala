package org.template.regression

import org.scalatest._
import org.scalatest.Matchers
import org.apache.predictionio.controller.{EmptyParams}

@Ignore
class DataSourceTest
  extends FlatSpec with SharedSingletonContext with Matchers {

  ignore should "return the data" in {
    val dataSource = new DataSource(
      new EmptyParams())
    val data = dataSource.readTraining(sc = sparkContext)
    data shouldBe a [TrainingData]
  }
}