package com.example.zoo_tour.model.entities

import android.os.Parcelable

interface ExhibitItem : Parcelable {
    val id: Int?
    val importDate: ImportDate?
    val name: String?
    val summary: String?
    val keywords: String?
    val alsoKnown: String?
    val geo: String?
    val location: String?
    val nameEnglish: String?
    val nameLatin: String?
    val family: String?
    val brief: String?
    val feature: String?
    val code: String?

    val pic01Url: String?
    val pic01Alt: String?
    val pic02Url: String?
    val pic02Alt: String?
    val pic03Url: String?
    val pic03Alt: String?
    val pic04Url: String?
    val pic04Alt: String?

    val pdf01Url: String?
    val pdf01Alt: String?
    val pdf02Url: String?
    val pdf02Alt: String?

    val voice01Url: String?
    val voice01Alt: String?
    val voice02Url: String?
    val voice02Alt: String?
    val voice03Url: String?
    val voice03Alt: String?

    val vedioUrl: String?
    val update: String?
    val cid: String?

    val imageUrls: List<String>
        get() = listOfNotNull(pic01Url, pic02Url, pic03Url, pic04Url)
}