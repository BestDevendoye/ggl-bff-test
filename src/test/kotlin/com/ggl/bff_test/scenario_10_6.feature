Feature:
  feature_10_6
  add entry to cart, and add customer to cart, and add delivery address to cart and finally create order

  Background:
    * url baseUrl
    * def KeycloakConnect = Java.type('com.ggl.bff_test.test_api.manager.KeycloakConnect')
    * def requestBaseFile = 'request/' + env
    * def tenant = "bhv"
    * def subtenant = "7510"

  Scenario: scenario_10_6
    Then print 'addEntryToCart'
    Given def query = read(requestBaseFile + '/cart/add_entry/query.graphql')
    Given def vars = read(requestBaseFile + '/cart/add_entry/vars_scenario_10_6_article.json')
    Given def expected = read('expected/cart/addEntryToCart.json')
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
    Then status 200
    * def cartUid = response.data.addEntryToCart.uid
    Then print 'cart uid : ', cartUid

    Then print 'addCustomerToCart'
    * def accessToken = new KeycloakConnect().login().accessToken
    Given def query = read(requestBaseFile + '/cart/add_customer/query.graphql')
    Given def vars = read(requestBaseFile + '/cart/add_customer/vars_scenario_10_6.json')
    And header Authorization = 'Bearer ' + accessToken
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
    Then status 200
    * def shippingGroupUid = response.data.addCustomerToCart.shippingGroups[0].uid
    Then print 'shipping group uid : ', shippingGroupUid

    Then print 'addDeliveryToCart'
    * def accessToken = new KeycloakConnect().login().accessToken
    Given def query = read(requestBaseFile + '/cart/update_sg/query.graphql')
    Given def vars = read(requestBaseFile + '/cart/update_sg/vars_scenario_10_6.json')
    And header Authorization = 'Bearer ' + accessToken
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
    Then status 200

    Then print 'createOrder'
    * def accessToken = new KeycloakConnect().login().accessToken
    Given def query = read(requestBaseFile + '/order/create/query.graphql')
    Given def vars = read(requestBaseFile + '/order/create/vars_scenario_10_6.json')
    And header Authorization = 'Bearer ' + accessToken
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
