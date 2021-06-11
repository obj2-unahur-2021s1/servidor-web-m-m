package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  describe("pedido"){

    val pedido = Pedido("194.168.2.0","http://pepito.com.ar/documentos/doc1.html",LocalDateTime.now())
    it("el protocolo es http"){
      pedido.protocoloUrl().shouldBe("http")
    }
    it("la ruta es /documentos/doc1.html "){
      pedido.rutaUrl().shouldBe("/documentos/doc1.html")
    }

    it("la extension es html"){
      pedido.extensionUrl().shouldBe("html")
    }
  }
  describe("Un servidor web") {
    val server = ServidorWeb()

    describe("un pedido erroneo"){
      val pedidoConError = Pedido("194.168.2.0","https://pepito.com.ar/documentos/doc1.html",LocalDateTime.now())

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
  }
})
