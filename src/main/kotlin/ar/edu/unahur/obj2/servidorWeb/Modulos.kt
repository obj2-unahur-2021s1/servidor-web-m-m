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
    TEXTO, GRAFICO, SONIDO, HTML
}


class Modulo(tipo: Tipo, val tiempoRespuesta: Int) {

    var extensiones = mutableSetOf<Extension>()

    fun definirExtension(nuevaExtension: Extension) = extensiones.add(nuevaExtension)

    fun validarTipoExtension(extensionPedido: String) : Boolean = extensiones.any { it.toString() == extensionPedido }

    fun procesarPedido(pedido: Pedido) : String {
        val cuerpo : String
        when (validarTipoExtension(pedido.extensionUrl())) {
            true -> {
                cuerpo = "Se procesó la extensión " + pedido.extensionUrl()
            }
            else -> { cuerpo = "" }
        }
        return cuerpo
    }
}