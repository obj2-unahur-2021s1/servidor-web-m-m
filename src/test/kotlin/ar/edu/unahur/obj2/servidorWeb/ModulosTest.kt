package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec

class ModulosTest : DescribeSpec({

    describe("Modulo Gráfico") {
        var moduloGrafico = Modulo(Tipo.GRAFICO, 15)
        it("Agregar una extension") {
            moduloGrafico.agregarExtension(Extension.jpg)
        }
    }
})