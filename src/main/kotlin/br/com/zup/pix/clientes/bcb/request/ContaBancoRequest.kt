package br.com.zup.pix.clientes.bcb.request

import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.chave.ContaAssociada
import br.com.zup.pix.clientes.bcb.TipoDeContaBcb

data class ContaBancoRequest(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: TipoDeContaBcb
) {
    constructor(chave: ChavePix) : this(
        ContaAssociada.ITAU_UNIBANCO_ISPB,
        chave.conta.agencia,
        chave.conta.numero,
        TipoDeContaBcb.para(chave.tipoDeConta)
    )
}
