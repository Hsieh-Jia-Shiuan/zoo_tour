package com.example.zoo_tour.view.area

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.ui.theme.ProjectColor
import com.example.zoo_tour.ui.theme.ProjectTextStyle

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AreaListItem(
    area: Area,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val (imageRef, textColRef, arrowRef) = createRefs()

            // 圖片
            val securedImageUrl = area.picUrl?.replace("http://", "https://")
            if (!securedImageUrl.isNullOrEmpty()) {
                GlideImage(
                    model = securedImageUrl,
                    contentDescription = area.name,
                    modifier = Modifier
                        .size(90.dp)
                        .constrainAs(imageRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    contentScale = ContentScale.Crop,
                    loading = placeholder {
                        CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    },
                    failure = placeholder {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Pic not found",
                            modifier = Modifier.size(50.dp),
                            tint = ProjectColor.Red
                        )
                    }
                )
            } else {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .constrainAs(imageRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                )
            }

            // 文字區塊
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(textColRef) {
                        start.linkTo(imageRef.end, margin = 8.dp)
                        end.linkTo(arrowRef.start, margin = 8.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            ) {
                val (nameRef, infoRef, memoRef) = createRefs()
                area.name?.let {
                    Text(
                        text = it,
                        style = ProjectTextStyle.H8,
                        color = ProjectColor.Black,
                        modifier = Modifier.constrainAs(nameRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                    )
                }
                area.info?.let {
                    Text(
                        text = it,
                        style = ProjectTextStyle.H10,
                        color = ProjectColor.Black50,
                        maxLines = if (area.memo.isNullOrEmpty()) 4 else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.constrainAs(infoRef) {
                            top.linkTo(nameRef.bottom, margin = 4.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                    )
                }
                area.memo?.takeIf { it.isNotEmpty() }?.let {
                    Text(
                        text = it,
                        style = ProjectTextStyle.H10,
                        color = ProjectColor.Black50,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.constrainAs(memoRef) {
                            top.linkTo(infoRef.bottom, margin = 4.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                    )
                }
            }

            // 箭頭
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .constrainAs(arrowRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
            )
        }
    }
}

@Preview
@Composable
fun AreaListItemPreview() {
    AreaListItem(
        area = Area(
            id = 1,
            no = "測試編號",
            category = "戶外區",
            name = "臺灣動物區",
            picUrl = "http://www.zoo.gov.tw/iTAP/05_Exhibit/01_FormosanAnimal.jpg",
            info = "臺灣位於北半球，北迴歸線橫越南部，造成亞熱帶溫和多雨的氣候。又因高山急流、起伏多樣的地形，因而在這蕞爾小島上，形成了多樣性的生態系，孕育了多種不同生態習性的動、植物，豐富的生物物種共存共榮於此，也使臺灣博得美麗之島「福爾摩沙」的美名。 臺灣動物區以臺灣原生動物與棲息環境為展示重點，佈置模擬動物原生棲地之生態環境，讓動物表現如野外般自然的生活習性，引導民眾更正確地認識本土野生動物，為園區環境教育的重要據點。藉由提供動物寬廣且具隱蔽的生態環境，讓動物避開人為過度的干擾，並展現如野外般自然的生活習性和行為。展示區以多種臺灣的保育類野生動物貫連成保育廊道，包括臺灣黑熊、穿山甲、歐亞水獺、白鼻心、石虎、山羌等。唯有認識、瞭解本土野生動物，才能愛護、保育牠們，並進而珍惜我們共同生存的這塊土地！",
            memo = "每週一休館，入館門票：全票20元、優待票10元",
            geo = "測試場域位置",
            url = "測試場域網址"
        ),
        onClick = {}
    )
}

@Preview
@Composable
fun AreaListItem_NoMemoPreview() {
    AreaListItem(
        area = Area(
            id = 1,
            no = "測試編號",
            category = "戶外區",
            name = "臺灣動物區",
            picUrl = "",
            info = "臺灣位於北半球，北迴歸線橫越南部，造成亞熱帶溫和多雨的氣候。又因高山急流、起伏多樣的地形，因而在這蕞爾小島上，形成了多樣性的生態系，孕育了多種不同生態習性的動、植物，豐富的生物物種共存共榮於此，也使臺灣博得美麗之島「福爾摩沙」的美名。 臺灣動物區以臺灣原生動物與棲息環境為展示重點，佈置模擬動物原生棲地之生態環境，讓動物表現如野外般自然的生活習性，引導民眾更正確地認識本土野生動物，為園區環境教育的重要據點。藉由提供動物寬廣且具隱蔽的生態環境，讓動物避開人為過度的干擾，並展現如野外般自然的生活習性和行為。展示區以多種臺灣的保育類野生動物貫連成保育廊道，包括臺灣黑熊、穿山甲、歐亞水獺、白鼻心、石虎、山羌等。唯有認識、瞭解本土野生動物，才能愛護、保育牠們，並進而珍惜我們共同生存的這塊土地！",
            memo = "",
            geo = "測試場域位置",
            url = "測試場域網址"
        ),
        onClick = {}
    )
}