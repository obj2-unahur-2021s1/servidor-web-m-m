package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import java.time.LocalDateTime

class AnalizadorTest: DescribeSpec( {

    //falta completar para probar
    val pedido1 = Pedido("194.168.2.0","http://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())
    val pedidoConError = Pedido("194.168.2.0","https://pepito.com.ar/documentos/doc1.html", LocalDateTime.now())

    val respuesta = Respuesta(CodigoHttp.OK,"hola mundo",12,pedido1)
    val respuesta2 = Respuesta(CodigoHttp.NOT_FOUND,"",10,pedidoConError)
    val respuesta3 = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"",10,pedidoConError)

    val modulo = Modulo(Tipo.HTML,12,"hola mundo")
    val modulo2 = Modulo(Tipo.TEXTO,12,"hola mundo")


    describe("analizador"){

        describe("funciones basicas de los analizadores"){
            val analizador1 = AnalizadorDemora(8)
            it("se agregan modulos y respuestas de manera correcta"){

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