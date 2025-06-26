package com.example.zoo_tour.view.information

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            errorMessage = "未找到展覽項目資訊。"
        }
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

            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        style = ProjectTextStyle.H7,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            exhibitItem != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        val rawImageUrl = exhibitItem!!.imageUrl
                        val securedImageUrl = rawImageUrl?.replace("http://", "https://")

                        securedImageUrl?.let { url ->
                            GlideImage(
                                model = url,
                                contentDescription = exhibitItem!!.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop,
                                loading = placeholder {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(50.dp))
                                    }
                                },
                                failure = placeholder {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
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
                        } ?: run {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = exhibitItem!!.name,
                            style = ProjectTextStyle.H5,
                            color = ProjectColor.Black,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = exhibitItem!!.feature,
                            style = ProjectTextStyle.H7,
                            color = ProjectColor.Black,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        when (exhibitItem) {
                            is Animal -> {
                                Text(
                                    "英文名稱: ${(exhibitItem as Animal).name}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "別名: ${(exhibitItem as Animal).alsoKnown}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "棲息地: ${(exhibitItem as Animal).habitat}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "行為: ${(exhibitItem as Animal).behavior}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "飲食: ${(exhibitItem as Animal).diet}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "危機: ${(exhibitItem as Animal).crisis}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }

                            is Plant -> {
                                Text(
                                    "英文名稱: ${(exhibitItem as Plant).nameEnglish}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "別名: ${(exhibitItem as Plant).alsoKnown}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "科別: ${(exhibitItem as Plant).family}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "簡介: ${(exhibitItem as Plant).brief}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "功能與應用: ${(exhibitItem as Plant).functionApplicationRaw ?: "N/A"}",
                                    style = ProjectTextStyle.H8,
                                    color = ProjectColor.Black50,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.information_not_found),
                        style = ProjectTextStyle.H7,
                        color = ProjectColor.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}