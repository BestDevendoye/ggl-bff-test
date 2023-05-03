Feature:
  feature_TP_searchArticle

  Background:
    * url baseUrl
    * def KeycloakConnect = Java.type('com.ggl.bff_test.test_api.manager.KeycloakConnect')
    * def requestBaseFile = 'request/' + env

  Scenario: scenario_TP_searchArticle
    Then print 'search_article'
    Given def query = read(requestBaseFile + '/product/search/query.graphql')
    Given def vars = read(requestBaseFile + '/product/search/vars_search_article.json')
    And request { query: '#(query)', variables: '#(vars)' }
    And header karate-name = 'search_article'
    When method post
    Then status 200
    * match response.data == '#present'
    * match response.errors == '#notpresent'


