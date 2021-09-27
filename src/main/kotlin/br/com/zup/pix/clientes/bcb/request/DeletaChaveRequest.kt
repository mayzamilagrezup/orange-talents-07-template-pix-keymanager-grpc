package br.com.zup.pix.clientes.bcb.request

import br.com.zup.pix.chave.ContaAssociada

data class DeletaChaveRequest(
    val key: String,
    val participant: String = ContaAssociada.ITAU_UNIBANCO_ISPB
)

