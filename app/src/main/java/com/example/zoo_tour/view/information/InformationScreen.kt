package com.example.zoo_tour.view.information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.zoo_tour.R
import com.example.zoo_tour.model.entities.Animal
import com.example.zoo_tour.model.entities.ExhibitItem
import com.example.zoo_tour.model.entities.Plant
import com.example.zoo_tour.ui.theme.ProjectColor
import com.example.zoo_tour.ui.theme.ProjectTextStyle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun InformationScreen(
    navController: NavController,
    exhibitItem: ExhibitItem?
) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(exhibitItem) {
        if (exhibitItem != null) {
            isLoading = false
            errorMessage = null
        } else {
            isLoading = false
            errorMessage = "no data"
        }
    }

    // 獲取安全的圖片網址
    val securedImageUrls = remember(exhibitItem) {
        exhibitItem?.imageUrls
            ?.filter { it.isNotBlank() }
            ?.map { it.replace("http://", "https://") }
            ?: emptyList()
    }

    // 初始化 PagerState
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { securedImageUrls.size }
    )

    // 觀察 currentPage
    val currentPage by remember {
        derivedStateOf { pagerState.currentPage }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = exhibitItem?.name ?: stringResource(R.string.information_title),
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

            exhibitItem != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        if (securedImageUrls.isNotEmpty()) {
                            HorizontalPager(
                                state = pagerState,
                                beyondViewportPageCount = 1, // 保留能改善圖片滑動時的 loading 延遲
                                key = { index -> securedImageUrls[index] }, // 保證 Compose 不會錯誤重組頁面
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                            ) { page ->
                                val url = securedImageUrls[page]
                                PagerImage(
                                    url = url,
                                    contentDescription = exhibitItem.name
                                )
                            }

                            if (securedImageUrls.size > 1) {
                                PagerIndicator(
                                    pageCount = securedImageUrls.size,
                                    currentPage = currentPage
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 中文名稱
                        InfoSection(
                            content = exhibitItem.name,
                            contentStyle = ProjectTextStyle.H5
                        )

                        // 英文名
                        InfoSection(
                            content = exhibitItem.nameEnglish,
                            contentStyle = ProjectTextStyle.H5
                        )

                        // 展覽區域
                        InfoSection(
                            content = exhibitItem.location
                        )

                        InfoSection(
                            title = stringResource(R.string.information_alsoKnown),
                            content = exhibitItem.alsoKnown
                        )

                        // 分類
                        when (exhibitItem) {
                            is Animal -> {
                                InfoSection(
                                    title = stringResource(R.string.information_phylum),
                                    content = exhibitItem.phylum
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_clazz),
                                    content = exhibitItem.clazz
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_order),
                                    content = exhibitItem.order
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_family),
                                    content = exhibitItem.family
                                )
                            }

                            is Plant -> {
                                InfoSection(
                                    title = stringResource(R.string.information_family),
                                    content = exhibitItem.family
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_genus),
                                    content = exhibitItem.genus
                                )
                            }
                        }

                        InfoSection(
                            title = stringResource(R.string.information_brief),
                            content = exhibitItem.brief
                        )

                        InfoSection(
                            title = stringResource(R.string.information_feature),
                            content = exhibitItem.feature
                        )

                        when (exhibitItem) {
                            is Animal -> {
                                InfoSection(
                                    title = stringResource(R.string.information_conservation),
                                    content = exhibitItem.conservation
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_distribution),
                                    content = exhibitItem.distribution
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_habitat),
                                    content = exhibitItem.habitat
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_behavior),
                                    content = exhibitItem.behavior
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_diet),
                                    content = exhibitItem.diet
                                )

                                InfoSection(
                                    title = stringResource(R.string.information_crisis),
                                    content = exhibitItem.crisis
                                )
                            }

                            is Plant -> {
                                InfoSection(
                                    title = stringResource(R.string.information_function_application),
                                    content = exhibitItem.functionApplicationRaw
                                )
                            }
                        }

                        exhibitItem.update?.let {
                            Text(
                                stringResource(R.string.information_last_updated, it),
                                style = ProjectTextStyle.H8,
                                color = ProjectColor.Black,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun InfoSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: String?,
    contentStyle: TextStyle = ProjectTextStyle.H8,
) {
    if (content.isNullOrEmpty()) {
        return
    }

    title?.let {
        it
        Text(
            text = it,
            style = ProjectTextStyle.H7,
            color = ProjectColor.Black,
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }

    Text(
        text = content,
        style = contentStyle,
        color = ProjectColor.Black,
        modifier = modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PagerImage(
    url: String,
    contentDescription: String?
) {
    GlideImage(
        model = url,
        contentDescription = contentDescription,
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

@Composable
fun PagerIndicator(pageCount: Int, currentPage: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(2.dp)
                    .background(
                        if (currentPage == index) ProjectColor.Black else Color.LightGray,
                        shape = CircleShape
                    )
            )
        }
    }
}