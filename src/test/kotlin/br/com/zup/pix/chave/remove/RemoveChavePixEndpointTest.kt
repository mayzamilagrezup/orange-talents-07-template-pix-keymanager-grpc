package br.com.zup.pix.chave.remove

import br.com.zup.KeyManagerRemoveServiceGrpc
import br.com.zup.RemoveChavePixRequest
import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.chave.ContaAssociada
import br.com.zup.pix.chave.TipoDeChave
import br.com.zup.pix.clientes.bcb.BancoCentralClient
import br.com.zup.pix.clientes.bcb.request.RemoveChaveRequest
import br.com.zup.pix.clientes.bcb.response.RemoveChaveResponse
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
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.LocalDateTime
import java.util.*

@MicronautTest(transactional = false)
internal class RemoveChavePixEndpointTest(
    val repository: ChavePixRepository,
    val grpcClient: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub
) {

    lateinit var CHAVE_EXISTENTE: ChavePix

    @Inject
    lateinit var bcbClient: BancoCentralClient

    @BeforeEach
    fun setup() {
        CHAVE_EXISTENTE = repository.save(chave(
            tipo = TipoDeChave.EMAIL,
            chave = "teste@gmail.com",
            clienteId = UUID.randomUUID()
        ))
    }

    @AfterEach
    fun cleanUp() {
        repository.deleteAll()
    }

    @Test
    fun `deve remover chave pix existente`() {

        val chave = "teste@gmail.com"

        `when`(bcbClient.remove(chave, RemoveChaveRequest(chave)))
            .thenReturn(
                HttpResponse.ok(RemoveChaveResponse(key = chave,
                participant = ContaAssociada.ITAU_UNIBANCO_ISPB,
                deletedAt = LocalDateTime.now()))
            )

        val response = grpcClient.remove(
            RemoveChavePixRequest.newBuilder()
                .setPixId(CHAVE_EXISTENTE.id.toString())
                .setClienteId(CHAVE_EXISTENTE.clienteId.toString())
                .build()
        )

        assertEquals(CHAVE_EXISTENTE.id.toString(), response.pixId)
        assertEquals(CHAVE_EXISTENTE.clienteId.toString(), response.clienteId)
    }

    @Test
    fun `nao deve remover chave pix quando chave nao existente`() {

        val pixIdNaoExistente = UUID.randomUUID().toString()

        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.remove(RemoveChavePixRequest.newBuilder()
                .setPixId(pixIdNaoExistente)
                .setClienteId(CHAVE_EXISTENTE.clienteId.toString())
                .build())
        }

        with(excecao) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave pix não encontrada ou não pertence ao cliente", status.description)
        }
    }

    @Test
    fun `nao deve remover chave pix quando chave existente mas pertencente a outro cliente`() {

        val outroClienteId = UUID.randomUUID().toString()

        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.remove(RemoveChavePixRequest.newBuilder()
                .setPixId(CHAVE_EXISTENTE.id.toString())
                .setClienteId(outroClienteId)
                .build())
        }

        with(excecao) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave pix não encontrada ou não pertence ao cliente", status.description)
        }
    }

    @Test
    fun `nao deve remover chave pix existente quando ocorrer algum erro no serviço do BCB`() {

        val chave = "teste@gmail.com"

        `when`(bcbClient.remove(chave, RemoveChaveRequest(chave)))
            .thenReturn(HttpResponse.unprocessableEntity())

        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.remove(RemoveChavePixRequest.newBuilder()
                .setPixId(CHAVE_EXISTENTE.id.toString())
                .setClienteId(CHAVE_EXISTENTE.clienteId.toString())
                .build())
        }

        with(excecao) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Erro ao tentar remover chave pix no Banco Central", status.description)
        }
    }


    @MockBean(BancoCentralClient::class)
    fun bcbClient(): BancoCentralClient? {
        return Mockito.mock(BancoCentralClient::class.java)
    }

    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) : KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub? {
            return KeyManagerRemoveServiceGrpc.newBlockingStub(channel)
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
                cpfTitular = "46196026046"
            )
        )
    }

}