package br.com.zup.pix.chave.registra


import br.com.zup.pix.anotacoes.ValidUUID
import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.chave.ContaAssociada
import br.com.zup.pix.chave.TipoDeChave
import br.com.zup.pix.chave.TipoDeConta
import br.com.zup.pix.anotacoes.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPixKey
data class NovaChavePixRequest(
    @field:NotBlank @ValidUUID val clienteId: String?,
    @field:NotNull val tipoDeChave: TipoDeChave?,
    @field:Size(max = 77) val chave: String?,
    @field:NotNull val tipoDeConta: TipoDeConta?
) {

    fun paraChavePix(conta: ContaAssociada) : ChavePix {

        return ChavePix(
            clienteId = UUID.fromString(this.clienteId),
            tipoDeChave = TipoDeChave.valueOf(this.tipoDeChave!!.name),
            chave = if (tipoDeChave == TipoDeChave.ALEATORIA) UUID.randomUUID().toString() else chave!!,
            tipoDeConta = TipoDeConta.valueOf(this.tipoDeConta!!.name),
            conta = conta

        )
    }


}
