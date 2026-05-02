package com.example.sicenetmultiplatform.data.mapper
// cris

import com.example.sicenetmultiplatform.data.model.CalificacionFinal
import com.example.sicenetmultiplatform.data.model.CalificacionUnidad
import com.example.sicenetmultiplatform.utils.extraerContenidoXml
import com.example.sicenetmultiplatform.utils.getCurrentTimeMillis
import kotlinx.serialization.json.*

object CalificacionesXmlParser {

    data class Resultado(
        val finales: List<CalificacionFinal>,
        val unidades: List<CalificacionUnidad>
    )

    fun parse(unidadesXml: String, finalesXml: String): Resultado {
        val finales = mutableListOf<CalificacionFinal>()
        val unidades = mutableListOf<CalificacionUnidad>()

        println("[CALIF_PARSE] Iniciando parseo. Unidades: ${unidadesXml.length}, Finales: ${finalesXml.length}")

        parseFinales(finalesXml, finales)
        parseUnidades(unidadesXml, unidades)

        return Resultado(finales, unidades)
    }


    // Finales
    private fun parseFinales(xml: String, list: MutableList<CalificacionFinal>) {
        val jsonString = extraerContenidoXml(
            xml,
            "getAllCalifFinalByAlumnosResult",
            "getCalificacionesByAlumnoResult"
        )

        if (jsonString.isNullOrBlank()) {
            println("[CALIF_PARSE] No se encontró contenido de finales en el XML")
            return
        }

        val trimmed = jsonString.trim()
        if (trimmed == "[]" || trimmed == "{}") return

        println("[CALIF_PARSE] Contenido finales: ${trimmed.take(100)}...")

        try {
            val timestamp = getCurrentTimeMillis()
            val element = Json.parseToJsonElement(trimmed)

            when {
                // Es un array directo: [{ "Materia": ..., "Calif": ... }]
                element is JsonArray -> {
                    element.forEach { item ->
                        list.add(mapToFinal(item.jsonObject, timestamp))
                    }
                }
                // Es un objeto con lstCalif
                element is JsonObject -> {
                    val lstCalif = element["lstCalif"]
                    when {
                        lstCalif is JsonArray -> {
                            lstCalif.forEach { item ->
                                list.add(mapToFinal(item.jsonObject, timestamp))
                            }
                        }
                        lstCalif is JsonObject -> {
                            list.add(mapToFinal(lstCalif, timestamp))
                        }
                        // El objeto mismo tiene los datos
                        element.containsKey("Materia") || element.containsKey("Calif") -> {
                            list.add(mapToFinal(element, timestamp))
                        }
                        else -> println("[CALIF_PARSE] Formato de finales desconocido")
                    }
                }
            }
        } catch (e: Exception) {
            println("[CALIF_PARSE] Error parseando finales: ${e.message}")
        }
    }

    private fun mapToFinal(item: JsonObject, timestamp: Long): CalificacionFinal {
        val califStr = item["Calif"]?.jsonPrimitive?.contentOrNull ?: "0"
        val califInt = califStr.trim().replace(".0", "").toIntOrNull() ?: 0

        return CalificacionFinal(
            materia = item["Materia"]?.jsonPrimitive?.contentOrNull ?: "Desconocida",
            calificacionFinal = califInt,
            acreditado = item["Acred"]?.jsonPrimitive?.contentOrNull ?: "",
            ultimaActualizacion = timestamp
        )
    }

    // Unidades
    private fun parseUnidades(xml: String, list: MutableList<CalificacionUnidad>) {
        val jsonString = extraerContenidoXml(xml, "getCalifUnidadesByAlumnoResult")

        if (jsonString.isNullOrBlank()) {
            println("[CALIF_PARSE] No se encontró contenido de unidades en el XML")
            return
        }

        val trimmed = jsonString.trim()
        if (trimmed == "[]" || trimmed == "{}") return

        println("[CALIF_PARSE] Contenido unidades: ${trimmed.take(100)}...")

        try {
            val timestamp = getCurrentTimeMillis()
            val element = Json.parseToJsonElement(trimmed)

            when {
                element is JsonArray -> {
                    element.forEach { item ->
                        parseUnidadItem(item.jsonObject, list, timestamp)
                    }
                }
                element is JsonObject -> {
                    val lstCalif = element["lstCalif"]
                    when {
                        lstCalif is JsonArray -> {
                            lstCalif.forEach { item ->
                                parseUnidadItem(item.jsonObject, list, timestamp)
                            }
                        }
                        lstCalif is JsonObject -> {
                            parseUnidadItem(lstCalif, list, timestamp)
                        }
                        element.containsKey("Materia") || element.containsKey("C1") -> {
                            parseUnidadItem(element, list, timestamp)
                        }
                        else -> println("[CALIF_PARSE] Formato de unidades desconocido")
                    }
                }
            }
        } catch (e: Exception) {
            println("[CALIF_PARSE] Error parseando unidades: ${e.message}")
        }
    }

    private fun parseUnidadItem(
        item: JsonObject,
        list: MutableList<CalificacionUnidad>,
        timestamp: Long
    ) {
        val materia = item["Materia"]?.jsonPrimitive?.contentOrNull ?: "Desconocida"

        for (u in 1..13) {
            val key = "C$u"
            val califVal = item[key]?.jsonPrimitive?.contentOrNull

            if (!califVal.isNullOrBlank() && califVal != "null") {
                val califInt = califVal.trim().replace(".0", "").toIntOrNull() ?: continue
                list.add(
                    CalificacionUnidad(
                        materia = materia,
                        unidad = u,
                        calificacion = califInt,
                        ultimaActualizacion = timestamp
                    )
                )
            }
        }
    }
}
