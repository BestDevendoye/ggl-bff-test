package com.ggl.bff_test.gatling

import com.ggl.bff_test.BffApplicationTest
import com.intuit.karate.gatling.PreDef._
import io.gatling.core.Predef._
import sys.process._
import scala.concurrent.duration._

class SequentielSimulation extends Simulation {

  private val  tpsMonte: Int = System.getProperty("tpsMonteMinutes", "1").toInt
  private val  nbVU: Int = System.getProperty("nbVU", "15").toInt
  private val  tpsPause: Int = System.getProperty("tpsPauseSeconde", "20").toInt

  private val directory = classOf[BffApplicationTest]
    .getPackageName
    .replace('.', '/')

  private val protocol = karateProtocol()
  protocol.nameResolver = (req, _) => req.getHeader("karate-name")

  private val searchArticle = scenario("Les_searchArticle").exec(getFeature("scenario_TP_searchArticle"))
  private val addToCart = scenario("Les_addToCart").exec(getFeature("scenario_TP_addToCart"))
  private val addCustomerToCart = scenario("Les_addCustomerToCart").exec(getFeature("scenario_TP_addCustomerToCart"))
  private val createArticle = scenario("Les_createArticle").exec(getFeature("scenario_TP_createArticle"))
  private val addEntryToCart = scenario("Les_addEntryToCart").exec(getFeature("scenario_TP_addEntryToCart"))
  private val addDeliveryToCart = scenario("Les_addDeliveryToCart").exec(getFeature("scenario_TP_addDeliveryToCart"))
  private val all = scenario("Les_CreateOrder").exec(getFeature("scenario_TP"))

  private val searchArticleTir = scenario("lesSearchArticleTir").exec(getFeature("scenario_TP_searchArticle"))
  private val addToCartTir = scenario("lesAddToCartTir").exec(getFeature("scenario_TP_addToCart"))
  private val createArticleTir = scenario("lesCreateArticleTir").exec(getFeature("scenario_TP_createArticle"))
  private val allTir = scenario("lesCreateOrderTir").exec(getFeature("scenario_TP"))



  setUp(
    searchArticle.inject(rampUsers(nbVU ) during ( tpsMonte minutes)).protocols(protocol),
    addToCart.inject(nothingFor( (tpsMonte *60 + tpsPause) seconds)  , rampUsers(nbVU) during ( tpsMonte minutes)).protocols(protocol),
    addCustomerToCart.inject(nothingFor( 2*(tpsMonte *60 + tpsPause) seconds) , rampUsers(nbVU) during ( tpsMonte minutes)).protocols(protocol) ,
    createArticle.inject(nothingFor( 3*(tpsMonte*60 + tpsPause) seconds) , rampUsers(nbVU) during ( tpsMonte minutes)).protocols(protocol) ,
    addEntryToCart.inject(nothingFor( 4*(tpsMonte*60 + tpsPause) seconds) , rampUsers(nbVU) during ( tpsMonte minutes)).protocols(protocol),
    addDeliveryToCart.inject(nothingFor( 5*(tpsMonte*60 + tpsPause) seconds) , rampUsers(nbVU) during ( tpsMonte minutes)).protocols(protocol),
    all.inject(nothingFor( 6*(tpsMonte*60 + tpsPause) seconds) , rampUsers(nbVU) during ( tpsMonte minutes)).protocols(protocol),
    // maintenant tout :
    searchArticleTir.inject(nothingFor( 8*(tpsMonte*60 + tpsPause) seconds) , rampUsers(nbVU*25) during ( tpsMonte * 10 minutes)).protocols(protocol),
    addToCartTir.inject(nothingFor( 8*(tpsMonte *60 + tpsPause) seconds)  , rampUsers(nbVU *25 ) during ( tpsMonte * 10 minutes)).protocols(protocol),
    createArticleTir.inject(nothingFor( 8*(tpsMonte*60 + tpsPause) seconds) , rampUsers(nbVU*25) during ( tpsMonte * 10  minutes)).protocols(protocol) ,
    allTir.inject(nothingFor( 8*(tpsMonte*60 + tpsPause) seconds) , rampUsers(nbVU*5) during ( tpsMonte * 10 minutes)).protocols(protocol)
    //  TP.inject(nothingFor( 1*(tpsMonte*60 + tpsPause) seconds) , rampUsers(nbVU) during ( tpsMonte minutes)).protocols(protocol)

  )



  private def getFeature(name: String) = karateFeature(s"classpath:$directory/$name.feature")

}
