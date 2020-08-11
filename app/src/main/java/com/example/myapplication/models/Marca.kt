package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Marca(

    @SerializedName("mar_codigo")
    var marCodigo: Int = 0,

    @SerializedName("mar_descricao")
    var marDescricao: String?
)