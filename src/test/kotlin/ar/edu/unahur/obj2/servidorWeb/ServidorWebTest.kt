package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
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
  }
})
