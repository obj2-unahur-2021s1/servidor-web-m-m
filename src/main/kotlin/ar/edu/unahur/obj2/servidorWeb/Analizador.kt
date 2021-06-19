package ar.edu.unahur.obj2.servidorWeb
//preguntar bien mañana para cerra idea
abstract class Analizador {

    val modulosYRespuestas = mutableMapOf <Modulo,MutableList<Respuesta>>()

    //no estoy seguro de esto
    abstract fun recibirRespuesta(respuesta: Respuesta)

    fun agregarModuloYRespuesta(modulo: Modulo,respuesta: Respuesta){
        if(!modulosYRespuestas.containsKey(modulo)){
            modulosYRespuestas.put(modulo, mutableListOf<Respuesta>())
        }
        modulosYRespuestas[modulo]?.add(respuesta)
    }
}

//preguntar porq dice el modulo
class AnalizadorDemora(val demoraMinima: Int): Analizador(){
    override fun recibirRespuesta(respuesta: Respuesta) {
        TODO("Not yet implemented")
    }

}

class AnalizadorIpSospechosa: Analizador(){
    override fun recibirRespuesta(respuesta: Respuesta) {
        TODO("Not yet implemented")
    }
}

class AnalizadorEstadisticas: Analizador(){
    override fun recibirRespuesta(respuesta: Respuesta) {
        TODO("Not yet implemented")
    }
    //probar
    //fun cantidadDeRespuestasExitosas() = modulosYRespuestas.filterValues { it.esExitosa() }.size
    //probar
    //fun porcentajeDeRespuestasExitosas() = (this.cantidadDeRespuestasExitosas() * 100)/modulosYRespuestas.size
    //probar
    //fun tiempoDeRespuestaPromedio() = respuestasRealizadas.sumBy { it.tiempo } / respuestasRealizadas.size

    //fun totalDeRespuestas() =

}