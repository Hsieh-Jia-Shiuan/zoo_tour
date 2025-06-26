package com.example.zoo_tour.view.area

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 載入圖片
            // 因為url回的資料會有http://開頭，會讀不到圖片
            val securedImageUrl = area.ePicUrl?.replace("http://", "https://")

            securedImageUrl?.let { url ->
                GlideImage(
                    model = url,
                    contentDescription = area.eName,
                    modifier = Modifier.size(90.dp),
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
            } ?: run {
                // 如果沒有圖片，顯示一個佔位符
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp),
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = area.eName,
                    style = ProjectTextStyle.H8,
                    color = ProjectColor.Black
                )

                Text(
                    text = area.eInfo,
                    style = ProjectTextStyle.H10,
                    color = ProjectColor.Black50,
                    maxLines = if (area.eMemo.isNullOrEmpty()) 4 else 2,
                    overflow = TextOverflow.Ellipsis
                )

                // 休館時間及票價資訊
                if (area.eMemo != null && area.eMemo.isNotEmpty()) {
                    Text(
                        text = area.eMemo,
                        style = ProjectTextStyle.H10,
                        color = ProjectColor.Black50,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

            Spacer(modifier = Modifier.width(8.dp))

            // 箭頭
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
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
            eNo = "測試編號",
            eCategory = "戶外區",
            eName = "臺灣動物區",
            ePicUrl = "http://www.zoo.gov.tw/iTAP/05_Exhibit/01_FormosanAnimal.jpg",
            eInfo = "臺灣位於北半球，北迴歸線橫越南部，造成亞熱帶溫和多雨的氣候。又因高山急流、起伏多樣的地形，因而在這蕞爾小島上，形成了多樣性的生態系，孕育了多種不同生態習性的動、植物，豐富的生物物種共存共榮於此，也使臺灣博得美麗之島「福爾摩沙」的美名。 臺灣動物區以臺灣原生動物與棲息環境為展示重點，佈置模擬動物原生棲地之生態環境，讓動物表現如野外般自然的生活習性，引導民眾更正確地認識本土野生動物，為園區環境教育的重要據點。藉由提供動物寬廣且具隱蔽的生態環境，讓動物避開人為過度的干擾，並展現如野外般自然的生活習性和行為。展示區以多種臺灣的保育類野生動物貫連成保育廊道，包括臺灣黑熊、穿山甲、歐亞水獺、白鼻心、石虎、山羌等。唯有認識、瞭解本土野生動物，才能愛護、保育牠們，並進而珍惜我們共同生存的這塊土地！",
            eMemo = "每週一休館，入館門票：全票20元、優待票10元",
            eGeo = "測試場域位置",
            eUrl = "測試場域網址"
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
            eNo = "測試編號",
            eCategory = "戶外區",
            eName = "臺灣動物區",
            ePicUrl = "",
            eInfo = "臺灣位於北半球，北迴歸線橫越南部，造成亞熱帶溫和多雨的氣候。又因高山急流、起伏多樣的地形，因而在這蕞爾小島上，形成了多樣性的生態系，孕育了多種不同生態習性的動、植物，豐富的生物物種共存共榮於此，也使臺灣博得美麗之島「福爾摩沙」的美名。 臺灣動物區以臺灣原生動物與棲息環境為展示重點，佈置模擬動物原生棲地之生態環境，讓動物表現如野外般自然的生活習性，引導民眾更正確地認識本土野生動物，為園區環境教育的重要據點。藉由提供動物寬廣且具隱蔽的生態環境，讓動物避開人為過度的干擾，並展現如野外般自然的生活習性和行為。展示區以多種臺灣的保育類野生動物貫連成保育廊道，包括臺灣黑熊、穿山甲、歐亞水獺、白鼻心、石虎、山羌等。唯有認識、瞭解本土野生動物，才能愛護、保育牠們，並進而珍惜我們共同生存的這塊土地！",
            eMemo = "",
            eGeo = "測試場域位置",
            eUrl = "測試場域網址"
        ),
        onClick = {}
    )
}