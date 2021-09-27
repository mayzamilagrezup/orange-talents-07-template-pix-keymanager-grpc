package br.com.zup.pix.clientes.bcb

import br.com.zup.pix.chave.TipoDeChave

enum class TipoChaveBcb(val domainType: TipoDeChave?) {
    CPF(TipoDeChave.CPF),
    CNPJ(null),
    PHONE(TipoDeChave.CELULAR),
    EMAIL(TipoDeChave.EMAIL),
    RANDOM(TipoDeChave.ALEATORIA);

    companion object {
        private val mapping = TipoChaveBcb.values().associateBy(TipoChaveBcb::domainType)

        fun by(domainType: TipoDeChave): TipoChaveBcb {
            return  mapping[domainType] ?: throw IllegalArgumentException("Chave inválida ou não encontrada para $domainType")
        }

    }

}
