package br.com.zup.pix.anotacoes

import br.com.zup.pix.chave.registra.NovaChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

@Singleton
class ValidPixKeyValidator : ConstraintValidator<ValidPixKey, NovaChavePixRequest> {

    override fun isValid(
        value: NovaChavePixRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean {

        if (value?.tipoDeChave == null) {
            return false
        }


        return value.tipoDeChave.valida(value.chave)
    }

}
