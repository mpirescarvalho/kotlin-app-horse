package com.example.myapplication.models

import com.squareup.moshi.Json

data class Product(

    @field:Json(name = "EMP_CODIGO")
    var empCodigo: Int,

    @field:Json(name = "PRO_CODIGO")
    var proCodigo: Int,

    @field:Json(name = "SUB_DESCRICAO")
    var subDescricao: String?,

    @field:Json(name = "PRO_NOME")
    var proNome: String?,

    @field:Json(name = "PRO_DESCRICAO")
    var proDescricao: String?,

    @field:Json(name = "PRO_PRECO_VENDA")
    var proPrecoVenda: Float?
)