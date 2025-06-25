package com.example.zoo_tour.model.entities

import com.google.gson.annotations.SerializedName

// 館區簡介的資料模型
data class Area(
    @SerializedName("_id") val id: Int,
    @SerializedName("e_no") val eNo: String,
    @SerializedName("e_category") val eCategory: String,
    @SerializedName("e_name") val eName: String,
    @SerializedName("e_pic_url") val ePicUrl: String?,
    @SerializedName("e_info") val eInfo: String,
    @SerializedName("e_memo") val eMemo: String?,
    @SerializedName("e_geo") val eGeo: String,
    @SerializedName("e_url") val eUrl: String
)