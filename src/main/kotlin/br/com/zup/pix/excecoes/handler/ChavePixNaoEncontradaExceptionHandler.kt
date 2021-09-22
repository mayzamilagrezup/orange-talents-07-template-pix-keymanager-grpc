package br.com.zup.pix.excecoes.handler

import br.com.zup.pix.excecoes.ChavePixNaoEncontradaException
import br.com.zup.pix.excecoes.handler.ExceptionHandler.StatusWithDetails
import io.grpc.Status
import jakarta.inject.Singleton

@Singleton
class ChavePixNaoEncontradaExceptionHandler : ExceptionHandler<ChavePixNaoEncontradaException> {

    override fun handle(e: ChavePixNaoEncontradaException): StatusWithDetails {
        return StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ChavePixNaoEncontradaException
    }
}