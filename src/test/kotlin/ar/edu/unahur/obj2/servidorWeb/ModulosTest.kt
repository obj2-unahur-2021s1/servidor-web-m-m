package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ModulosTest : DescribeSpec({

    var servidor = ServidorWeb()
    val pedidoJPG = Pedido("194.168.2.0","http://pepito.com.ar/documentos/pepito.jpg", LocalDateTime.now())
    val moduloGrafico = Modulo(Tipo.GRAFICO, 15,"hola")

    moduloGrafico.agregarExtension(Extension.jpg)
    servidor.agregarModulo(moduloGrafico)

    describe("Modulo Gráfico") {

        it("nuevo procesamiento para módulo") {
            val respuesta = servidor.procesarExtension(pedidoJPG)
            respuesta.codigo.shouldBe(CodigoHttp.OK)
        }

    }
})