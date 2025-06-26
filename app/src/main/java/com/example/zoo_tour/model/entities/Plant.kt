package com.example.zoo_tour.model.entities

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plant(
    @SerializedName("_id") override val id: Int,
    @SerializedName("_importdate") override val importDate: ImportDate,
    @SerializedName("f_name_ch") val nameChinese: String,
    @SerializedName("f_summary") override val summary: String?,
    @SerializedName("f_keywords") override val keywords: String?,
    @SerializedName("f_alsoknown") override val alsoKnown: String?,
    @SerializedName("f_geo") override val geo: String,
    @SerializedName("f_location") override val location: String,
    @SerializedName("f_name_en") override val nameEnglish: String?,
    @SerializedName("f_name_latin") override val nameLatin: String,
    @SerializedName("f_family") override val family: String,
    @SerializedName("f_genus") val genus: String?,
    @SerializedName("f_brief") override val brief: String?,
    @SerializedName("f_feature") override val feature: String,
    @SerializedName("f_function＆application") val functionApplicationRaw: String?,
    @SerializedName("f_code") override val code: String?,
    @SerializedName("f_pic01_url") override val pic01Url: String?,
    @SerializedName("f_pic01_alt") override val pic01Alt: String?,
    @SerializedName("f_pic02_url") override val pic02Url: String?,
    @SerializedName("f_pic02_alt") override val pic02Alt: String?,
    @SerializedName("f_pic03_url") override val pic03Url: String?,
    @SerializedName("f_pic03_alt") override val pic03Alt: String?,
    @SerializedName("f_pic04_url") override val pic04Url: String?,
    @SerializedName("f_pic04_alt") override val pic04Alt: String?,
    @SerializedName("f_pdf01_url") override val pdf01Url: String?,
    @SerializedName("f_pdf01_alt") override val pdf01Alt: String?,
    @SerializedName("f_pdf02_url") override val pdf02Url: String?,
    @SerializedName("f_pdf02_alt") override val pdf02Alt: String?,
    @SerializedName("f_voice01_url") override val voice01Url: String?,
    @SerializedName("f_voice01_alt") override val voice01Alt: String?,
    @SerializedName("f_voice02_url") override val voice02Url: String?,
    @SerializedName("f_voice02_alt") override val voice02Alt: String?,
    @SerializedName("f_voice03_url") override val voice03Url: String?,
    @SerializedName("f_voice03_alt") override val voice03Alt: String?,
    @SerializedName("f_vedio_url") override val vedioUrl: String?,
    @SerializedName("f_update") override val update: String?,
    @SerializedName("f_cid") override val cid: String?
) : ExhibitItem {
    override val name: String
        get() = nameChinese
}