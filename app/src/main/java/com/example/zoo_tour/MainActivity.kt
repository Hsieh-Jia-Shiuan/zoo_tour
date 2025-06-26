package com.example.zoo_tour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.zoo_tour.model.dataSource.RemoteDataSource
import com.example.zoo_tour.model.dataSource.ZooApiService
import com.example.zoo_tour.model.entities.ExhibitItem
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.ui.theme.ZooTourTheme
import com.example.zoo_tour.util.Constants.BASE_URL
import com.example.zoo_tour.view.area.AreaListScreen
import com.example.zoo_tour.view.exhibitData.ExhibitDataScreen
import com.example.zoo_tour.view.information.InformationScreen
import com.example.zoo_tour.viewModel.ZooViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        val apiService: ZooApiService = retrofit.create(ZooApiService::class.java)
        val remoteDataSource = RemoteDataSource(apiService)
        val repository = ZooRepository(remoteDataSource)

        setContent {
            ZooTourTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "area_list"
                    ) {
                        // 館區簡介
                        composable("area_list") {
                            AreaListScreen(
                                navController = navController,
                                repository = repository
                            )
                        }

                        // 館內資料
                        composable(
                            "exhibit_data/{areaName}",
                            arguments = listOf(navArgument("areaName") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val areaName = backStackEntry.arguments?.getString("areaName")
                            ExhibitDataScreen(
                                navController = navController,
                                repository = repository,
                                areaName = areaName
                            )
                        }

                        // 動物或植物詳細資料
                        composable("information_detail") { backStackEntry ->
                            val exhibitItem = navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.get<ExhibitItem>("exhibitItem")

                            InformationScreen(
                                navController = navController,
                                exhibitItem = exhibitItem
                            )
                        }
                    }
                }
            }
        }
    }
}