package br.com.zup.pix.chave.remove

import br.com.zup.pix.anotacoes.ValidUUID
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.clientes.bcb.BancoCentralClient
import br.com.zup.pix.clientes.bcb.request.DeletaChaveRequest
import br.com.zup.pix.excecoes.ChavePixNaoEncontradaException
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import java.util.*
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank


@Validated
@Singleton
class RemoveChavePixService(
    @Inject val repository: ChavePixRepository,
    @Inject val bcbClient: BancoCentralClient
) {

    val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun remove(
        @NotBlank @ValidUUID(message = "cliente id com formato inválido") clienteId: String?,
        @NotBlank @ValidUUID(message = "pix id com formato inválido") pixId: String?
    ) {

        val uuidClienteId = UUID.fromString(clienteId)
        val uuidPixId = UUID.fromString(pixId)

        val chave = repository.findByIdAndClienteId(uuidPixId, uuidClienteId).orElseThrow {
            ChavePixNaoEncontradaException("Chave pix não encontrada ou não pertence ao cliente")
        }

        repository.deleteById(uuidPixId)

        val request = DeletaChaveRequest(chave.chave)

        val bcbResponse = bcbClient.deleta(key = chave.chave, request = request)
        if (bcbResponse.status != HttpStatus.OK) {
            throw IllegalStateException("Erro ao remover chave pix no Banco Central")
        }

        log.info("chave pix de id ${chave.id} pertencente ao cliente de id ${chave.clienteId} removida com sucesso!")
    }
}