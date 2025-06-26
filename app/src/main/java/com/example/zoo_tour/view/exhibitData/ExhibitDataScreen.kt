package com.example.zoo_tour.view.exhibitData

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.zoo_tour.R
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.ui.theme.ProjectColor
import com.example.zoo_tour.ui.theme.ProjectTextStyle
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
    // 取得 viewModel
    val viewModel: ExhibitDataViewModel = viewModel(
        factory = ZooViewModelFactory(repository)
    )

    // 收集動物以及植物狀態
    val animalsState by viewModel.animals.collectAsState()
    val plantsState by viewModel.plants.collectAsState()

    val areaNameString: String = areaName ?: stringResource(R.string.exhibit_data_title)

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
                    it.location.contains(areaName ?: "", ignoreCase = true) == true
                } ?: emptyList()

                val plants = plantsState.data?.filter {
                    it.location.contains(areaName ?: "", ignoreCase = true) == true
                } ?: emptyList()

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
                                    text = stringResource(R.string.exhibit_data_animals),
                                    modifier = Modifier.padding(16.dp),
                                    style = ProjectTextStyle.H6,
                                    color = ProjectColor.Black
                                )
                            }
                            items(animals) { animal ->
                                ExhibitListItem(item = animal) {
                                    // 將動物資料傳給下一頁
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "exhibitItem",
                                        animal
                                    )
                                    navController.navigate("information_detail")
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = stringResource(
                                        R.string.exhibit_data_empty,
                                        areaNameString,
                                        stringResource(R.string.exhibit_data_animals)
                                    ),
                                    modifier = Modifier.padding(16.dp),
                                    style = ProjectTextStyle.H7,
                                    color = ProjectColor.Black
                                )
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
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "exhibitItem",
                                        plant
                                    )
                                    navController.navigate("information_detail")
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = stringResource(
                                        R.string.exhibit_data_empty,
                                        areaNameString,
                                        stringResource(R.string.exhibit_data_plants)
                                    ),
                                    modifier = Modifier.padding(16.dp),
                                    style = ProjectTextStyle.H7,
                                    color = ProjectColor.Black
                                )
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