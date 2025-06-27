package com.example.zoo_tour.view.exhibitData

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.zoo_tour.R
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.ui.theme.ProjectColor
import com.example.zoo_tour.ui.theme.ProjectTextStyle
import com.example.zoo_tour.util.NetworkResult
import com.example.zoo_tour.viewModel.ExhibitDataViewModel
import com.example.zoo_tour.viewModel.ZooViewModelFactory
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ExhibitDataScreen(
    navController: NavController,
    repository: ZooRepository,
    area: Area,
) {
    // 取得 viewModel
    val viewModel: ExhibitDataViewModel = viewModel(
        factory = ZooViewModelFactory(repository)
    )

    // 收集動物以及植物狀態
    val animalsState by viewModel.animals.collectAsState()
    val plantsState by viewModel.plants.collectAsState()

    val areaNameString: String = area.name ?: stringResource(R.string.exhibit_data_title)

    val gson = Gson()

    LaunchedEffect(Unit) {
        viewModel.fetchAnimals()
        viewModel.fetchPlants()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = areaNameString,
                        style = ProjectTextStyle.H5,
                        color = ProjectColor.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
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
            ?: (plantsState as? NetworkResult.Error)?.message ?: "Unknown error"

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
                        Text(
                            "Error: $errorMessage",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            viewModel.fetchAnimals()
                            viewModel.fetchPlants()
                        }) {
                            Text(
                                text = stringResource(R.string.reload),
                            )
                        }
                    }
                }
            }

            // 只有在動物和植物都成功載入後才顯示列表
            animalsState is NetworkResult.Success && plantsState is NetworkResult.Success -> {
                val animals = animalsState.data?.filter {
                    it.location?.contains(areaNameString, ignoreCase = true) == true
                } ?: emptyList()

                val plants = plantsState.data?.filter {
                    it.location?.contains(areaNameString, ignoreCase = true) == true
                } ?: emptyList()

                if (animals.isNotEmpty() || plants.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        item {
                            Column {
                                // 載入圖片
                                // 因為url回的資料會有http://開頭，會讀不到圖片
                                val securedImageUrl = area.picUrl?.replace("http://", "https://")

                                securedImageUrl?.let { url ->
                                    GlideImage(
                                        model = url,
                                        contentDescription = area.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(16f / 9f),
                                        contentScale = ContentScale.Crop,
                                        loading = placeholder {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(16f / 9f)
                                                    .background(Color.LightGray),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(
                                                        50.dp
                                                    )
                                                )
                                            }
                                        },
                                        failure = placeholder {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(16f / 9f)
                                                    .background(Color.LightGray),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = "Pic not found",
                                                    modifier = Modifier.size(50.dp),
                                                    tint = ProjectColor.Red,
                                                )
                                            }
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // 館內資訊
                                InfoSection(
                                    content = area.info
                                )

                                // 休館時間及票價資訊
                                InfoSection(
                                    content = area.memo
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    // 類別
                                    InfoSection(
                                        content = area.category
                                    )

                                    // web連結
                                    if (!area.url.isNullOrBlank()) {
                                        Text(
                                            text = stringResource(R.string.exhibit_data_web_view),
                                            style = ProjectTextStyle.H8.copy(
                                                textDecoration = TextDecoration.Underline
                                            ),
                                            color = Color.Blue,
                                            modifier = Modifier
                                                .padding(horizontal = 16.dp)
                                                .clickable {
                                                    navController.navigate(
                                                        "webview/${
                                                            Uri.encode(
                                                                area.url
                                                            )
                                                        }"
                                                    )
                                                }
                                        )
                                    }
                                }
                            }
                        }

                        // 顯示動物列表
                        if (animals.isNotEmpty()) {
                            item {
                                Text(
                                    text = stringResource(R.string.exhibit_data_animals),
                                    modifier = Modifier.padding(16.dp),
                                    style = ProjectTextStyle.H6,
                                    color = ProjectColor.Black
                                )
                            }
                            items(animals) { animal ->
                                ExhibitListItem(item = animal) {
                                    // 將動物資料傳給下一頁
                                    navController.navigate(
                                        "information_detail/animal/${
                                            Uri.encode(
                                                gson.toJson(animal)
                                            )
                                        }"
                                    )
                                }
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
                                    text = stringResource(R.string.exhibit_data_plants),
                                    modifier = Modifier.padding(16.dp),
                                    style = ProjectTextStyle.H6,
                                    color = ProjectColor.Black
                                )
                            }
                            items(plants) { plant ->
                                ExhibitListItem(item = plant) {
                                    // 將植物資料傳給下一頁
                                    navController.navigate(
                                        "information_detail/plant/${
                                            Uri.encode(
                                                gson.toJson(plant)
                                            )
                                        }"
                                    )
                                }
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
                        Text(
                            text = stringResource(
                                R.string.exhibit_data_all_empty,
                                areaNameString
                            ),
                            modifier = Modifier.padding(16.dp),
                            style = ProjectTextStyle.H7,
                            color = ProjectColor.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoSection(
    modifier: Modifier = Modifier,
    content: String?,
    contentStyle: TextStyle = ProjectTextStyle.H8,
) {
    if (content.isNullOrEmpty()) {
        return
    }

    Text(
        text = content,
        style = contentStyle,
        color = ProjectColor.Black,
        modifier = modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))
}