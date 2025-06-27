package com.example.zoo_tour.model.entities

import com.google.gson.annotations.SerializedName

// 館區簡介的資料模型
data class Area(
    @SerializedName("_id") val id: Int?,
    @SerializedName("e_no") val no: String?,
    @SerializedName("e_category") val category: String?,
    @SerializedName("e_name") val name: String?,
    @SerializedName("e_pic_url") val picUrl: String?,
    @SerializedName("e_info") val info: String?,
    @SerializedName("e_memo") val memo: String?,
    @SerializedName("e_geo") val geo: String?,
    @SerializedName("e_url") val url: String?
)