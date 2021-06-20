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

    // probar
    fun cantidadDeRespuestaTotales(): Int{
        var total = 0
        //recorro las claves y a cada clave( que son los modulos ) llamo a cantidadDeRespuestasPorModulo y sumo
        total += modulosYRespuestas.keys.sumBy { this.cantidadDeRespuestasPorModulo(it)!! }

        return total
    }

}


class AnalizadorDemora(val demoraMinima: Int): Analizador(){

    //probar
    fun cantidadDeRespuestasDemoradasPorModulo(modulo: Modulo) = modulosYRespuestas[modulo]?.count { it.superaElTiempo(demoraMinima) }
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

}