package ar.edu.unahur.obj2.servidorWeb

enum class Extension(val tipo: Tipo) {
    jpg(Tipo.GRAFICO),
    png(Tipo.GRAFICO),
    gif(Tipo.GRAFICO),
    docx(Tipo.TEXTO),
    odt(Tipo.TEXTO),
    html(Tipo.HTML)
}

enum class Tipo() {
    TEXTO, GRAFICO, SONIDO, HTML, NO_RESUELTO, NO_ENCONTRADO
}

class Modulo(tipo: Tipo, val tiempoRespuesta: Int, val cuerpoRespuesta: String) {
    var extensiones = mutableSetOf<Extension>()

    fun agregarExtension(nuevaExtension: Extension) = extensiones.add(nuevaExtension)

    fun procesarPedido(pedido: Pedido) : Respuesta {
        return Respuesta(CodigoHttp.OK,cuerpoRespuesta,tiempoRespuesta,pedido)
    }

    fun puedeResponderPedido(pedido: Pedido) =  extensiones.any { it.toString() == pedido.extensionUrl() }
}