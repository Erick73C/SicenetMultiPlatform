package com.example.sicenetmultiplatform.data.mapper

import android.util.Log
import com.example.sicenetmultiplatform.data.local.entity.CardexEntity
import org.json.JSONArray
import org.json.JSONObject

object CardexXmlParser {

    fun parse(xml: String): List<CardexEntity> {

        val lista = mutableListOf<CardexEntity>()

        val regex = Regex(
            "<getAllKardexConPromedioByAlumnoResult>(.*?)</getAllKardexConPromedioByAlumnoResult>",
            RegexOption.DOT_MATCHES_ALL
        )

        val match = regex.find(xml) ?: return emptyList()

        var contenido = match.groupValues[1]
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .trim()

        if (contenido.isBlank()) return emptyList()

        try {

            val jsonObject = JSONObject(contenido)
            val jsonArray = jsonObject.getJSONArray("lstKardex")

            for (i in 0 until jsonArray.length()) {

                val item = jsonArray.getJSONObject(i)

                lista.add(
                    CardexEntity(
                        materia = item.optString("Materia"),
                        calificacion = item.optInt("Calif"),
                        acreditado = item.optString("Acred"),
                        periodo = "${item.optString("P1")} ${item.optString("A1")}",
                        ultimaActualizacion = System.currentTimeMillis()
                    )
                )

            }

        } catch (e: Exception) {
            Log.e("CARDEX_PARSE_ERROR", "Error parseando JSON", e)
        }

        return lista
    }
}

