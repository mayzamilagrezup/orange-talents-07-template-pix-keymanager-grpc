package br.com.zup.pix.clientes.bcb

import br.com.zup.pix.clientes.bcb.request.CriaChavePixRequest
import br.com.zup.pix.clientes.bcb.request.DeletaChaveRequest
import br.com.zup.pix.clientes.bcb.response.BcbCriaChavePixResponse
import br.com.zup.pix.clientes.bcb.response.DeletaChaveResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${bcb.pix.url}")
interface BancoCentralClient {

    @Post("/api/v1/pix/keys")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    fun registra(@Body request: CriaChavePixRequest) : HttpResponse<BcbCriaChavePixResponse>

    @Delete("/api/v1/pix/keys/{key}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    fun deleta(@PathVariable key: String, @Body request: DeletaChaveRequest): HttpResponse<DeletaChaveResponse>
}