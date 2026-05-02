package com.example.sicenetmultiplatform.utils

/**
 * Extrae el contenido de texto ubicado entre etiquetas XML.
 * Recorre los tags proporcionados en orden y devuelve el contenido
 * del primero que encuentre en el XML. Si ningún tag es encontrado,
 * devuelve null. Es útil para manejar respuestas SOAP donde el tag
 * del resultado puede variar según el servicio o versión del servidor.
 *
 * Ejemplo de uso:
 * ```
 * val xml = "<getCardexResult>{ datos }</getCardexResult>"
 * val contenido = extraerContenidoXml(xml, "getCardexResult") // "{ datos }"
 * ```
 *
 * @param xml String con el XML completo de la respuesta SOAP
 * @param tags Uno o más nombres de etiquetas XML a buscar, se recorren en orden
 *
 * @return String? con el contenido entre las etiquetas encontradas,
 * o null si ningún tag existe en el XML
 *
 * @author Erick Omar Pérez González
 */
fun extraerContenidoXml(xml: String, vararg tags: String): String? {
    for (tag in tags) {
        val regex = Regex("<$tag>(.*?)</$tag>", RegexOption.DOT_MATCHES_ALL)
        val match = regex.find(xml)
        if (match != null) return match.groupValues[1].trim()
    }
    return null
}