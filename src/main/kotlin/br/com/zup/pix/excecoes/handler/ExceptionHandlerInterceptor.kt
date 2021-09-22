package br.com.zup.pix.excecoes.handler

import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class ExceptionHandlerInterceptor(@Inject val resolver: ExceptionHandlerResolver) : ServerInterceptor {

    private val logger = LoggerFactory.getLogger(ExceptionHandlerInterceptor::class.java)

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>,
    ): ServerCall.Listener<ReqT> {

        fun handleException(call: ServerCall<ReqT, RespT>, e: Exception) {
            logger.error("Handling exception $e while processing the call: ${call.methodDescriptor.fullMethodName}")
            val handler = resolver.resolve(e)
            val translatedStatus = handler.handle(e)
            call.close(translatedStatus.status, translatedStatus.metadata)
        }

        val listener: ServerCall.Listener<ReqT> = try {
            next.startCall(call, headers)
        } catch (ex: Exception) {
            handleException(call, ex)
            throw ex
        }

        return object : SimpleForwardingServerCallListener<ReqT>(listener) {
            override fun onHalfClose() {
                try {
                    super.onHalfClose()
                } catch (ex: Exception) {
                    handleException(call, ex)
                    throw ex
                }
            }

            override fun onReady() {
                try {
                    super.onReady()
                } catch (ex: Exception) {
                    handleException(call, ex)
                    throw ex
                }
            }
        }
    }

}
