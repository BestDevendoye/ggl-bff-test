Feature:
  feature_search_article

  Background:
    * url baseUrl
    * def KeycloakConnect = Java.type('com.ggl.bff_test.test_api.manager.KeycloakConnect')
    * def requestBaseFile = 'request/' + env

  Scenario: scenario_search_article
    Then print 'addEntryToCart'
    Given def query = read(requestBaseFile + '/product/search/query.graphql')
    Given def vars = read(requestBaseFile + '/product/search/vars_search_article.json')
    And request { query: '#(query)', variables: '#(vars)' }
    When method post
    Then status 200
    * match response.data == '#present'
    * match response.errors == '#notpresent'


