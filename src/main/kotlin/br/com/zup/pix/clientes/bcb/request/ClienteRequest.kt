package br.com.zup.pix.clientes.bcb.request

import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.clientes.bcb.TipoDeClienteBcb

data class ClienteRequest(
    val type: TipoDeClienteBcb,
    val name: String,
    val taxIdNumber: String
) {
    constructor(chave: ChavePix) : this(
        TipoDeClienteBcb.NATURAL_PERSON,
        chave.conta.nomeTitular,
        chave.conta.cpfTitular
    )
}
