package com.ggl.bff_test.test_api.manager.mapper

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

class MapperJWT {

    fun getMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(KotlinModule())
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)

    }
}
