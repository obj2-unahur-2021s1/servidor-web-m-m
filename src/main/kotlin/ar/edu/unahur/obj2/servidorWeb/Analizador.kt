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

class AnalizadorIpSospechosa(val coleccionDeIpsSospechosas: List<String>): Analizador(){

    fun pedidosRealizadorPorLaIpSospechosaEnUnModulo(modulo: Modulo,ipSospechosa: String) =  modulosYRespuestas[modulo]?.count { it.laRespuestaFueGeneradaParaElPedidoConIp(ipSospechosa)}

    //probar
    fun pedidosTotalesRealizadosPorUnaIpSospechosa(ipSospechosa: String): Int{
        var total = 0
        //recorro las llaves con un for y llamo al this y paso como parametro cada modulo
        total += modulosYRespuestas.keys.sumBy {this.pedidosRealizadorPorLaIpSospechosaEnUnModulo(it,ipSospechosa) !!}
        return total
    }
    //probar
    //fun moduloMasConsultadoPorLasIpSospechosas() =  modulosYRespuestas.keys.max{

    //fun conjuntoDeIpQueSolicitaronUnaRuta(ruta: String)
}

class AnalizadorEstadisticas: Analizador(){


    fun cantidadDeRespuestasExitosasPorModulo(modulo: Modulo) = modulosYRespuestas[modulo]?.count { it.esExitosa()}

    fun tiempoDeRespuestaPromedioPorModulo(modulo: Modulo) = modulosYRespuestas[modulo]?.sumBy { it.tiempo }?.div(modulosYRespuestas[modulo]?.size!!)
    //probar
    fun cantidadDeRespuestasExitosas(): Int{
        var total = 0
        total += modulosYRespuestas.keys.sumBy{ this.cantidadDeRespuestasExitosasPorModulo(it)!! }
        return total
    }
    //probar
    fun porcentajeDeRespuestasExitosas() = (this.cantidadDeRespuestasExitosas() * 100)/this.cantidadDeRespuestaTotales()

    //probar y refact
    fun tiempoDeRespuestaPromedio(): Int {
        var total = 0
        //recorro las claves y a cada clave( que son los modulos )
        total += modulosYRespuestas.keys.sumBy{ this.tiempoDeRespuestaPromedioPorModulo(it)!! }
        total /= this.cantidadDeRespuestaTotales()
        return total
    }

}