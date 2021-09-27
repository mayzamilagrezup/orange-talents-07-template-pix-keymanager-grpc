package br.com.zup.pix.chave


import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class ChavePix(
    @field:NotNull
    @Column(nullable = false)
    val clienteId: UUID,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDeChave: TipoDeChave,

    @field:NotBlank
    @field:Size(max = 77)
    @Column(unique = true, nullable = false)
    var chave: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDeConta: TipoDeConta,

    @field:Valid
    @Embedded
    val conta: ContaAssociada
) {

    @Id
    @GeneratedValue
    val id: UUID? = null

    @Column(nullable = false)
    val criadaEm: LocalDateTime = LocalDateTime.now()

    fun isAleatoria(): Boolean {
        return tipoDeChave == TipoDeChave.ALEATORIA
    }

    fun atualiza(chave: String): Boolean {
        if (isAleatoria()) {
            this.chave = chave
            return true
        }
        return false
    }

}
