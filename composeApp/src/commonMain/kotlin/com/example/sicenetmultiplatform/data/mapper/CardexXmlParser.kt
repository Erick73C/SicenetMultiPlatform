package com.example.sicenetmultiplatform.data.mapper

import com.example.sicenetmultiplatform.data.model.Cardex
import com.example.sicenetmultiplatform.utils.extraerContenidoXml
import com.example.sicenetmultiplatform.utils.getCurrentTimeMillis
import kotlinx.serialization.json.*

object CardexXmlParser {

    fun parse(xml: String): List<Cardex> {

        val lista = mutableListOf<Cardex>()

        // Extrae el contenido entre las etiquetas del resultado
        val jsonString = extraerContenidoXml(xml, "getAllKardexConPromedioByAlumnoResult")

        if (jsonString.isNullOrBlank()) {
            println("[CARDEX_PARSE] No se encontró contenido en el XML")
            return emptyList()
        }

        // Decodifica entidades HTML que vienen en el XML
        val contenido = jsonString
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .trim()

        if (contenido.isBlank()) return emptyList()

        println("[CARDEX_PARSE] Contenido extraído: ${contenido.take(100)}...")

        try {
            val timestamp = getCurrentTimeMillis()
            val elemento = Json.parseToJsonElement(contenido)

            // El JSON viene como objeto con una lista lstKardex adentro
            // Ejemplo: { "lstKardex": [ {...}, {...} ] }
            if (elemento is JsonObject) {
                val lstKardex = elemento["lstKardex"]

                when {
                    lstKardex is JsonArray -> {
                        lstKardex.forEach { item ->
                            lista.add(mapToCardex(item.jsonObject, timestamp))
                        }
                    }
                    lstKardex is JsonObject -> {
                        // Caso raro: solo hay una materia y viene como objeto
                        lista.add(mapToCardex(lstKardex, timestamp))
                    }
                    else -> {
                        println("[CARDEX_PARSE] No se encontró lstKardex en el JSON")
                    }
                }
            }

        } catch (e: Exception) {
            println("[CARDEX_PARSE] Error parseando JSON: ${e.message}")
        }

        return lista
    }

    private fun mapToCardex(item: JsonObject, timestamp: Long): Cardex {

        val periodo1 = item["P1"]?.jsonPrimitive?.contentOrNull ?: ""
        val anio1    = item["A1"]?.jsonPrimitive?.contentOrNull ?: ""

        return Cardex(
            materia       = item["Materia"]?.jsonPrimitive?.contentOrNull ?: "Desconocida",
            calificacion  = item["Calif"]?.jsonPrimitive?.intOrNull ?: 0,
            acreditado    = item["Acred"]?.jsonPrimitive?.contentOrNull ?: "",
            periodo       = "$periodo1 $anio1".trim(),
            ultimaActualizacion = timestamp
        )
    }
}