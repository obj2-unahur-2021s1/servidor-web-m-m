package ar.edu.unahur.obj2.servidorWeb

// REVISAR O CONSULTAR SI ESTAMOS LLAMANDO BIEN A LOS ENUM...

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

//creo q hay q definir al crearse su body
class Modulo(tipo: Tipo, val tiempoRespuesta: Int) {

    var extensiones = mutableSetOf<Extension>()

    fun agregarExtension(nuevaExtension: Extension) = extensiones.add(nuevaExtension)

    fun validarExtension(extensionPedido: String) : Boolean = extensiones.any { it.toString() == extensionPedido }

    fun procesarPedido(pedido: Pedido) : String {
        val cuerpo : String
        when (validarExtension(pedido.extensionUrl())) {
            true -> {
                cuerpo = "Se procesó la extensión " + pedido.extensionUrl()
            }
            else -> { cuerpo = "" }
        }
        return cuerpo
    }

    fun puedeResponderElPedido(pedido: Pedido) =  extensiones.any{it.toString() == pedido.extensionUrl()}
}