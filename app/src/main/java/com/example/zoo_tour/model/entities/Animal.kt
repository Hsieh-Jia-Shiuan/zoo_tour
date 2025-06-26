package com.example.zoo_tour.model.entities

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal(
    @SerializedName("_id") override val id: Int,
    @SerializedName("_importdate") override val importDate: ImportDate,
    @SerializedName("a_name_ch") val nameCh: String,
    @SerializedName("a_summary") override val summary: String?,
    @SerializedName("a_keywords") override val keywords: String?,
    @SerializedName("a_alsoknown") override val alsoKnown: String?,
    @SerializedName("a_geo") override val geo: String,
    @SerializedName("a_location") override val location: String,
    @SerializedName("a_poigroup") val pioGroup: String?,
    @SerializedName("a_name_en") override val nameEnglish: String?,
    @SerializedName("a_name_latin") override val nameLatin: String,
    @SerializedName("a_phylum") val phylum: String,
    @SerializedName("a_class") val clazz: String,
    @SerializedName("a_order") val order: String,
    @SerializedName("a_family") override val family: String,
    @SerializedName("a_conservation") val conservation: String?,
    @SerializedName("a_distribution") val distribution: String?,
    @SerializedName("a_habitat") val habitat: String?,
    @SerializedName("a_feature") override val feature: String,
    @SerializedName("a_behavior") val behavior: String?,
    @SerializedName("a_diet") val diet: String?,
    @SerializedName("a_crisis") val crisis: String?,
    @SerializedName("a_interpretation") val interpretation: String?,
    @SerializedName("a_theme_name") val themeName: String?,
    @SerializedName("a_theme_url") val themeUrl: String?,
    @SerializedName("a_adopt") val adopt: String?,
    @SerializedName("a_code") override val code: String?,
    @SerializedName("a_pic01_alt") override val pic01Alt: String?,
    @SerializedName("a_pic01_url") override val pic01Url: String?,
    @SerializedName("a_pic02_alt") override val pic02Alt: String?,
    @SerializedName("a_pic02_url") override val pic02Url: String?,
    @SerializedName("a_pic03_alt") override val pic03Alt: String?,
    @SerializedName("a_pic03_url") override val pic03Url: String?,
    @SerializedName("a_pic04_alt") override val pic04Alt: String?,
    @SerializedName("a_pic04_url") override val pic04Url: String?,
    @SerializedName("a_pdf01_alt") override val pdf01Alt: String?,
    @SerializedName("a_pdf01_url") override val pdf01Url: String?,
    @SerializedName("a_pdf02_alt") override val pdf02Alt: String?,
    @SerializedName("a_pdf02_url") override val pdf02Url: String?,
    @SerializedName("a_voice01_alt") override val voice01Alt: String?,
    @SerializedName("a_voice01_url") override val voice01Url: String?,
    @SerializedName("a_voice02_alt") override val voice02Alt: String?,
    @SerializedName("a_voice02_url") override val voice02Url: String?,
    @SerializedName("a_voice03_alt") override val voice03Alt: String?,
    @SerializedName("a_voice03_url") override val voice03Url: String?,
    @SerializedName("a_vedio_url") override val vedioUrl: String?,
    @SerializedName("a_update") override val update: String?,
    @SerializedName("a_cid") override val cid: String?
) : ExhibitItem {
    override val name: String
        get() = nameCh
    override val brief: String?
        get() = interpretation?.takeIf { it.isNotBlank() }
}