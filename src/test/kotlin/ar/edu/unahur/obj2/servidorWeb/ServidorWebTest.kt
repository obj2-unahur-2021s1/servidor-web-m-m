package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  val server = ServidorWeb()

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

    describe("sin modulos"){

      describe("un pedido erroneo"){
        it("devuele el codigo 501"){

          //suponiendo que tiene q devolver 501, en el caso que no eliminar el ultimo .codigo
          val cod = server.procesarPedidoSinModulos(pedidoConError).codigo.codigo
          cod.shouldBe(501)
        }

        it("no devuelve el codigo de errror de esta ok(200) "){

          val cod = server.procesarPedidoSinModulos(pedidoConError).codigo
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


          val respuesta = server.procesarPedidoSinModulos(pedido1)
          respuesta.tiempo.shouldBe(nano)
        }

        it("el tiempo no sera distinto de 10 milisegundos"){

          val respuesta = server.procesarPedidoSinModulos(pedido1)
          respuesta.tiempo.shouldNotBe(15)
        }
      }
    }
    describe("con modulos"){
      describe("respuesta de pedido http") {
        val modulo = Modulo(Tipo.HTML,12,"hola mundo")

        it("se obtiene una respuesta al pedido") {
          modulo.agregarExtension(Extension.html)
          server.agregarModulo(modulo)

          val respuesta = server.procesarPedido(pedido1)
          respuesta?.codigo.shouldBe(CodigoHttp.OK)
          respuesta?.body.shouldBe("hola mundo")
          respuesta?.tiempo.shouldBe(12)
          respuesta?.pedido.shouldBe(pedido1)
        }
        it("se obtiene una respuesta con codigo de not found, ya que no no hay un modulo que entienda la extension"){
          server.agregarModulo(modulo)

          val respuesta = server.procesarPedido(pedido2)
          respuesta?.codigo.shouldBe(CodigoHttp.NOT_FOUND)
          respuesta?.body.shouldBe("")
          respuesta?.tiempo.shouldBe(10)
          respuesta?.pedido.shouldBe(pedido2)
        }
        it("no hay ningun pedido y termina con codigo 501"){
          val respuesta = server.procesarPedido(pedidoConError2)
          respuesta?.codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
          respuesta?.body.shouldBe("")
          respuesta?.tiempo.shouldBe(10)
          respuesta?.pedido.shouldBe(pedidoConError2)
        }
      }
      describe("se agrega y elimina modulos"){
        val moduloGrafico = Modulo(Tipo.GRAFICO, 15,"hola")
        val moduloTexto = Modulo(Tipo.TEXTO,12,"hola")

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
      describe("Servidor consulta módulos") {

        val modulo = Modulo(Tipo.HTML,12,"hola mundo")
        val moduloTexto = Modulo(Tipo.TEXTO, 12, "hola")
        val pedidoOdt = Pedido("194.168.2.0", "http://pepito.com.ar/documentos/apuntes.odt", LocalDateTime.now())
        val analizador1 = AnalizadorDemora(8)

        moduloTexto.agregarExtension(Extension.odt)
        moduloTexto.agregarExtension(Extension.docx)

        modulo.agregarExtension(Extension.html)
        server.agregarModulo(modulo)
        server.agregarModulo(moduloTexto)

        server.agregarAnalizador(analizador1)

        it("hay un modulo en el servidor que puede resolver el pedido") {
          server.primerModuloQuePuedeResolverElPedido(pedidoOdt).shouldNotBe(null)
        }

        //estos creo q deberian estar en analizadorTest
        it("hay 2 respuestas para el modulo"){
          server.procesarPedido(pedido1)
          server.procesarPedido(pedido1)
          analizador1.cantidadDeRespuestasPorModulo(modulo).shouldBe(2)
        }

        it("la cantidad de respuestas totales deben ser 3"){
          server.procesarPedido(pedido1)
          server.procesarPedido(pedido1)
          server.procesarPedido(pedidoOdt)
          analizador1.cantidadDeRespuestaTotales().shouldBe(3)
        }
      }
    }
  }
  describe("Analizadores") {
    val pedidoODT = Pedido("194.168.2.0", "http://pepito.com.ar/documentos/pepito.odt", LocalDateTime.now())
    val analizadorDemora = AnalizadorDemora(1)
    val moduloTexto = Modulo(Tipo.TEXTO, 12, "hola, mundo")

    moduloTexto.agregarExtension(Extension.odt)
    server.agregarModulo(moduloTexto)
    server.agregarAnalizador(analizadorDemora)
    server.procesarPedido(pedidoODT)

    it("Un pedido demorado") {
      analizadorDemora.cantidadDeRespuestasDemoradasPorModulo(moduloTexto).shouldBe(1)
    }

    describe("analizador de ip sospechosas") {
      val ipSospechosas = listOf("104.26.8.142", "142.250.186.131")
      val pedidoPASS1 = Pedido("104.26.8.142", "http://pepito.com.ar/passwords.txt", LocalDateTime.now())
      val pedidoPASS2 = Pedido("104.26.8.142", "http://pepito.com.ar/passwords.odt", LocalDateTime.now())
      val pedidoPASS3 = Pedido("142.250.186.131", "http://pepito.com.ar/passwords.odt", LocalDateTime.now())
      val analizadorSospechoso = AnalizadorIpSospechosa(ipSospechosas)

      server.agregarAnalizador(analizadorSospechoso)
      server.procesarPedido(pedidoPASS1)
      server.procesarPedido(pedidoPASS2)

      it("104.26.8.142 realizó dos consultas sospechosas") {
        analizadorSospechoso.pedidosTotalesRealizadosPorUnaIpSospechosa("104.26.8.142").shouldBe(2)
      }
      // agregamos un pedido más para el módulo texto
      server.procesarPedido(pedidoPASS3)

      it("módulo más consultado por todas las ip sospechosas") {
        analizadorSospechoso.moduloMasConsultadoPorLasIpSospechosas().shouldBe(moduloTexto)
      }

      it("conjunto ip sospechosas que consultaron una ruta") {
        analizadorSospechoso.conjuntoDeIpQueSolicitaronUnaRuta("/passwords.odt")
          .shouldBe(listOf("104.26.8.142", "142.250.186.131"))
      }
    }


    // agregado nuevo
    describe("segundo analizador de ip sospechosas") {
      val pedidoODT2 = Pedido("194.168.2.0", "http://pepito.com.ar/documentos/pepito.odt", LocalDateTime.now())

      val listaIps = mutableListOf<String>()
      listaIps.add("194.168.2.0")
      listaIps.add("194.168.2.1")
      listaIps.add("194.168.2.2")

      val analizadorIp = AnalizadorIpSospechosa(listaIps)
      server.agregarAnalizador(analizadorIp)
      server.procesarPedido(pedidoODT)
      server.procesarPedido(pedidoODT2)

      it("la primera ip sospechosa realizo 2 pedidos") {
        analizadorIp.pedidosTotalesRealizadosPorUnaIpSospechosa(listaIps.first()).shouldBe(2)
      }

      it("el modulo mas consultado por las ip sospechosas debe ser el modulo de texto") {
        analizadorIp.moduloMasConsultadoPorLasIpSospechosas().shouldBe(moduloTexto)
      }

      it("la ip que hizo un pedido sospechoso es la 194.168.2.0") {

        analizadorIp.respuestasConRutaEnUnModulo(moduloTexto, "/documentos/pepito.odt")?.size.shouldBe(2)
        analizadorIp.conjuntoDeIpQueSolicitaronUnaRuta("/documentos/pepito.odt").size.shouldBe(1)
        analizadorIp.conjuntoDeIpQueSolicitaronUnaRuta("/documentos/pepito.odt").contains("194.168.2.0").shouldBeTrue()
      }

    }
  }
})
