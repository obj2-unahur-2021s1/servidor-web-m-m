package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime
import kotlin.concurrent.timer

val dominioServidor = "pepito.com.ar"

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime) {
  fun protocoloUrl() = url.split(":").first()
  fun rutaUrl() = url.split(dominioServidor).last()
  fun extensionUrl() = url.split(".").last()
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){}

class ServidorWeb() {
  val cantidadDePedidos = mutableListOf<Pedido>()
  var pedidosExistosos = 0
  //en teoria el lateinit evita q se envie un null
  lateinit var pedidoActual: Pedido

  // tiempo de respuesta preestablecido por el enunciado
  val tiempoRespuesta = 10

  //creo que recibir y enviar deberian estar en una misma funcion

  fun recibirPedido(pedido: Pedido) {
    pedidoActual = pedido
  }

  fun procesarPedido(pedido: Pedido) : Respuesta {
    val protocoloPedido = pedido.protocoloUrl()
    val rutaPedido = pedido.rutaUrl()
    val extensioPedido = pedido.extensionUrl()

    // ACÁ VAN LOS MÓDULOS
    // ACÁ VAN LOS ANALIZADORES

    // esto es en primera instancia.... después se modifica con la inclusión de los módulos
    val cuerpoRespuesta = ""

    return Respuesta(validarProtocoloPedido(protocoloPedido), cuerpoRespuesta, tiempoRespuesta, pedido)
  }

  fun validarProtocoloPedido(protocolo: String) = if (protocolo == "http") CodigoHttp.OK else CodigoHttp.NOT_IMPLEMENTED

  fun calcularTiempoRespuesta(fechaPedido: LocalDateTime) {
    // TODO: 11/6/21
    // retornar tiempo actual - tiempo del pedido
  }

  fun enviarRespuesta(): Respuesta? {
    var respuesta: Respuesta? = null
    if (!pedidoActual.protocoloUrl().equals("http")) {
      //solucion parcial para los 10 milisegundos
      val tiempo1 = LocalDateTime.now()
      val tiempo2 = tiempo1.plusNanos(10)
      val nano = tiempo2.nano - tiempo1.nano
      //no se si se devuelve asi un enun
      respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED, "todavia no funciona", nano, pedidoActual)
    }
    return respuesta
  }

  fun fueUnPedidoExitoso(){pedidosExistosos+=1}
}