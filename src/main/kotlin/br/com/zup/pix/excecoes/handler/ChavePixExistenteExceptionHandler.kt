package br.com.zup.pix.excecoes.handler

import br.com.zup.pix.excecoes.ChavePixExistenteException
import br.com.zup.pix.excecoes.handler.ExceptionHandler.StatusWithDetails
import io.grpc.Status
import jakarta.inject.Singleton

@Singleton
class ChavePixExistenteExceptionHandler : ExceptionHandler<ChavePixExistenteException> {

    override fun handle(e: ChavePixExistenteException): StatusWithDetails {
        return StatusWithDetails(
            Status.ALREADY_EXISTS
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ChavePixExistenteException
    }
}