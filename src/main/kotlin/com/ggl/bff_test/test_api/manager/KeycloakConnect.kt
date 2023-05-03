package com.ggl.bff_test.test_api.manager

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ggl.bff_test.test_api.manager.mapper.MapperJWT
import com.ggl.bff_test.test_api.manager.mapper.MapperOIDC
import com.ggl.bff_test.test_api.manager.model.AuthConnection
import com.ggl.bff_test.test_api.manager.model.AuthData
import com.ggl.bff_test.test_api.manager.model.JwtDecodedToken
import com.ggl.bff_test.test_api.manager.model.OpenidConnectResponse
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import java.util.stream.Collectors

class KeycloakConnect {

    private companion object {
        private const val KARATE_ENV = "karate.env"
        private const val DEFAULT_ENV = "pp"
    }

    /**
     * Use a Keycloak client to do "Direct Grant Access" with known username/password - to get a valid
     * access_token - to decode token in order to get id of the customer
     */
    @Throws(IOException::class, InterruptedException::class, JWTDecodeException::class)
    fun login(): AuthData {
        // get auth connection data
        val env = System.getProperty(KARATE_ENV, DEFAULT_ENV)
        val authConnection = ObjectMapper()
            .registerModule(KotlinModule())
            .readValue(
                File("src/main/resources/auth/$env.json"), AuthConnection::class.java
            )

        // build data to send
        val parameters =
            with(authConnection){
                java.util.Map.of(
                    "grant_type", grantType,
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "username", username,
                    "password", password
                )
            }

        val form = parameters.keys.stream()
            .map { "$it=" + URLEncoder.encode(parameters[it],UTF_8) }
            .collect(Collectors.joining("&"))

        // do call to get access_token
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(authConnection.authServerUrl))
            .headers("content-type", "application/x-www-form-urlencoded")
            .POST(BodyPublishers.ofString(form))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        // extract access token
        val openidConnectResponse =
            MapperOIDC().getMapper().readValue(response.body(), OpenidConnectResponse::class.java)

        // extract customer id
        val jwt = JWT.decode(openidConnectResponse.accessToken)
        val jwtDecoded: String = String(Base64.getDecoder().decode(jwt.payload))
        val jwtDecodedToken = MapperJWT().getMapper().readValue(jwtDecoded, JwtDecodedToken::class.java)

        return AuthData(openidConnectResponse.accessToken, jwtDecodedToken.customerId)
    }
}
