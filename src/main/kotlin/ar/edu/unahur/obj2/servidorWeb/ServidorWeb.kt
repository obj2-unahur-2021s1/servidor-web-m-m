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
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){
  fun esExitosa() = codigo == CodigoHttp.OK
}

class ServidorWeb(val dominioServidor: String) {
  val respuestasRealizadas = mutableListOf<Respuesta>()
  //en teoria el lateinit evita q se envie un null
  lateinit var pedidoActual: Pedido

  // tiempo de respuesta preestablecido por el enunciado
  val tiempoRespuesta = 10

  var modulos = mutableListOf<Modulo>()

  val analizadores = mutableListOf<Analizador>()


  fun agregarModulo(nuevoModulo: Modulo) = modulos.add(nuevoModulo)
  //solucion parcial, creo q seria mejor q se pudieran identificar los modulos
  fun quitarModulo(moduloDescartado: Modulo) = modulos.removeIf { it.hashCode() == moduloDescartado.hashCode() }

  fun agregarAnalizador(analizador: Analizador) = analizadores.add(analizador)
  //solucion parcial
  fun quitarAnalizador(analizadorDescartado: Analizador) = analizadores.removeIf{it.hashCode() == analizadorDescartado.hashCode() }


  fun procesarPedido(pedido: Pedido) : Respuesta? {
    val protocoloPedido = pedido.protocoloUrl()
    val rutaPedido = pedido.rutaUrl()
    val extensionPedido = pedido.extensionUrl()
    var respuesta: Respuesta? = null
    // ACÁ VAN LOS LLAMADOS A LOS MÓDULOS

    //refact sta parte
    //pensar un modulo en el caso que se genera un error o no se encuentra
     if(this.validarProtocoloPedido(protocoloPedido)!=  CodigoHttp.NOT_IMPLEMENTED ){
       //en el caso de que alguno puede resolver
       if(this.primerModuloQuePuedeResolverElPedido(pedido) != null){
         val moduloResolvente = this.primerModuloQuePuedeResolverElPedido(pedido)
         if (moduloResolvente != null) {
           respuesta = moduloResolvente.procesarPedido(pedido)
         }
       }
       else{
         respuesta = Respuesta(CodigoHttp.NOT_FOUND,"", tiempoRespuesta, pedido)
       }

     }
     else{
       respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"", tiempoRespuesta, pedido)
     }

    // ACÁ VAN LOS LLAMADOS A LOS ANALIZADORES

    // analizadores.forEach{it.agregarModuloYRespuesta(modulo,respuesta)}

    //esto enrrealidad deberia agregrase a los analizadores


    return respuesta
  }

  fun primerModuloQuePuedeResolverElPedido(pedido: Pedido) = modulos.find { it.puedeResponderElPedido(pedido) }

  fun validarProtocoloPedido(protocolo: String) = if (protocolo == "http") CodigoHttp.OK else CodigoHttp.NOT_IMPLEMENTED

  //creo que recibir y enviar deberian estar en una misma funcion
  // arreglar essto para q sea una funcion
  fun recibirPedido(pedido: Pedido) {
    pedidoActual = pedido
  }

  fun enviarRespuesta(): Respuesta? {
    val protocoloPedido = pedidoActual.protocoloUrl()

    val cuerpoRespuesta = ""
    val respuesta = Respuesta(validarProtocoloPedido(protocoloPedido), cuerpoRespuesta, tiempoRespuesta, pedidoActual)
    respuestasRealizadas.add(respuesta)
    return respuesta
  }
}


