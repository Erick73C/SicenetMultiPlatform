package com.example.sicenetmultiplatform.data.mapper

import android.util.Log
import com.example.sicenetmultiplatform.data.local.entity.CargaAcademicaEntity
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

object CargaAcademicaXmlParser {

    fun parse(
        xml: String,
        matricula: String,
        semestre: Int
    ): List<CargaAcademicaEntity> {

        val lista = mutableListOf<CargaAcademicaEntity>()

        val document = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .parse(InputSource(StringReader(xml)))

        val resultNode =
            document.getElementsByTagName("getCargaAcademicaByAlumnoResult")
                .item(0)

        val jsonString = resultNode?.textContent ?: return emptyList()

        if (jsonString.isBlank()) return emptyList()

        Log.d("JSON_DEBUG", jsonString)

        val jsonArray = org.json.JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {

            val materia = jsonArray.getJSONObject(i)

            val horario = listOf(
                materia.optString("Lunes"),
                materia.optString("Martes"),
                materia.optString("Miercoles"),
                materia.optString("Jueves"),
                materia.optString("Viernes"),
                materia.optString("Sabado")
            ).filter { it.isNotBlank() }
                .joinToString(" | ")

            lista.add(
                CargaAcademicaEntity(
                    matricula = matricula,
                    claveMateria = materia.optString("clvOficial"),
                    nombreMateria = materia.optString("Materia"),
                    grupo = materia.optString("Grupo"),
                    docente = materia.optString("Docente"),
                    creditos = materia.optInt("CreditosMateria"),
                    horario = horario,
                    semestre = semestre,
                    ultimaActualizacion = System.currentTimeMillis()
                )
            )
        }

        return lista
    }
}
