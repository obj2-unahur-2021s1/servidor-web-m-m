package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ModulosTest : DescribeSpec({

    var servidor = ServidorWeb()
    val pedidoJPG = Pedido("194.168.2.0","http://pepito.com.ar/documentos/pepito.jpg", LocalDateTime.now())
    val pedidoPDF = Pedido("194.168.2.0","http://pepito.com.ar/documentos/pepito.pdf", LocalDateTime.now())
    val moduloGrafico = Modulo(Tipo.GRAFICO, 15,"hola")

    moduloGrafico.agregarExtension(Extension.jpg)
    servidor.agregarModulo(moduloGrafico)

    describe("Servidor con un módulo Gráfico") {

        it("procesar jpg") {
            val respuesta = servidor.puedeProcesarExtension(pedidoJPG)
            respuesta.codigo.shouldBe(CodigoHttp.OK)
        }

        it("no puede procesar pdf") {
            val respuesta = servidor.puedeProcesarExtension(pedidoPDF)
            respuesta.codigo.shouldBe(CodigoHttp.NOT_FOUND)
        }

    }
})