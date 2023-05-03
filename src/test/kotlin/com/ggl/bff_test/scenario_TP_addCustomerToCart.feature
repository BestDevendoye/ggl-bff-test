Feature:
  feature_TP_addCustomerToCart

  Background:
    * url baseUrl
    * def KeycloakConnect = Java.type('com.ggl.bff_test.test_api.manager.KeycloakConnect')
    * def requestBaseFile = 'request/' + env

  Scenario: scenario_TP_addCustomerToCart
    Then print 'addCustomerToCart'
    * def accessToken = new KeycloakConnect().login().accessToken
    Given def query = read(requestBaseFile + '/cart/add_customer_without_cart_uid/query.graphql')
    Given def vars = read(requestBaseFile + '/cart/add_customer_without_cart_uid/vars_scenario_TP_addCustomerToCart.json')
    And header karate-name = 'addCustomerToCart'
    And header Authorization = 'Bearer ' + accessToken
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
    Then status 200
    * match response.data == '#present'
    * match response.errors == '#notpresent'
