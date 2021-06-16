package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  val server = ServidorWeb("pepito.com.ar")

  val pedido1 = Pedido("194.168.2.0","http://pepito.com.ar/documentos/doc1.html",LocalDateTime.now())
  val pedido2 = Pedido("194.168.2.0","http://pepito.com.ar/documentos/doc2.html",LocalDateTime.now())
  val pedidoConError = Pedido("194.168.2.0","https://pepito.com.ar/documentos/doc1.html",LocalDateTime.now())
  val pedidoConError2 = Pedido("194.168.2.0","https://pepito.com.ar/documentos/doc1.html",LocalDateTime.now())


  describe("pedido"){

    it("el protocolo es http"){
      pedido1.protocoloUrl().shouldBe("http")
    }
    it("la ruta es /documentos/doc1.html "){
      pedido1.rutaUrl().shouldBe("/documentos/doc1.html")
    }

    it("la extension es html"){
      pedido1.extensionUrl().shouldBe("html")
    }
  }

  describe("Un servidor web") {

    describe("un pedido erroneo"){

      it("devuele el codigo 501"){
        server.recibirPedido(pedidoConError)
        //suponiendo que tiene q devolver 501, en el caso que no eliminar el ultimo .codigo
        val cod = server.enviarRespuesta()?.codigo?.codigo
        cod.shouldBe(501)
      }

      it("no devuelve el codigo de errror de esta ok(200) "){
        server.recibirPedido(pedidoConError)
        val cod = server.enviarRespuesta()?.codigo?.codigo
        cod.shouldNotBe(200)
      }

      it("valida el protocolo recibido como protocolo http") {
        val protocoloPedido = pedido1.protocoloUrl()
        server.validarProtocoloPedido(protocoloPedido).shouldBe(CodigoHttp.OK)
      }

      it("el tiempo que tarda es de 10 milisegundos"){
        //10 mili
        val tiempo1 = LocalDateTime.now()
        val tiempo2 = tiempo1.plusNanos(10)
        val nano = tiempo2.nano - tiempo1.nano

        server.recibirPedido(pedidoConError)
        val respuesta = server.enviarRespuesta()
        respuesta?.tiempo.shouldBe(nano)
      }

      it("el tiempo no sera distinto de 10 milisegundos"){
        server.recibirPedido(pedidoConError)
        val respuesta = server.enviarRespuesta()
        respuesta?.tiempo.shouldNotBe(15)
      }
    }
    describe("respuesta de pedido http") {
      it("obtiene una respuesta al pedido") {
        val respuesta = server.procesarPedido(pedido1)
        respuesta.codigo.shouldBe(CodigoHttp.OK)
        respuesta.body.shouldBe("")
        respuesta.tiempo.shouldBe(10)
        respuesta.pedido.shouldBe(pedido1)
      }
    }
    describe("estadisticas"){
      val respuesta1 = server.procesarPedido(pedido1)
      val respuesta2 = server.procesarPedido(pedido2)
      val respuesta3 = server.procesarPedido(pedidoConError)
      val respuesta4 = server.procesarPedido(pedidoConError2)

      it("el tiempo de respuesta promedio es de 10"){
        server.tiempoDeRespuestaPromedio().shouldBe(10)
      }
      it("el porcentaje de respuesta exitosa es de 50.0"){
        server.porcentajeDeRespuestasExitosas().shouldBe(50.0)
      }
    }

   describe("se agrega y elimina modulos"){
     val moduloGrafico = Modulo(Tipo.GRAFICO, 15)
     val moduloTexto = Modulo(Tipo.TEXTO,12)

     it("se agregan los 2 "){
       server.agregarModulo(moduloGrafico)
       server.agregarModulo(moduloTexto)

       server.modulos.size.shouldBe(2)
     }
     it("se agrega los 2 y se elimina 1"){
       server.agregarModulo(moduloGrafico)
       server.agregarModulo(moduloTexto)
       server.quitarModulo(moduloGrafico)

       server.modulos.size.shouldBe(1)
       server.modulos.contains(moduloGrafico).shouldBeFalse()
     }
   }
  }
  describe("Servidor consulta módulos") {
    val moduloTexto = Modulo(Tipo.TEXTO,12)
    val pedidoOdt = Pedido("194.168.2.0","http://pepito.com.ar/documentos/apuntes.odt", LocalDateTime.now())
    moduloTexto.agregarExtension(Extension.odt)
    server.agregarModulo(moduloTexto)
    it("Consulta resuelta") {
      //server.
    //server.primerModuloQuePuedeResolverElPedido(pedidoOdt).shouldBe(true)
    }
  }
})
