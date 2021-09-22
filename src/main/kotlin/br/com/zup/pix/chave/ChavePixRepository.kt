package br.com.zup.pix.chave

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, Long> {

    fun existsByChave(chave: String?): Boolean

}
