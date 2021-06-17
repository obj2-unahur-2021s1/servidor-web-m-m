package ar.edu.unahur.obj2.servidorWeb
//preguntar bien mañana para cerra idea
abstract class Analizador {

    //val modulosYRespuestas = mutableMapOf<Modulo,mutableListOf<Respuesta>()>()
    val modulosYRespuestas = mutableMapOf<Modulo,Respuesta>()

    //no estoy seguro de esto
    abstract fun recibirRespuesta(respuesta: Respuesta)

    fun agregarModuloYRespuesta(modulo: Modulo,respuesta: Respuesta) = modulosYRespuestas.put(modulo,respuesta)
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

}