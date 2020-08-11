package com.example.myapplication.models

import com.squareup.moshi.Json

data class Product(

    @Json(name = "EMP_CODIGO")
    var empCodigo: Int,

    @Json(name = "PRO_CODIGO")
    var proCodigo: Int,

    @Json(name = "SUB_DESCRICAO")
    var subDescricao: String?,

    @Json(name = "PRO_NOME")
    var proNome: String?,

    @Json(name = "PRO_DESCRICAO")
    var proDescricao: String?,

    @Json(name = "PRO_PRECO_VENDA")
    var proPrecoVenda: Float?
)