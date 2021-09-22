package br.com.zup.pix.clientes.response

import br.com.zup.pix.chave.ContaAssociada

data class DadosDaContaResponse(
    val tipo: String,
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse
) {

    fun paraContaAssociada(): ContaAssociada {
        return ContaAssociada(
            instituicao = this.instituicao.nome,
            agencia = this.agencia,
            numero = this.numero,
            nomeTitular = this.titular.nome,
            cpfTitular = this.titular.cpf
        )
    }

}
