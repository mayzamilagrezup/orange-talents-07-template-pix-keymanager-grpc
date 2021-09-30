package br.com.zup.pix.clientes.bcb.response

import br.com.zup.pix.clientes.bcb.TipoChaveBcb
import br.com.zup.pix.clientes.bcb.request.ClienteRequest
import br.com.zup.pix.clientes.bcb.request.ContaBancoRequest
import java.time.LocalDateTime

data class CriaChavePixResponse(
    val keyType: TipoChaveBcb,
    var key: String,
    val bankAccount: ContaBancoRequest,
    val owner: ClienteRequest,
    val createdAt: LocalDateTime
)
