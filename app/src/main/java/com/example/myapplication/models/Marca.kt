package com.example.myapplication.models

import com.squareup.moshi.Json

data class Marca(

    @Json(name = "MAR_CODIGO")
    var marCodigo: Int = 0,

    @Json(name = "MAR_DESCRICAO")
    var marDescricao: String?
)