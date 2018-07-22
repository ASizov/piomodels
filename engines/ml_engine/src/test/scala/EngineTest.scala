package org.template.regression

import org.scalatest._
import org.scalatest.Matchers

import org.apache.predictionio.controller.Engine

@Ignore
class EngineTest
  extends FlatSpec with Matchers {

  "apply" should "return a new engine instance" in {
    val engine = RegressionEngine()
    engine shouldBe an [Engine[_,_,_,_,_,_]]
  }
}