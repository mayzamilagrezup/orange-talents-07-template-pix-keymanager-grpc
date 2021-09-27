package br.com.zup.pix.chave

import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class ContaAssociada(
    @field:NotBlank
    val instituicao: String,

    @field:NotBlank
    val agencia: String,

    @field:NotBlank
    val numero: String,

    @field:NotBlank
    val nomeTitular: String,

    @field:NotBlank
    val cpfTitular: String
) {

    companion object {
        public val ITAU_UNIBANCO_ISPB: String = "60701190"
    }
}
