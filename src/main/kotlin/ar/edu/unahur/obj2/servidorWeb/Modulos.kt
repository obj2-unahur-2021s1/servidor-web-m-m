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
class Modulo(tipo: Tipo, val tiempoRespuesta: Int, val cuerpoRespuesta: String) {

    var extensiones = mutableSetOf<Extension>()

    fun agregarExtension(nuevaExtension: Extension) = extensiones.add(nuevaExtension)

    fun validarExtension(extensionPedido: String) : Boolean = extensiones.any { it.toString() == extensionPedido }

    // ESTO HAY QUE REFORMULARLO, pensar el caso de que no haya un modulo que resuelve
    fun procesarPedido(pedido: Pedido) : Respuesta {
        return Respuesta(CodigoHttp.OK,cuerpoRespuesta,tiempoRespuesta,pedido)
    }

    fun puedeResponderElPedido(pedido: Pedido) =  extensiones.any{it.toString() == pedido.extensionUrl()}
}