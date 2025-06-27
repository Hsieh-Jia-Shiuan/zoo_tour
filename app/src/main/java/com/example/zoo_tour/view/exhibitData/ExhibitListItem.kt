package com.example.zoo_tour.view.exhibitData

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.zoo_tour.model.entities.ExhibitItem
import com.example.zoo_tour.ui.theme.ProjectColor
import com.example.zoo_tour.ui.theme.ProjectTextStyle

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ExhibitListItem(
    item: ExhibitItem,
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
                .padding(16.dp)
        ) {
            val (imageRef, textColRef, arrowRef) = createRefs()

            // 圖片
            val rawImageUrl = item.imageUrls.firstOrNull()
            val securedImageUrl = rawImageUrl?.replace("http://", "https://")
            if (!securedImageUrl.isNullOrEmpty()) {
                GlideImage(
                    model = securedImageUrl,
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(90.dp)
                        .constrainAs(imageRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    contentScale = ContentScale.Crop,
                    loading = placeholder {
                        Box(
                            modifier = Modifier.size(90.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(50.dp))
                        }
                    },
                    failure = placeholder {
                        Box(
                            modifier = Modifier.size(90.dp),
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
                val (nameRef, featureRef) = createRefs()
                item.name?.let {
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
                item.feature?.let {
                    Text(
                        text = it,
                        style = ProjectTextStyle.H10,
                        color = ProjectColor.Black50,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.constrainAs(featureRef) {
                            top.linkTo(nameRef.bottom, margin = 4.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
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
                    .size(24.dp)
                    .constrainAs(arrowRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
            )
        }
    }
}