package ar.edu.unahur.obj2.servidorWeb

abstract class Analizador {
    val modulosYRespuestas = mutableMapOf <Modulo,MutableList<Respuesta>>()

    fun agregarModuloYRespuesta(modulo: Modulo,respuesta: Respuesta){
        if(!modulosYRespuestas.containsKey(modulo)){
            modulosYRespuestas.put(modulo, mutableListOf<Respuesta>())
        }
        modulosYRespuestas[modulo]?.add(respuesta)
    }

    fun cantidadDeRespuestasPorModulo(modulo: Modulo) = modulosYRespuestas[modulo]?.size

    fun cantidadDeRespuestaTotales(): Int{
        var total = 0
         total += modulosYRespuestas.keys.sumBy { this.cantidadDeRespuestasPorModulo(it)!! }
        return total
    }
}


class AnalizadorDemora(val demoraMinima: Int): Analizador(){
    fun cantidadDeRespuestasDemoradasPorModulo(modulo: Modulo) = modulosYRespuestas[modulo]?.count { it.superaElTiempo(demoraMinima) }
}

class AnalizadorIpSospechosa(val coleccionDeIpsSospechosas: List<String>): Analizador(){
    fun pedidosRealizadorPorLaIpSospechosaEnUnModulo(modulo: Modulo,ipSospechosa: String) =  modulosYRespuestas[modulo]?.count { it.laRespuestaFueGeneradaParaElPedidoConIp(ipSospechosa)}

    fun pedidosTotalesRealizadosPorUnaIpSospechosa(ipSospechosa: String): Int{
        var total = 0
        total += modulosYRespuestas.keys.sumBy {this.pedidosRealizadorPorLaIpSospechosaEnUnModulo(it,ipSospechosa) !!}
        return total
    }

    fun moduloMasConsultadoPorLasIpSospechosas() =  modulosYRespuestas.keys.maxByOrNull { this.funcionQueCuentaLaCantidadDeConsultasQueRealizaronLasIpSospechososEnUnModulo(it) }

    fun funcionQueCuentaLaCantidadDeConsultasQueRealizaronLasIpSospechososEnUnModulo(modulo: Modulo) : Int {
       var total = 0
       for (ip in coleccionDeIpsSospechosas){
           total += this.pedidosRealizadorPorLaIpSospechosaEnUnModulo(modulo,ip)!!
       }
       return total
    }

    fun conjuntoDeIpQueSolicitaronUnaRuta(ruta: String): Set<String> {
        var listaAux = listOf<String>()
        listaAux = this.respuestasQuetieneLaRutaPedida(ruta).map { it.pedido.ip }
        return coleccionDeIpsSospechosas.intersect(listaAux)
    }

    fun respuestasQuetieneLaRutaPedida(ruta: String): MutableList<Respuesta> {
        var respuestasTotalesConRuta = mutableListOf<Respuesta>()

        for(iter in modulosYRespuestas.keys){
            this.respuestasConRutaEnUnModulo(iter,ruta)?.let { respuestasTotalesConRuta.addAll(it) }
        }
        return respuestasTotalesConRuta
    }

    fun respuestasConRutaEnUnModulo(modulo: Modulo,ruta: String) = modulosYRespuestas[modulo]?.filter { it.pedido.rutaUrl() == ruta }
}

class AnalizadorEstadisticas: Analizador(){

    fun cantidadDeRespuestasExitosasPorModulo(modulo: Modulo) = modulosYRespuestas[modulo]?.count { it.esExitosa()}

    fun tiempoDeRespuestaPromedioPorModulo(modulo: Modulo) = modulosYRespuestas[modulo]?.sumBy { it.tiempo }?.div(modulosYRespuestas[modulo]?.size!!)

    fun cantidadDeRespuestasExitosas(): Int{
        var total = 0
        total += modulosYRespuestas.keys.sumBy{ this.cantidadDeRespuestasExitosasPorModulo(it)!! }
        return total
    }

    fun porcentajeDeRespuestasExitosas() = (this.cantidadDeRespuestasExitosas() * 100)/this.cantidadDeRespuestaTotales()

    fun tiempoDeRespuestaPromedio(): Int {
        var total = 0
        total += modulosYRespuestas.keys.sumBy{ this.tiempoDeRespuestaPromedioPorModulo(it)!! }
        total /= this.cantidadDeRespuestaTotales()
        return total
    }
}