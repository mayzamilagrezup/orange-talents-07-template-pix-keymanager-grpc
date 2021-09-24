package br.com.zup.pix.chave.registra

import br.com.zup.KeyManagerRegistraServiceGrpc
import br.com.zup.RegistraChavePixRequest
import br.com.zup.RegistraChavePixResponse
import br.com.zup.pix.excecoes.handler.ErrorHandler
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton

@ErrorHandler
@Singleton
class RegistraChavePixEndpoint(@Inject val service: NovaChavePixService) : KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceImplBase() {

    override fun regitra(
        request: RegistraChavePixRequest,
        responseObserver: StreamObserver<RegistraChavePixResponse>
    ) {
        val novaChave = request.paraNovaChavePixRequest()
        val chaveCriada = service.registra(novaChave)
        responseObserver.onNext(RegistraChavePixResponse.newBuilder()
            .setClienteId(chaveCriada.clienteId.toString())
            .setPixId(chaveCriada.id.toString())
            .build()
        )

        responseObserver.onCompleted()
    }
}