package com.mls.kmp.mor.nytnewskmp.core.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient(
    engine: HttpClientEngine? = null
) {
    val client =
        if (engine != null) HttpClient(engine = engine)
        else HttpClient {
            defaultRequest {
                url(BASE_URL)
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
        }
}