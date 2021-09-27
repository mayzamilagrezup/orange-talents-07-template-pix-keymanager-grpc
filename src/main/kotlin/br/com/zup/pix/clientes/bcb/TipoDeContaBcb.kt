package br.com.zup.pix.clientes.bcb

import br.com.zup.pix.chave.TipoDeConta

enum class TipoDeContaBcb {

    CACC,
    SVGS;

    companion object {
        fun para(domainType: TipoDeConta) : TipoDeContaBcb {
            return  when (domainType) {
                TipoDeConta.CONTA_CORRENTE -> CACC
                TipoDeConta.CONTA_POUPANCA -> SVGS
            }
        }
    }

}
