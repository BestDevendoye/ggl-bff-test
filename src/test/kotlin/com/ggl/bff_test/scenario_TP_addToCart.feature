Feature:
  feature_TP_addToCart

  Background:
    * url baseUrl
    * def KeycloakConnect = Java.type('com.ggl.bff_test.test_api.manager.KeycloakConnect')
    * def requestBaseFile = 'request/' + env

  Scenario: scenario_TP_addToCart
    Then print 'addEntryToCart'
    Given def query = read(requestBaseFile + '/cart/add_entry/query.graphql')
    Given def vars = read(requestBaseFile + '/cart/add_entry/vars_scenario_TP_addEntryToCart_article.json')
    Given def expected = read('expected/cart/addEntryToCart.json')
    And header karate-name = 'addToCart'
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
    Then status 200
  //  * def cartUid = response.data.addEntryToCart.uid
  //  Then print 'cart uid : ', cartUid
   // * match response.data == '#present'
    //* match response.errors == '#notpresent'
