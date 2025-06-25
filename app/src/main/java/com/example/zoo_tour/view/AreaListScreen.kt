package com.example.zoo_tour.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.util.NetworkResult
import com.example.zoo_tour.viewModel.AreaListViewModel
import com.example.zoo_tour.viewModel.ZooViewModelFactory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AreaListScreen(
    navController: NavController,
    repository: ZooRepository // 透過參數傳入 repository
) {
    val viewModel: AreaListViewModel = viewModel(
        factory = ZooViewModelFactory(repository)
    )
    val areasState by viewModel.areas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("台北動物園 - 館區") })
        }
    ) { paddingValues ->
        when (areasState) {
            is NetworkResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NetworkResult.Success -> {
                val areas = areasState.data
                if (!areas.isNullOrEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        items(areas) { area ->
                            AreaListItem(area = area) {
                                navController.navigate("exhibit_data/${area.eName}")
                            }
                            HorizontalDivider()
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("沒有館區資料。")
                    }
                }
            }
            is NetworkResult.Error -> {
                val errorMessage = areasState.message ?: "未知錯誤"
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("載入失敗: $errorMessage")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.fetchAreas() }) {
                            Text("重試")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AreaListItem(area: Area, onClick: () -> Unit) {
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 使用 GlideImage 載入圖片
            val securedImageUrl = area.ePicUrl?.replace("http://", "https://")

            securedImageUrl?.let { url ->
                GlideImage(
                    model = url,
                    contentDescription = area.eName,
                    modifier = Modifier
                        .size(90.dp)
                        .weight(0.3f),
                    contentScale = ContentScale.Crop,
                )
            } ?: run {
                // 如果沒有圖片 URL，可以顯示一個佔位符
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .weight(0.3f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(0.7f)) {
                Text(text = area.eName, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = area.eInfo,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3, // 顯示最多三行簡介
                    overflow = TextOverflow.Ellipsis // 超出部分顯示省略號
                )
            }
        }
    }
}