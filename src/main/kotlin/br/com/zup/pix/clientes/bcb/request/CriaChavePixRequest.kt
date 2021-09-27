package br.com.zup.pix.clientes.bcb.request

import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.clientes.bcb.TipoChaveBcb

data class CriaChavePixRequest(
    val keyType: TipoChaveBcb,
    val key: String,
    val bankAccount: ContaBancoRequest,
    val owner: ClienteRequest
) {

    constructor(chave: ChavePix) : this(
        keyType = TipoChaveBcb.by(chave.tipoDeChave),
        key = chave.chave,
        bankAccount = ContaBancoRequest(chave),
        owner = ClienteRequest(chave)
    )
}

