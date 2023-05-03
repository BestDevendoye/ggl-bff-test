Feature:
  feature_TP_addDeliveryToCart

  Background:
    * url baseUrl
    * def KeycloakConnect = Java.type('com.ggl.bff_test.test_api.manager.KeycloakConnect')
    * def requestBaseFile = 'request/' + env

  Scenario: scenario_TP_addDeliveryToCart
    Then print 'createArticle'
      * def accessToken = new KeycloakConnect().login().accessToken
      Given def query = read(requestBaseFile + '/product/create/query.graphql')
      Given def vars = read(requestBaseFile + '/product/create/vars_scenario_Delivery.json')
      And header karate-name = 'createArticle'
      And header Authorization = 'Bearer ' + accessToken
      And request { query: '#(query)', variables: '#(vars)' }
      When method post
      Then status 200
      * def uga = response.data.createPurchasedArticle.product.article.purchasedArticle.uga
      Then print 'uga : ', uga
      * match response.data == '#present'
      * match response.errors == '#notpresent'

      Then print 'addEntryToCart'
      Given def query = read(requestBaseFile + '/cart/add_entry/query.graphql')
      Given def vars = read(requestBaseFile + '/cart/add_entry/vars_scenario_TP_article.json')
      Given def expected = read('expected/cart/addEntryToCart.json')
      And header karate-name = 'addEntryToCart'
      And request { query: '#(query)', variables: '#(vars)' }
      When method post
      Then status 200
      * def cartUid = response.data.addEntryToCart.uid
      Then print 'cart uid : ', cartUid
      * match response.data == '#present'
      * match response.errors == '#notpresent'

      Then print 'addCustomerToCart'
      * def accessToken = new KeycloakConnect().login().accessToken
      Given def query = read(requestBaseFile + '/cart/add_customer/query.graphql')
      Given def vars = read(requestBaseFile + '/cart/add_customer/vars_scenario_TP.json')
      And header karate-name = 'addCustomerToCart'
      And header Authorization = 'Bearer ' + accessToken
      And request { query: '#(query)', variables: '#(vars)' }
      When method post
      Then status 200
      * def shippingGroupUid = response.data.addCustomerToCart.shippingGroups[0].uid
      Then print 'shipping group uid : ', shippingGroupUid
      * match response.data == '#present'
      * match response.errors == '#notpresent'

      Then print 'updateCartShippingGroup'
      * def accessToken = new KeycloakConnect().login().accessToken
      Given def query = read(requestBaseFile + '/cart/update_sg/query.graphql')
      Given def vars = read(requestBaseFile + '/cart/update_sg/vars_scenario_TP.json')
      And header karate-name = 'updateCartShippingGroup'
      And header Authorization = 'Bearer ' + accessToken
      And request { query: '#(query)', variables: '#(vars)' }
      When method post
      Then status 200
      * match response.data == '#present'
      * match response.errors == '#notpresent'
