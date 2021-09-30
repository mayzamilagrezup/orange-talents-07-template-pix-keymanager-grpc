package br.com.zup.pix.chave.registra

import br.com.zup.pix.chave.ChavePix
import br.com.zup.pix.chave.ChavePixRepository
import br.com.zup.pix.clientes.bcb.BancoCentralClient
import br.com.zup.pix.clientes.bcb.request.CriaChavePixRequest
import br.com.zup.pix.clientes.itau.ItauClient
import br.com.zup.pix.excecoes.ChavePixExistenteException
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChavePixService(
    @Inject val repository: ChavePixRepository,
    @Inject val itauClient: ItauClient,
    @Inject val bcbClient: BancoCentralClient
) {

    val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registra(@Valid novaChaveRequest: NovaChavePixRequest): ChavePix {

        if (repository.existsByChave(novaChaveRequest.chave))
            throw ChavePixExistenteException("Chave Pix '${novaChaveRequest.chave}' existente")

        val response  = itauClient.buscaContaPorTipo(novaChaveRequest.clienteId!!, novaChaveRequest.tipoDeConta!!.name)
        val conta = response.body()?.paraContaAssociada() ?: throw IllegalStateException("Cliente não encontrado no Itau")

        val chave = novaChaveRequest.paraChavePix(conta)
        repository.save(chave)

        val bcbRequest = CriaChavePixRequest(chave)

        val bcbResponse = bcbClient.registra(bcbRequest)

        if (bcbResponse.status != HttpStatus.CREATED) {
            throw java.lang.IllegalStateException("Erro ao tentar registrar chave pix no Banco Central")
        }

        chave.atualiza(bcbResponse.body()!!.key)
        log.info("Chave pix registrada com sucesso para o cliente id ${chave.clienteId}")

        return chave

    }
}
