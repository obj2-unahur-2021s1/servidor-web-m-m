package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime) {
  fun protocoloUrl() = url.split(":").first()
  fun rutaUrl() = "/" + url.split("""\.[a-z]*/""".toRegex()).last()
  fun extensionUrl() = url.split(".").last()

  fun fuePedidoPorLaIp(supuestaIp: String) = ip == supuestaIp
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){
  fun esExitosa() = codigo == CodigoHttp.OK

  fun superaElTiempo(tiempoParametro: Int) = tiempo > tiempoParametro
}

class ServidorWeb(val dominioServidor: String) {
  val respuestasRealizadas = mutableListOf<Respuesta>()

  // tiempo de respuesta preestablecido por el enunciado
  val tiempoRespuesta = 10

  var modulos = mutableListOf<Modulo>()

  val analizadores = mutableListOf<Analizador>()


  fun agregarModulo(nuevoModulo: Modulo) = modulos.add(nuevoModulo)

  fun quitarModulo(moduloDescartado: Modulo) = modulos.remove(moduloDescartado)

  fun agregarAnalizador(analizador: Analizador) = analizadores.add(analizador)

  fun quitarAnalizador(analizadorDescartado: Analizador) = analizadores.remove(analizadorDescartado)


  fun procesarPedido(pedido: Pedido) : Respuesta? {
    val protocoloPedido = pedido.protocoloUrl()
    var respuesta: Respuesta? = null
    var moduloResolvente: Modulo?
    // ACÁ VAN LOS LLAMADOS A LOS MÓDULOS

    //refact sta parte
    //pensar un modulo en el caso que se genera un error o no se encuentra
     if(this.validarProtocoloPedido(protocoloPedido)!=  CodigoHttp.NOT_IMPLEMENTED ){
       //en el caso de que alguno puede resolver
       if(this.primerModuloQuePuedeResolverElPedido(pedido) != null){
         moduloResolvente = this.primerModuloQuePuedeResolverElPedido(pedido)
         if (moduloResolvente != null) {
           respuesta = moduloResolvente.procesarPedido(pedido)
         }
       }
       //generar un tipo para cuando hay un modulo que no resuelve
       else{
         moduloResolvente = Modulo(Tipo.NO_ENCONTRADO,tiempoRespuesta,"")
         respuesta = Respuesta(CodigoHttp.NOT_FOUND,"", tiempoRespuesta, pedido)
       }

     }
     //generar un tipo para cuando hay un modulo que no resuelve
     else{
       moduloResolvente = Modulo(Tipo.NO_RESUELTO,tiempoRespuesta,"")
       respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"", tiempoRespuesta, pedido)
     }

    // ACÁ VAN LOS LLAMADOS A LOS ANALIZADORES

    //se agrega a los analizadores
    analizadores.forEach{
      if (moduloResolvente != null && respuesta != null ) {
          it.agregarModuloYRespuesta(moduloResolvente,respuesta)

      }
    }

    return respuesta
  }

  fun primerModuloQuePuedeResolverElPedido(pedido: Pedido) = modulos.find { it.puedeResponderElPedido(pedido) }

  fun validarProtocoloPedido(protocolo: String) = if (protocolo == "http") CodigoHttp.OK else CodigoHttp.NOT_IMPLEMENTED

  fun procesarPedidoSinModulos(pedido:Pedido): Respuesta {
    val cuerpoRespuesta = ""
    val respuesta = Respuesta(this.validarProtocoloPedido(pedido.protocoloUrl()), cuerpoRespuesta, tiempoRespuesta, pedido)
    return respuesta
  }

}



