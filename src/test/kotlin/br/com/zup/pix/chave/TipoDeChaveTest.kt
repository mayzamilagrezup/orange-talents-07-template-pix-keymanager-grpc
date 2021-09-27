package br.com.zup.pix.chave

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TipoDeChaveTest {


    @Test
    fun `deve ser valida quando chave for cpf valido e com formato valido`() {
        with(TipoDeChave.CPF) {
            assertTrue(valida("50001681001"))
        }
    }

    @Test
    fun `nao deve ser valida quando chave for cpf invalido ou com formato invalido`() {
        with(TipoDeChave.CPF) {
            assertFalse(valida("11144455520"))
            assertFalse(valida("500.016.810-01"))
            assertFalse(valida(null))
            assertFalse(valida(" "))
        }
    }

    @Test
    fun `deve ser valida quando chave for email em formato valido`() {
        with(TipoDeChave.EMAIL) {
            assertTrue(valida("teste@gmail.com"))
        }
    }

    @Test
    fun `nao deve ser valida quando chave for email em formato invalido`() {
        with(TipoDeChave.EMAIL) {
            assertFalse(valida("testegmail.com"))
            assertFalse(valida("teste@gmail.c"))
            assertFalse(valida(" "))
            assertFalse(valida(null))
        }
    }

    @Test
    fun `deve ser valida quando chave for celular em formato valido`() {
        with(TipoDeChave.CELULAR) {
            assertTrue(valida("+5521994748775"))
        }
    }

    @Test
    fun `nao deve ser valida quando chave for celular em formato invalido`() {
        with(TipoDeChave.CELULAR) {
            assertFalse(valida("21994748775"))
            assertFalse(valida("5521994748775"))
            assertFalse(valida(" "))
            assertFalse(valida(null))
        }
    }

    @Test
    fun `deve ser valida quando chave for aleatoria e nao tiver valor`() {
        with(TipoDeChave.ALEATORIA) {
            assertTrue(valida(" "))
            assertTrue(valida(null))
        }
    }

    @Test
    fun `nao deve ser valida quando chave for aleatoria e tiver valor`() {
        with(TipoDeChave.ALEATORIA) {
            assertFalse(valida("chave"))
            assertFalse(valida("+5521994748775"))
            assertFalse(valida("teste@gmail.com"))
            assertFalse(valida("50001681001"))
        }
    }




}