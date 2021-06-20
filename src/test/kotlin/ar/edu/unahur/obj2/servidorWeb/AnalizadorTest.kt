package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class AnalizadorTest: DescribeSpec( {

    //falta completar para probar
    val pedido1 = Pedido("194.168.2.0","http://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
    val pedidoConError = Pedido("194.168.2.0","https://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())

    val respuesta = Respuesta(CodigoHttp.OK,"hola mundo",12,pedido1)
    val respuesta2 = Respuesta(CodigoHttp.NOT_FOUND,"",10,pedidoConError)
    val respuesta3 = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"",10,pedidoConError)
    val respuesta4 = Respuesta(CodigoHttp.OK,"hola mundo",12,pedido1)

    val modulo1 = Modulo(Tipo.HTML,12,"hola mundo")
    val modulo2 = Modulo(Tipo.TEXTO,12,"hola mundo")


    describe("analizador"){

        describe("funciones basicas de los analizadores"){
            val analizador1 = AnalizadorDemora(8)

            analizador1.agregarModuloYRespuesta(modulo1,respuesta)
            analizador1.agregarModuloYRespuesta(modulo1,respuesta4)
            analizador1.agregarModuloYRespuesta(modulo2,respuesta2)

            it("se agregan modulos y respuestas de manera correcta"){
                analizador1.modulosYRespuestas.shouldNotBeNull()
            }
            it("el modulo1 tiene 2 respuestas"){
                analizador1.cantidadDeRespuestasPorModulo(modulo1).shouldBe(2)
            }
        }

        describe("analizador estadisticas"){

        }
        describe("analizador ip sospechosas"){

        }

        describe("analizador tiempo"){

        }
    }
})