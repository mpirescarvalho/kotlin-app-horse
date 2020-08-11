package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Client(

    @SerializedName("emp_codigo")
    var empCodigo: Int,

    @SerializedName("cli_codigo")
    var cliCodigo: Int,

    @SerializedName("cli_nome")
    var cliNome: String?,

    @SerializedName("cli_fantasia")
    var cliFantasia: String?,

    @SerializedName("cli_cpf")
    var cliCpf: String?,

    @SerializedName("cli_cnpj")
    var cliCnpj: String?,

    @SerializedName("documento")
    var documento: String?,

    @SerializedName("sit_permite_venda")
    var ativoStr: String?,

    @SerializedName("cli_pessoa")
    var pessoaStr: String?,

    @SerializedName("cid_nome")
    var cidNome: String?
) {
    val ativo: Boolean get() = ativoStr == "S"
    val pessoaFisica: Boolean get() = pessoaStr == "F"
    val pessoaJuridica: Boolean get() = pessoaStr == "J"
}