package com.example.sicenetmultiplatform.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

/**
 * Proveedor del cliente HTTP configurado para comunicarse con el
 * WebService SOAP de SICENET. Reemplaza OkHttpClient + Interceptors
 * del proyecto Android original.
 *
 * Configuraciones incluidas:
 * - Cookies: manejo automático de sesión (reemplaza AddCookiesInterceptor y ReceivedCookiesInterceptor)
 * - Timeouts: 60 segundos igual que el proyecto original
 * - Logging: muestra las peticiones en consola
 *
 * @author Erick Omar Pérez González
 */
object KtorClientProvider {

    val client: HttpClient by lazy {
        HttpClient {

            // Manejo automático de cookies de sesión
            // Reemplaza AddCookiesInterceptor + ReceivedCookiesInterceptor
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }

            // Timeouts igual que el proyecto Android original
            install(HttpTimeout) {
                connectTimeoutMillis = 60_000
                requestTimeoutMillis = 60_000
                socketTimeoutMillis  = 60_000
            }

            // Logging para debug
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("[KTOR] $message")
                    }
                }
                level = LogLevel.BODY
            }
        }
    }
}