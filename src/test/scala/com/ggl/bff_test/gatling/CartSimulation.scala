package com.ggl.bff_test.gatling

import com.ggl.bff_test.BffApplicationTest
import com.intuit.karate.gatling.PreDef._

import io.gatling.core.Predef._
import scala.concurrent.duration._

class CartSimulation extends Simulation {

  private val scenarios = List("scenario_TP", "scenario_TP_addCustomerToCart", "scenario_TP_addToCart", "scenario_TP_createArticle")

  private val directory = classOf[BffApplicationTest]
    .getPackageName
    .replace('.', '/')

  private val protocol = karateProtocol()
  protocol.nameResolver = (req, _) => req.getHeader("karate-name")

  setUp(
    scenario("createOrder").exec(getFeature("scenario_TP")).inject(rampUsers(30) during (10 minutes)).protocols(protocol),
    scenario("addCustomerToCart").exec(getFeature("scenario_TP_addCustomerToCart")).inject(rampUsers(30) during (10 minutes)).protocols(protocol),
    scenario("addToCart").exec(getFeature("scenario_TP_addToCart")).inject(rampUsers(30) during (10 minutes)).protocols(protocol),
    scenario("createArticle").exec(getFeature("scenario_TP_createArticle")).inject(rampUsers(30) during (10 minutes)).protocols(protocol) ,
    scenario("searchArticle").exec(getFeature("scenario_TP_searchArticle")).inject(rampUsers(400) during (10 minutes)).protocols(protocol)

  // 1) store reality
    //    200 orders a day
    //    200 / 36000 (200 per 10 hours)
    //    => 0.005 users/s
    // 2) this "soak test"
    //    300 users per 10 min
    //    => 0.5  users/s (* 100 in comparison to first one)

  )

  private def getFeature(name: String) = karateFeature(s"classpath:$directory/$name.feature")

}
