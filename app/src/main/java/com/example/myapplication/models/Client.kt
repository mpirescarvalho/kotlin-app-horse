package com.example.myapplication.models

import com.squareup.moshi.Json

data class Client(

    @Json(name = "EMP_CODIGO")
    var empCodigo: Int,

    @Json(name = "CLI_CODIGO")
    var cliCodigo: Int,

    @Json(name = "CLI_NOME")
    var cliNome: String?,

    @Json(name = "CLI_FANTASIA")
    var cliFantasia: String?,

    @Json(name = "CLI_CPF")
    var cliCpf: String?,

    @Json(name = "CLI_CNPJ")
    var cliCnpj: String?,

    @Json(name = "DOCUMENTO")
    var documento: String?,

    @Json(name = "SIT_PERMITE_VENDA")
    var ativoStr: String?,

    @Json(name = "CLI_PESSOA")
    var pessoaStr: String?,

    @Json(name = "CID_NOME")
    var cidNome: String?
) {
    val ativo: Boolean get() = ativoStr == "S"
    val pessoaFisica: Boolean get() = pessoaStr == "F"
    val pessoaJuridica: Boolean get() = pessoaStr == "J"
}