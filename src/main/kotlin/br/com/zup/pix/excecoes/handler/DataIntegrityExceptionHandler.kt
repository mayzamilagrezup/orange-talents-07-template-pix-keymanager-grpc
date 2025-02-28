package br.com.zup.pix.excecoes.handler

import br.com.zup.pix.excecoes.handler.ExceptionHandler.StatusWithDetails
import io.grpc.Status
import io.micronaut.context.MessageSource
import io.micronaut.context.MessageSource.MessageContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.hibernate.exception.ConstraintViolationException

@Singleton
class DataIntegrityExceptionHandler(@Inject var messageSource: MessageSource) : ExceptionHandler<ConstraintViolationException> {

    override fun handle(e: ConstraintViolationException): StatusWithDetails {

        val constraintName = e.constraintName
        if (constraintName.isNullOrBlank()) {
            return internalServerError(e)
        }

        val message = messageSource.getMessage("data.integrity.error.$constraintName", MessageContext.DEFAULT)
        return message
            .map { alreadyExistsError(it, e) }
            .orElse(internalServerError(e))
    }

    override fun supports(e: Exception): Boolean {
        return e is ConstraintViolationException
    }

    private fun alreadyExistsError(message: String?, e: ConstraintViolationException) =
        StatusWithDetails(
            Status.ALREADY_EXISTS
                .withDescription(message)
                .withCause(e)
        )

    private fun internalServerError(e: ConstraintViolationException) =
        StatusWithDetails(
            Status.INTERNAL
                .withDescription("Unexpected internal server error")
                .withCause(e)
        )
}