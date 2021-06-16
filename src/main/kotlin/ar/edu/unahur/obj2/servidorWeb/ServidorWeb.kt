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
  // alternativa con RegEx -- se puede complejizar para extraer el dominio
  // fun rutaUrl() = "/" + url.split("""\.[a-z]*/""".toRegex()).last()
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

  fun agregarModulo(nuevoModulo: Modulo) = modulos.add(nuevoModulo)
  //solucion parcial, creo q seria mejor q se pudieran identificar los modulos
  fun quitarModulo(moduloDescartado: Modulo) = modulos.removeIf { it.hashCode() == moduloDescartado.hashCode() }

  fun procesarPedido(pedido: Pedido) : Respuesta {
    val protocoloPedido = pedido.protocoloUrl()
    val rutaPedido = pedido.rutaUrl()
    val extensionPedido = pedido.extensionUrl()
    //val respuesta = null
    // ACÁ VAN LOS LLAMADOS A LOS MÓDULOS
    /*
      //en el caso de que alguno puede resolver
      if(this.primerModuloQuePuedeResolverElPedido(pedido) != null){
         val moduloResolvente = this.primerModuloQuePuedeResolverElPedido(pedido)
         respuesta = Respuesta(validarProtocoloPedido(protocoloPedido), moduloResolvente.cuerpo[yo creo q deberiamos usar funcion],
                                moduloResolvente.tiempoRespuesta, pedido)
      }

      //caso de error
      else{

        respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"", tiempoRespuesta, pedido)
      }
       respuestasRealizadas.add(respuesta)
     */
    // ACÁ VAN LOS LLAMADOS A LOS ANALIZADORES

    // esto es en primera instancia.... después se modifica con la inclusión de los módulos
    val cuerpoRespuesta = ""
    val respuesta = Respuesta(this.validarProtocoloPedido(protocoloPedido), cuerpoRespuesta, tiempoRespuesta, pedido)
    respuestasRealizadas.add(respuesta)

    return respuesta
  }

  //fun puedeAlgunModuloResolverElPedido(pedido: Pedido) = modulos.any { it.puedeResponderElPedido(pedido)}

  fun primerModuloQuePuedeResolverElPedido(pedido: Pedido) = modulos.find { it.puedeResponderElPedido(pedido) }

  fun validarProtocoloPedido(protocolo: String) = if (protocolo == "http") CodigoHttp.OK else CodigoHttp.NOT_IMPLEMENTED

  fun calcularTiempoRespuesta(fechaPedido: LocalDateTime) {
    // TODO: 11/6/21
    // retornar tiempo actual - tiempo del pedido
  }

  //creo que recibir y enviar deberian estar en una misma funcion

  fun recibirPedido(pedido: Pedido) {
    pedidoActual = pedido
  }

  fun enviarRespuesta(): Respuesta? {
    val protocoloPedido = pedidoActual.protocoloUrl()
    //var respuesta: Respuesta? = null
    /*
    if (!pedidoActual.protocoloUrl().equals("http")) {
      //solucion parcial para los 10 milisegundos
      val tiempo1 = LocalDateTime.now()
      val tiempo2 = tiempo1.plusNanos(10)
      val nano = tiempo2.nano - tiempo1.nano
      //no se si se devuelve asi un enun
      respuesta = Respuesta(CodigoHttp.NOT_IMPLEMENTED, "todavia no funciona", nano, pedidoActual)
    }else{
      val tiempo1 = LocalDateTime.now()
      val tiempo2 = tiempo1.plusNanos(10)
      val nano = tiempo2.nano - tiempo1.nano
      respuesta = Respuesta(CodigoHttp.OK, "correcto", nano, pedidoActual)
    }
     */
    val cuerpoRespuesta = ""
    val respuesta = Respuesta(validarProtocoloPedido(protocoloPedido), cuerpoRespuesta, tiempoRespuesta, pedidoActual)
    respuestasRealizadas.add(respuesta)
    return respuesta
  }

  //probar
  fun cantidadDeRespuestasExitosas() = respuestasRealizadas.filter { it.esExitosa() }.size
  //probar
  fun porcentajeDeRespuestasExitosas() = (this.cantidadDeRespuestasExitosas() * 100)/respuestasRealizadas.size
  //probar
  fun tiempoDeRespuestaPromedio() = respuestasRealizadas.sumBy { it.tiempo } / respuestasRealizadas.size
}