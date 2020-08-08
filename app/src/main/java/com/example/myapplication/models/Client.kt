package com.example.myapplication.models

import com.squareup.moshi.Json

data class Client(

    @field:Json(name = "EMP_CODIGO")
    var empCodigo: Int,

    @field:Json(name = "CLI_CODIGO")
    var cliCodigo: Int,

    @field:Json(name = "CLI_NOME")
    var cliNome: String?,

    @field:Json(name = "CLI_FANTASIA")
    var cliFantasia: String?,

    @field:Json(name = "CLI_CPF")
    var cliCpf: String?,

    @field:Json(name = "CLI_CNPJ")
    var cliCnpj: String?,

    @field:Json(name = "DOCUMENTO")
    var documento: String?,

    @field:Json(name = "SIT_PERMITE_VENDA")
    var ativoStr: String?,

    @field:Json(name = "CLI_PESSOA")
    var pessoaStr: String?,

    @field:Json(name = "CID_NOME")
    var cidNome: String?
) {
    val ativo: Boolean get() = ativoStr == "S"
    val pessoaFisica: Boolean get() = pessoaStr == "F"
    val pessoaJuridica: Boolean get() = pessoaStr == "J"
}