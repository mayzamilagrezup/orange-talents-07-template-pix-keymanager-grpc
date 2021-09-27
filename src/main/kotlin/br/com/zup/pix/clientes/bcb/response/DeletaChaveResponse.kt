package br.com.zup.pix.clientes.bcb.response

import java.time.LocalDateTime

data class DeletaChaveResponse(
    val key: String,
    val participant: String,
    val deletedAt: LocalDateTime
)
