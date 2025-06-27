package com.example.zoo_tour.model.repositories

import FakeZooApiService
import com.example.zoo_tour.model.dataSource.RemoteDataSource
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.entities.ImportDate
import com.example.zoo_tour.model.entities.Plant
import com.example.zoo_tour.util.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlantRepositoryTest {
    @Test
    fun `getPlants emits Loading then Success`() = runTest {
        val fakePlants = listOf(
            Plant(
                id = 1,
                importDate = ImportDate("2024-01-01", 3, "Asia/Taipei"),
                nameChinese = "台灣杉",
                summary = "台灣特有植物",
                keywords = "樹,台灣",
                alsoKnown = "Taiwania",
                geo = "",
                location = "",
                nameEnglish = "Taiwania",
                nameLatin = "Taiwania cryptomerioides",
                family = "Cupressaceae",
                genus = "Taiwania",
                brief = "台灣特有針葉樹",
                feature = "高大直立",
                functionApplicationRaw = "建材",
                code = "",
                pic01Url = "",
                pic01Alt = "",
                pic02Url = "",
                pic02Alt = "",
                pic03Url = "",
                pic03Alt = "",
                pic04Url = "",
                pic04Alt = "",
                pdf01Url = "",
                pdf01Alt = "",
                pdf02Url = "",
                pdf02Alt = "",
                voice01Url = "",
                voice01Alt = "",
                voice02Url = "",
                voice02Alt = "",
                voice03Url = "",
                voice03Alt = "",
                vedioUrl = "",
                update = "",
                cid = ""
            )
        )
        val fakeApiService = FakeZooApiService(plants = fakePlants)
        val remoteDataSource = RemoteDataSource(fakeApiService)
        val repository = ZooRepository(remoteDataSource)

        val result = repository.getPlants().toList()
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Success && (result[1] as NetworkResult.Success).data == fakePlants)
    }

    @Test
    fun `getPlants emits Loading then Error on exception`() = runTest {
        val fakeApiService = object : FakeZooApiService() {
            override suspend fun getPlants(scope: String) = throw RuntimeException("Test error")
        }
        val remoteDataSource = RemoteDataSource(fakeApiService)
        val repository = ZooRepository(remoteDataSource)

        val result = repository.getPlants().toList()
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Error)
        assertTrue((result[1] as NetworkResult.Error).message!!.contains("Test error"))
    }

    @Test
    fun `getPlants emits Loading then Success with empty list`() = runTest {
        val fakeApiService = FakeZooApiService(plants = emptyList())
        val remoteDataSource = RemoteDataSource(fakeApiService)
        val repository = ZooRepository(remoteDataSource)

        val result = repository.getPlants().toList()
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Success)
        (result[1] as NetworkResult.Success).data?.let { assertTrue(it.isEmpty()) }
    }
}