package com.example.sicenetmultiplatform.data.network

/**
 * Construye los cuerpos XML para las peticiones SOAP al WebService de SICENET.
 * Cada función genera el envelope SOAP correspondiente a una operación del servicio.
 *
 * @author Erick Omar Pérez González
 */
object SoapRequestBuilder {

    /**
     * Genera el XML para autenticar al alumno en SICENET.
     *
     * @param matricula Matrícula del alumno
     * @param password Contraseña del alumno
     * @return String con el envelope SOAP de login
     */
    fun login(matricula: String, password: String): String {
        return """
            <soap:Envelope 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <accesoLogin xmlns="http://tempuri.org/">
                  <strMatricula>$matricula</strMatricula>
                  <strContrasenia>$password</strContrasenia>
                  <tipoUsuario>ALUMNO</tipoUsuario>
                </accesoLogin>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
    }

    /**
     * Genera el XML para obtener el perfil académico del alumno.
     *
     * @return String con el envelope SOAP de perfil
     */
    fun perfil(): String {
        return """
            <soap:Envelope 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <getAlumnoAcademico xmlns="http://tempuri.org/" />
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
    }

    /**
     * Genera el XML para obtener la carga académica del alumno.
     *
     * @return String con el envelope SOAP de carga académica
     */
    fun cargaAcademica(): String {
        return """
            <soap:Envelope 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <getCargaAcademicaByAlumno xmlns="http://tempuri.org/" />
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
    }

    /**
     * Genera el XML para obtener el kardex completo del alumno.
     *
     * @param aluLineamiento Lineamiento del alumno (generalmente 1)
     * @return String con el envelope SOAP del kardex
     */
    fun cardex(aluLineamiento: Int = 1): String {
        return """
            <soap:Envelope 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <getAllKardexConPromedioByAlumno xmlns="http://tempuri.org/">
                  <aluLineamiento>$aluLineamiento</aluLineamiento>
                </getAllKardexConPromedioByAlumno>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
    }

    /**
     * Genera el XML para obtener las calificaciones por unidad del alumno.
     *
     * @return String con el envelope SOAP de calificaciones por unidad
     */
    fun calificacionesUnidades(): String {
        return """
            <soap:Envelope 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <getCalifUnidadesByAlumno xmlns="http://tempuri.org/" />
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
    }

    /**
     * Genera el XML para obtener las calificaciones finales del alumno.
     *
     * @param modEducativo Modalidad educativa (0 o 1 según el alumno)
     * @return String con el envelope SOAP de calificaciones finales
     */
    fun calificacionesFinales(modEducativo: Int): String {
        return """
            <soap:Envelope 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <getAllCalifFinalByAlumnos xmlns="http://tempuri.org/">
                  <bytModEducativo>$modEducativo</bytModEducativo>
                </getAllCalifFinalByAlumnos>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()
    }
}