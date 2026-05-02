package com.example.sicenetmultiplatform.data.mapper
// cris

import com.example.sicenetmultiplatform.data.local.entity.CalificacionFinalEntity
import com.example.sicenetmultiplatform.data.local.entity.CalificacionUnidadEntity
import jdk.internal.net.http.common.Log
import org.json.JSONObject
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

object CalificacionesXmlParser {
    data class Resultado(
        val finales: List<CalificacionFinalEntity>,
        val unidades: List<CalificacionUnidadEntity>
    )

    fun parse(unidadesXml: String, finalesXml: String): Resultado {
        val finales = mutableListOf<CalificacionFinalEntity>()
        val unidades = mutableListOf<CalificacionUnidadEntity>()

        Log.d("CALIF_PARSE", "Iniciando parseo de XMLs. Unidades: ${unidadesXml.length}, Finales: ${finalesXml.length}")

        // 1. Parsear Finales
        parseFinales(finalesXml, finales)

        // 2. Parsear Unidades
        parseUnidades(unidadesXml, unidades)

        return Resultado(finales, unidades)
    }

    private fun parseFinales(xml: String, list: MutableList<CalificacionFinalEntity>) {
        val jsonString = try {
            val document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(InputSource(StringReader(xml)))

            val resultNode = 
                document.getElementsByTagName("getAllCalifFinalByAlumnosResult").item(0)
                ?: document.getElementsByTagName("getCalificacionesByAlumnoResult").item(0)

            resultNode?.textContent
        } catch (e: Exception) {
            Log.e("CALIF_PARSE", "Error extrayendo finales con DOM", e)
            null
        }

        if (jsonString == null) {
            Log.e("CALIF_PARSE", "No se encontró el tag de resultados finales en el XML o error de parsing")
            return
        }

        Log.d("CALIF_PARSE", "Contenido extraído (finales): ${jsonString.take(100)}...")
        val trimmed = jsonString.trim()
        if (trimmed.isBlank() || trimmed == "[]" || trimmed == "{}") return

        try {
            val timestamp = System.currentTimeMillis()
            
            if (trimmed.startsWith("[")) {
                // Es un Array directo
                val jsonArray = org.json.JSONArray(trimmed)
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    list.add(mapToFinalEntity(item, timestamp))
                }
            } else {
                // Es un Objeto que podría tener lstCalif
                val jsonObject = JSONObject(trimmed)
                val lstCalifObj = jsonObject.opt("lstCalif")
                
                if (lstCalifObj is org.json.JSONArray) {
                    for (i in 0 until lstCalifObj.length()) {
                        val item = lstCalifObj.getJSONObject(i)
                        list.add(mapToFinalEntity(item, timestamp))
                    }
                } else if (lstCalifObj is JSONObject) {
                    list.add(mapToFinalEntity(lstCalifObj, timestamp))
                } else {
                    // Si no tiene lstCalif, tal vez el objeto mismo es la calificación (poco probable pero posible)
                    // O intentar parsear campos directos si no hay wrapper
                    if (jsonObject.has("Materia") || jsonObject.has("Calif")) {
                         list.add(mapToFinalEntity(jsonObject, timestamp))
                    } else {
                        Log.w("CALIF_PARSE", "Formato de JSON de finales desconocido: $trimmed")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("CALIF_PARSE", "Error parseando JSON de finales", e)
        }
    }

    private fun mapToFinalEntity(item: JSONObject, timestamp: Long): CalificacionFinalEntity {
        val califStr = item.optString("Calif", "0")
        val califInt = try {
            califStr.trim().replace(".0", "").toInt()
        } catch (e: Exception) {
            0
        }

        return CalificacionFinalEntity(
            materia = item.optString("Materia", "Desconocida"),
            calificacionFinal = califInt,
            acreditado = item.optString("Acred", ""),
            ultimaActualizacion = timestamp
        )
    }

    private fun parseUnidades(xml: String, list: MutableList<CalificacionUnidadEntity>) {
        val jsonString = try {
            val document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(InputSource(StringReader(xml)))

            val resultNode = 
                document.getElementsByTagName("getCalifUnidadesByAlumnoResult").item(0)

            resultNode?.textContent
        } catch (e: Exception) {
            Log.e("CALIF_PARSE", "Error extrayendo unidades con DOM", e)
            null
        }

        if (jsonString == null) {
            Log.e("CALIF_PARSE", "No se encontró el tag de resultados unidades en el XML")
            return
        }

        Log.d("CALIF_PARSE", "Contenido extraído (unidades): ${jsonString.take(100)}...")
        val trimmed = jsonString.trim()
        if (trimmed.isBlank() || trimmed == "[]" || trimmed == "{}") return

        try {
            val timestamp = System.currentTimeMillis()
            
            if (trimmed.startsWith("[")) {
                val jsonArray = org.json.JSONArray(trimmed)
                for (i in 0 until jsonArray.length()) {
                    parseUnidadItem(jsonArray.getJSONObject(i), list, timestamp)
                }
            } else {
                val jsonObject = JSONObject(trimmed)
                val lstCalifObj = jsonObject.opt("lstCalif")
                
                if (lstCalifObj is org.json.JSONArray) {
                    for (i in 0 until lstCalifObj.length()) {
                        parseUnidadItem(lstCalifObj.getJSONObject(i), list, timestamp)
                    }
                } else if (lstCalifObj is JSONObject) {
                    parseUnidadItem(lstCalifObj, list, timestamp)
                } else {
                    if (jsonObject.has("Materia") || jsonObject.has("C1")) {
                        parseUnidadItem(jsonObject, list, timestamp)
                    } else {
                        Log.w("CALIF_PARSE", "Formato de JSON de unidades desconocido: $trimmed")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("CALIF_PARSE", "Error parseando JSON de unidades", e)
        }
    }

    private fun parseUnidadItem(item: JSONObject, list: MutableList<CalificacionUnidadEntity>, timestamp: Long) {
        val materia = item.optString("Materia", "Desconocida")
        for (u in 1..13) {
            val key = "C$u"
            if (item.has(key)) {
                val califVal = item.optString(key, "")
                if (califVal.isNotBlank() && califVal != "null") {
                    val califInt = try { 
                        califVal.trim().replace(".0", "").toInt() 
                    } catch (e: Exception) { 
                        -1 
                    }
                    if (califInt != -1) {
                        list.add(
                            CalificacionUnidadEntity(
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
    }
}
