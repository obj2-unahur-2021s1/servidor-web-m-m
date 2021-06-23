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

  fun laRespuestaFueGeneradaParaElPedidoConIp(ip: String) = pedido.fuePedidoPorLaIp(ip)
}

class ServidorWeb() {

  val tiempoRespuesta = 10
  var modulos = mutableListOf<Modulo>()
  val analizadores = mutableListOf<Analizador>()

  fun agregarModulo(nuevoModulo: Modulo) = modulos.add(nuevoModulo)
  fun quitarModulo(moduloDescartado: Modulo) = modulos.remove(moduloDescartado)

  fun agregarAnalizador(analizador: Analizador) = analizadores.add(analizador)
  fun quitarAnalizador(analizadorDescartado: Analizador) = analizadores.remove(analizadorDescartado)

  fun procesarProtocolo(pedido: Pedido) : Respuesta {
    if (pedido.protocoloUrl() == "http")
      return Respuesta(CodigoHttp.OK, "Ok (200)", tiempoRespuesta, pedido)
    else
      return Respuesta(CodigoHttp.NOT_IMPLEMENTED, "Not implemented (501)", tiempoRespuesta, pedido)
  }

  fun buscarModulo(pedido: Pedido): Modulo {
    var modu = modulos.find { it.puedeResponderPedido(pedido) }
    if ( modu != null)
      return modu
     else
      return Modulo(Tipo.NO_ENCONTRADO,10,"")
  }

  fun procesarPedido(pedido: Pedido) : Respuesta? {
    val protocoloPedido = pedido.protocoloUrl()
    var respuesta: Respuesta? = null
    var moduloResolvente: Modulo?

     if (this.validarProtocoloPedido(protocoloPedido) ==  CodigoHttp.OK ) {
       if(this.primerModuloQuePuedeResolverElPedido(pedido) != null){
         moduloResolvente = this.primerModuloQuePuedeResolverElPedido(pedido)
         if (moduloResolvente != null) {
           respuesta = moduloResolvente.procesarPedido(pedido)
         }
       }
       else{
         moduloResolvente = Modulo(Tipo.NO_ENCONTRADO,tiempoRespuesta,"")
         respuesta = Respuesta(CodigoHttp.NOT_FOUND,"", tiempoRespuesta, pedido)
       }
     }
     else{
       moduloResolvente = Modulo(Tipo.NO_RESUELTO,tiempoRespuesta,"")
       respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"", tiempoRespuesta, pedido)
     }

    analizadores.forEach{
      if (moduloResolvente != null && respuesta != null ) {
          it.agregarModuloYRespuesta(moduloResolvente,respuesta)
      }
    }
    return respuesta
  }

  fun primerModuloQuePuedeResolverElPedido(pedido: Pedido) = modulos.find { it.puedeResponderPedido(pedido) }

  fun validarProtocoloPedido(protocolo: String) = if (protocolo == "http") CodigoHttp.OK else CodigoHttp.NOT_IMPLEMENTED

  fun procesarPedidoSinModulos(pedido:Pedido): Respuesta {
    val cuerpoRespuesta = ""
    val respuesta = Respuesta(this.validarProtocoloPedido(pedido.protocoloUrl()), cuerpoRespuesta, tiempoRespuesta, pedido)
    return respuesta
  }
}