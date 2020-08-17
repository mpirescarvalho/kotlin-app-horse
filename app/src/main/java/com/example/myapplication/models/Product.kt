package com.example.myapplication.models

import com.example.myapplication.util.Memoria
import com.google.gson.annotations.SerializedName

data class Product(

    @SerializedName("emp_codigo")
    var empCodigo: Int,

    @SerializedName("pro_codigo")
    var proCodigo: Int,

    @SerializedName("sub_descricao")
    var subDescricao: String?,

    @SerializedName("pro_nome")
    var proNome: String?,

    @SerializedName("pro_descricao")
    var proDescricao: String?,

    @SerializedName("pro_preco_venda")
    var proPrecoVenda: Float?
) {
    val imgUrl: String get() = "${Memoria.APP_URL}imagens/${proCodigo}"
}