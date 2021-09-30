package br.com.zup.pix.chave

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class ChavePixTest {

    companion object {
        val tiposDeChaveExcetoAleatoria = TipoDeChave.values().filterNot { it == TipoDeChave.ALEATORIA }
    }

    @Test
    fun `deve chave ser do tipo aleatoria`() {
        with(chave(TipoDeChave.ALEATORIA)) {
            assertTrue(this.isAleatoria())
        }
    }

    @Test
    fun `nao deve chave ser do tipo aleatoria`() {
        tiposDeChaveExcetoAleatoria.forEach { assertFalse(chave(it).isAleatoria()) }
    }

    @Test
    fun `deve atualizar chave quando for aleatoria`() {
        with(chave(TipoDeChave.ALEATORIA)) {
            assertTrue(this.atualiza("chave atualizada"))
            assertEquals("chave atualizada", this.chave)
        }

    }

    private fun chave(
        tipo: br.com.zup.pix.chave.TipoDeChave,
        chave: String = UUID.randomUUID().toString(),
        clienteId: UUID = UUID.randomUUID()
    ): ChavePix {
        return ChavePix(
            clienteId = clienteId,
            tipoDeChave = tipo,
            chave = chave,
            tipoDeConta = br.com.zup.pix.chave.TipoDeConta.CONTA_CORRENTE,
            conta = ContaAssociada(
                instituicao = "UNIBANCO ITAU",
                agencia = "1452",
                numero = "5555",
                nomeTitular = "Usuario Teste",
                cpfTitular = "68626892071"
            )
        )
    }
}