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
    fun moduloMasConsultadoPorLasIpSospechosas() =  modulosYRespuestas.keys.maxByOrNull { this.funcionQueCuentaLaCantidadDeConsultasQueRealizaronLasIpSospechososEnUnModulo(it) }

    fun funcionQueCuentaLaCantidadDeConsultasQueRealizaronLasIpSospechososEnUnModulo(modulo: Modulo) : Int{

       var total = 0
       //reever esta parte
       for (ip in coleccionDeIpsSospechosas){
           total += this.pedidosRealizadorPorLaIpSospechosaEnUnModulo(modulo,ip)!!
       }
       return total
   }
    //probar
    fun conjuntoDeIpQueSolicitaronUnaRuta(ruta: String): Set<String> {
        var listaAux = listOf<String>()
        listaAux = this.respuestasQuetieneLaRutaPedida(ruta).map { it.pedido.ip }
        return coleccionDeIpsSospechosas.intersect(listaAux)
    }

    //la idea aca es ir agregando cada lista filter de respuestasConRutaEnUnModulo a la lista auxiliar
    fun respuestasQuetieneLaRutaPedida(ruta: String): MutableList<Respuesta> {
        var respuestasTotalesConRuta = mutableListOf<Respuesta>()
        //respuestasTotalesConRuta.addAll{ modulosYRespuestas.keys.forEach { this.respuestasConRutaEnUnModulo(it,ruta) }}

        //para cada elemento de las claves de respuestas(osea los modulos) las traigo y las agrego a respuestasTotalesConRuta
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