package br.com.zup.pix.chave.remove

import br.com.zup.KeyManagerRemoveServiceGrpc
import br.com.zup.RemoveChavePixRequest
import br.com.zup.RemoveChavePixResponse
import br.com.zup.pix.excecoes.handler.ErrorHandler
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton

@ErrorHandler
@Singleton
class RemoveChavePixEndpoint(@Inject val service: RemoveChavePixService)
    : KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceImplBase() {

    override fun remove(request: RemoveChavePixRequest, responseObserver: StreamObserver<RemoveChavePixResponse>) {

        service.remove(clienteId = request.clienteId, pixId = request.pixId)

        responseObserver.onNext(RemoveChavePixResponse.newBuilder()
                                    .setClienteId(request.clienteId)
                                    .setPixId(request.pixId)
                                    .build())
        responseObserver.onCompleted()
    }
}