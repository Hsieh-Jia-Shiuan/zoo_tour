package com.example.zoo_tour.view.exhibitData

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
    val viewModel: ExhibitDataViewModel = viewModel(factory = ZooViewModelFactory(repository))
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
        val isLoading = animalsState is NetworkResult.Loading || plantsState is NetworkResult.Loading
        val isError = animalsState is NetworkResult.Error || plantsState is NetworkResult.Error
        val errorMessage = (animalsState as? NetworkResult.Error)?.message
            ?: (plantsState as? NetworkResult.Error)?.message ?: "Unknown error"

        when {
            isLoading -> {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    val (progressRef) = createRefs()
                    CircularProgressIndicator(
                        modifier = Modifier.constrainAs(progressRef) {
                            centerTo(parent)
                        }
                    )
                }
            }

            isError -> {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    val (colRef) = createRefs()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.constrainAs(colRef) {
                            centerTo(parent)
                        }
                    ) {
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
                            Text(text = stringResource(R.string.reload))
                        }
                    }
                }
            }

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
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                val (
                                    imageRef, infoRef, memoRef, rowRef
                                ) = createRefs()

                                // 圖片
                                val securedImageUrl = area.picUrl?.replace("http://", "https://")
                                securedImageUrl?.let { url ->
                                    GlideImage(
                                        model = url,
                                        contentDescription = area.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(16f / 9f)
                                            .constrainAs(imageRef) {
                                                top.linkTo(parent.top)
                                                start.linkTo(parent.start)
                                                end.linkTo(parent.end)
                                            },
                                        contentScale = ContentScale.Crop,
                                        loading = placeholder {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(16f / 9f)
                                                    .background(Color.LightGray),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(modifier = Modifier.size(50.dp))
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

                                // 館內資訊
                                InfoSection(
                                    content = area.info,
                                    modifier = Modifier.constrainAs(infoRef) {
                                        top.linkTo(imageRef.bottom, margin = 16.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        width = Dimension.fillToConstraints
                                    }
                                )

                                // 休館時間及票價資訊
                                InfoSection(
                                    content = area.memo,
                                    modifier = Modifier.constrainAs(memoRef) {
                                        top.linkTo(infoRef.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        width = Dimension.fillToConstraints
                                    }
                                )

                                val hasMemo = !area.memo.isNullOrEmpty()

                                // 分類與 web 連結
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .constrainAs(rowRef) {
                                            top.linkTo(if (hasMemo) memoRef.bottom else infoRef.bottom)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                        },
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    InfoSection(
                                        content = area.category,
                                        modifier = Modifier.weight(1f)
                                    )
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
                                                        "webview/${Uri.encode(area.url)}"
                                                    )
                                                }
                                        )
                                    }
                                }
                            }
                        }

                        // 動物列表
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
                                    navController.navigate(
                                        "information_detail/animal/${Uri.encode(gson.toJson(animal))}"
                                    )
                                }
                            }
                        }

                        // 分隔
                        if (animals.isNotEmpty() && plants.isNotEmpty()) {
                            item {
                                ConstraintLayout(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val (spacerRef) = createRefs()
                                    Box(
                                        modifier = Modifier
                                            .height(16.dp)
                                            .constrainAs(spacerRef) {
                                                top.linkTo(parent.top)
                                                start.linkTo(parent.start)
                                                end.linkTo(parent.end)
                                            }
                                    )
                                }
                            }
                        }

                        // 植物列表
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
                                    navController.navigate(
                                        "information_detail/plant/${Uri.encode(gson.toJson(plant))}"
                                    )
                                }
                            }
                        }
                    }
                } else {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        val (textRef) = createRefs()
                        Text(
                            text = stringResource(
                                R.string.exhibit_data_all_empty,
                                areaNameString
                            ),
                            modifier = Modifier
                                .padding(16.dp)
                                .constrainAs(textRef) {
                                    centerTo(parent)
                                },
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
    if (content.isNullOrEmpty()) return

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        val (contentRef) = createRefs()
        Text(
            text = content,
            style = contentStyle,
            color = ProjectColor.Black,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(contentRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
    }
}