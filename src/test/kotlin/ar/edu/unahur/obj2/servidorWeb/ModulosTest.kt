package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ModulosTest : DescribeSpec({

    describe("Modulo Gráfico") {
        val moduloGrafico = Modulo(Tipo.GRAFICO, 15,"hola")
        it("Agregar una extension") {
            moduloGrafico.agregarExtension(Extension.jpg)
            moduloGrafico.extensiones.size.shouldBe(1)
        }
    }
})