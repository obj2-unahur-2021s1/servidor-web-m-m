package ar.edu.unahur.obj2.servidorWeb
//preguntar bien mañana para cerra idea
abstract class Analizador {

    val modulosYRespuestas = mutableMapOf <Modulo,MutableList<Respuesta>>()

    fun agregarModuloYRespuesta(modulo: Modulo,respuesta: Respuesta){
        if(!modulosYRespuestas.containsKey(modulo)){
            modulosYRespuestas.put(modulo, mutableListOf<Respuesta>())
        }
        modulosYRespuestas[modulo]?.add(respuesta)
    }

    fun cantidadDeRespuestasPorModulo(modulo: Modulo) = modulosYRespuestas[modulo]?.size
}

//preguntar porq dice el modulo
class AnalizadorDemora(val demoraMinima: Int): Analizador(){

}

class AnalizadorIpSospechosa: Analizador(){

}

class AnalizadorEstadisticas: Analizador(){

//probar
    //fun cantidadDeRespuestasExitosas() = modulosYRespuestas.filterValues { it.esExitosa() }.size
    //probar
    //fun porcentajeDeRespuestasExitosas() = (this.cantidadDeRespuestasExitosas() * 100)/modulosYRespuestas.size
    //probar
    //fun tiempoDeRespuestaPromedio() = respuestasRealizadas.sumBy { it.tiempo } / respuestasRealizadas.size

    //fun totalDeRespuestas() =

}