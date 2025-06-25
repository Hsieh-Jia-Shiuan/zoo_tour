package com.example.zoo_tour.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.zoo_tour.model.entities.Animal
import com.example.zoo_tour.model.entities.Plant
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.util.NetworkResult
import com.example.zoo_tour.viewModel.ExhibitDataViewModel
import com.example.zoo_tour.viewModel.ZooViewModelFactory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ExhibitDataScreen(
    navController: NavController,
    repository: ZooRepository,
    areaName: String?
) {
    val viewModel: ExhibitDataViewModel = viewModel(
        factory = ZooViewModelFactory(repository)
    )

    // 監聽動物和植物的狀態
    val animalsState by viewModel.animals.collectAsState()
    val plantsState by viewModel.plants.collectAsState()

    // 首次進入觸發資料載入
    LaunchedEffect(Unit) {
        viewModel.fetchAnimals()
        viewModel.fetchPlants()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(areaName?.let { "$it - 館內資料" } ?: "館內資料清單") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val isLoading =
            animalsState is NetworkResult.Loading || plantsState is NetworkResult.Loading
        val isError = animalsState is NetworkResult.Error || plantsState is NetworkResult.Error
        val errorMessage = (animalsState as? NetworkResult.Error)?.message
            ?: (plantsState as? NetworkResult.Error)?.message ?: "未知錯誤"

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            isError -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("載入失敗: $errorMessage")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            viewModel.fetchAnimals()
                            viewModel.fetchPlants()
                        }) {
                            Text("重試")
                        }
                    }
                }
            }
            // 只有在動物和植物都成功載入後才顯示列表
            animalsState is NetworkResult.Success && plantsState is NetworkResult.Success -> {
                val animals = animalsState.data?.filter { it.aLocation == areaName } ?: emptyList()
                val plants = plantsState.data?.filter { it.location == areaName } ?: emptyList()

                if (animals.isNotEmpty() || plants.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        // 顯示動物列表
                        if (animals.isNotEmpty()) {
                            item {
                                Text(
                                    "動物",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(animals) { animal ->
                                AnimalListItem(animal = animal)
                                HorizontalDivider()
                            }
                        } else {
                            item {
                                Text(
                                    areaName?.let { "在 '$it' 館區沒有找到動物資料。" }
                                        ?: "沒有動物資料。",
                                    modifier = Modifier.padding(16.dp))
                            }
                        }

                        // 如果同時有動物和植物，加一個分隔
                        if (animals.isNotEmpty() && plants.isNotEmpty()) {
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }

                        // 顯示植物列表
                        if (plants.isNotEmpty()) {
                            item {
                                Text(
                                    "植物",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(plants) { plant ->
                                PlantListItem(plant = plant) // 使用 PlantListItem
                                HorizontalDivider()
                            }
                        } else {
                            item {
                                Text(
                                    areaName?.let { "在 '$it' 館區沒有找到植物資料。" }
                                        ?: "沒有植物資料。",
                                    modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                } else {
                    // 動物和植物資料都為空的情況
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(areaName?.let { "在 '$it' 館區沒有找到動物或植物資料。" }
                            ?: "沒有動物或植物資料。")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimalListItem(animal: Animal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val rawImageUrl =
                animal.aPic01Url ?: animal.aPic02Url ?: animal.aPic03Url ?: animal.aPic04Url
            val securedImageUrl = rawImageUrl?.replace("http://", "https://")

            securedImageUrl?.let { url ->
                GlideImage(
                    model = url,
                    contentDescription = animal.aNameCh,
                    modifier = Modifier
                        .size(90.dp)
                        .weight(0.3f),
                    contentScale = ContentScale.Crop,
                )
            } ?: run {
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
                Text(text = animal.aNameCh, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "學名: ${animal.aNameLatin}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "棲息地: ${animal.aHabitat}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "位置: ${animal.aLocation}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlantListItem(plant: Plant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val rawImageUrl = plant.pic01Url ?: plant.pic02Url ?: plant.pic03Url ?: plant.pic04Url
            val securedImageUrl = rawImageUrl?.replace("http://", "https://")

            securedImageUrl?.let { url ->
                GlideImage(
                    model = url,
                    contentDescription = plant.nameChinese,
                    modifier = Modifier
                        .size(90.dp)
                        .weight(0.3f),
                    contentScale = ContentScale.Crop,
                )
            } ?: run {
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
                Text(text = plant.nameChinese, style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "學名: ${plant.nameLatin}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "簡介: ${plant.brief}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "位置: ${plant.location}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}