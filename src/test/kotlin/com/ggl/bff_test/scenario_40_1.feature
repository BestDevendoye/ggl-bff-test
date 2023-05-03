Feature:
  feature_40_1

  Background:
    * url baseUrl
    * def KeycloakConnect = Java.type('com.ggl.bff_test.test_api.manager.KeycloakConnect')
    * def requestBaseFile = 'request/' + env

  Scenario: scenario_40_1
    Then print 'createArticle'
    * def accessToken = new KeycloakConnect().login().accessToken
    Given def query = read(requestBaseFile + '/product/create/query.graphql')
    Given def vars = read(requestBaseFile + '/product/create/vars_scenario_40_1.json')
    And header Authorization = 'Bearer ' + accessToken
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
    Then status 200
