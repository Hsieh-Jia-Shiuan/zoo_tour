package com.example.zoo_tour.view.area

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.zoo_tour.R
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.ui.theme.ProjectColor
import com.example.zoo_tour.ui.theme.ProjectTextStyle
import com.example.zoo_tour.util.NetworkResult
import com.example.zoo_tour.viewModel.AreaListViewModel
import com.example.zoo_tour.viewModel.ZooViewModelFactory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AreaListScreen(
    navController: NavController,
    repository: ZooRepository
) {
    // 取得 viewModel
    val viewModel: AreaListViewModel = viewModel(
        factory = ZooViewModelFactory(repository)
    )

    // 收集管區狀態
    val areasState by viewModel.areas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.area_list_title),
                        style = ProjectTextStyle.H5,
                        color = ProjectColor.Black
                    )
                }
            )
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(areas) { area ->
                            // 前往館區資料
                            AreaListItem(area = area) {
                                navController.navigate("exhibit_data/${area.eName}")
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.area_list_no_data),
                            style = ProjectTextStyle.H8,
                            color = ProjectColor.Black
                        )
                    }
                }
            }

            is NetworkResult.Error -> {
                val errorMessage = areasState.message ?: "Unknown error"
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

                        Button(onClick = { viewModel.fetchAreas() }) {
                            Text(
                                text = stringResource(R.string.reload),
                            )
                        }
                    }
                }
            }
        }
    }
}