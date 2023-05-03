Feature:
  feature_TP_createArticle

  Background:
    * url baseUrl
    * def KeycloakConnect = Java.type('com.ggl.bff_test.test_api.manager.KeycloakConnect')
    * def requestBaseFile = 'request/' + env

  Scenario: scenario_TP_createArticle
    Then print 'createArticle'
    * def accessToken = new KeycloakConnect().login().accessToken
    Given def query = read(requestBaseFile + '/product/create/query.graphql')
    Given def vars = read(requestBaseFile + '/product/create/vars_scenario_CreateSeul.json')
    And header karate-name = 'createArticle'
    And header Authorization = 'Bearer ' + accessToken
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
    Then status 200
    * match response.data == '#present'
    * match response.errors == '#notpresent'
