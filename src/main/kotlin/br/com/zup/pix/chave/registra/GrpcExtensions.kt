package br.com.zup.pix.chave.registra

import br.com.zup.RegistraChavePixRequest
import br.com.zup.TipoDeChave.UNKNOWN_TIPO_CHAVE
import br.com.zup.TipoDeConta.UNKNOWN_TIPO_CONTA
import br.com.zup.pix.chave.TipoDeChave
import br.com.zup.pix.chave.TipoDeConta

fun RegistraChavePixRequest.paraNovaChavePixRequest() : NovaChavePixRequest {

    return NovaChavePixRequest(
        clienteId = clienteId,
        tipoDeChave = when(tipoDeChave) {
            UNKNOWN_TIPO_CHAVE -> null
            else -> TipoDeChave.valueOf(tipoDeChave.name)
        },
        chave = chave,
        tipoDeConta = when(tipoDeConta) {
            UNKNOWN_TIPO_CONTA -> null
            else -> TipoDeConta.valueOf(tipoDeConta.name)
        }
    )
}