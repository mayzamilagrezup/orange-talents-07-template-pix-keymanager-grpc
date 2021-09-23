package br.com.zup.pix.chave.registra

import br.com.zup.KeyManagerRegistraServiceGrpc
import br.com.zup.RegistraChavePixRequest
import br.com.zup.TipoDeChave
import br.com.zup.TipoDeConta
import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.chave.ContaAssociada
import br.com.zup.pix.chave.TipoDeChave.*
import br.com.zup.pix.clientes.ItauClient
import br.com.zup.pix.clientes.response.DadosDaContaResponse
import br.com.zup.pix.clientes.response.InstituicaoResponse
import br.com.zup.pix.clientes.response.TitularResponse
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*


@MicronautTest(transactional = false)
internal class RegistraChavePixEndpointTest(
    val repository: ChavePixRepository,
    val grpcClient: KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub
) {


    @Inject
    lateinit var itauClient: ItauClient

    companion object {
        val CLIENTE_ID = UUID.randomUUID()
    }

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun `deve registrar uma nova chave pix do tipo cpf valido`() {

        `when`(itauClient.buscaContaPorTipo(clienteId = CLIENTE_ID.toString(), tipo = TipoDeConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))

        val response = registraChavePixRequest(TipoDeChave.CPF, "46196026046", TipoDeConta.CONTA_CORRENTE)

        with(response) {
            assertEquals(CLIENTE_ID.toString(), clienteId)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `deve registrar uma nova chave pix do tipo celular valido`() {

        `when`(itauClient.buscaContaPorTipo(clienteId = CLIENTE_ID.toString(), tipo = TipoDeConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))

        val response = registraChavePixRequest(TipoDeChave.CELULAR, "+5521999399457", TipoDeConta.CONTA_CORRENTE)

        with(response) {
            assertEquals(CLIENTE_ID.toString(), clienteId)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `deve registrar uma nova chave pix do tipo email valido`() {

        `when`(itauClient.buscaContaPorTipo(clienteId = CLIENTE_ID.toString(), tipo = TipoDeConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))

        val response = registraChavePixRequest(TipoDeChave.EMAIL, "teste@gmail.com", TipoDeConta.CONTA_CORRENTE)

        with(response) {
            assertEquals(CLIENTE_ID.toString(), clienteId)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `deve registrar uma nova chave pix aleatoria`() {

        `when`(itauClient.buscaContaPorTipo(clienteId = CLIENTE_ID.toString(), tipo = TipoDeConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))

        val response = registraChavePixRequest(TipoDeChave.ALEATORIA, "", TipoDeConta.CONTA_CORRENTE)

        with(response) {
            assertEquals(CLIENTE_ID.toString(), clienteId)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `nao deve registrar chave pix quando chave existente`() {

        repository.save(chave(
            tipo = CPF,
            chave = "46196026046",
            clienteId = CLIENTE_ID
        ))

        val excecao = assertThrows<StatusRuntimeException> {
            registraChavePixRequest(TipoDeChave.CPF, "46196026046", TipoDeConta.CONTA_CORRENTE)
        }

        with(excecao) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave Pix '46196026046' existente", status.description)

        }
    }

    @Test
    fun `nao deve registrar chave pix quando o cliente nao for encontrado`() {

        `when`(itauClient.buscaContaPorTipo(clienteId = CLIENTE_ID.toString(), tipo = TipoDeConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.notFound())

        val excecao = assertThrows<StatusRuntimeException> {
            registraChavePixRequest(TipoDeChave.CPF, "46196026046", TipoDeConta.CONTA_CORRENTE)
        }

        with(excecao) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Cliente não encontrado no Itau", status.description)
        }

    }

    @Test
    fun `nao deve registrar chave pix quando os dados forem invalidos`() {

        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.regitra(RegistraChavePixRequest.newBuilder().build())
        }

        with(excecao) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
        }

    }

    @MockBean(ItauClient::class)
    fun itauClient() : ItauClient? {
        return Mockito.mock(ItauClient::class.java)
    }

    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) : KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub? {
            return KeyManagerRegistraServiceGrpc.newBlockingStub(channel)
        }
    }

    private fun dadosDaContaResponse(): DadosDaContaResponse {

        return DadosDaContaResponse(
            tipo = "CONTA_CORRENTE",
            instituicao = InstituicaoResponse("UNIBANCO ITAU SA", "ITAU_UNIBANCO_ISPB"),
            agencia = "1452",
            numero = "5555",
            titular = TitularResponse("Usuario Teste", "63657520325")
        )

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
                cpfTitular = "46196026046"
            )
        )
    }

    private fun registraChavePixRequest(
        tipoDeChave: TipoDeChave,
        chave: String,
        tipoDeConta: TipoDeConta
    ) = grpcClient.regitra(
        RegistraChavePixRequest.newBuilder()
            .setClienteId(CLIENTE_ID.toString())
            .setTipoDeChave(tipoDeChave)
            .setChave(chave)
            .setTipoDeConta(tipoDeConta)
            .build()
    )

}


