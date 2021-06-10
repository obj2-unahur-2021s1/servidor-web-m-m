package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){
  fun protocoloUrl() = url.split(":").first()

  fun rutaUrl() = url.split("ar").last()

  fun extensionUrl() = url.split(".").last()
}
class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){}

class ServidorWeb(){
  val cantidadDePedidos = mutableListOf<Pedido>()
  var pedidosExistosos = 0
  var pedidoActual: Pedido? = null

  fun recibirPedido(pedido: Pedido){
    pedidoActual = pedido
  }

  fun enviarRespuesta(){}

  fun fueUnPedidoExitoso(){pedidosExistosos+=1}
}