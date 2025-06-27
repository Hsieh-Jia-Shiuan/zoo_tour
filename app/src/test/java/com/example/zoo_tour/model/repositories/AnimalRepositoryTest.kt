package com.example.zoo_tour.model.repositories

import FakeZooApiService
import com.example.zoo_tour.model.dataSource.RemoteDataSource
import com.example.zoo_tour.model.entities.Animal
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.entities.ImportDate
import com.example.zoo_tour.util.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnimalRepositoryTest {
    @Test
    fun `getAnimals emits Loading then Success`() = runTest {
        val fakeAnimals = listOf(
            Animal(
                id = 1,
                importDate = ImportDate("2024-01-01", 3, "Asia/Taipei"),
                nameCh = "台灣黑熊",
                summary = "台灣特有種",
                keywords = "熊,台灣",
                alsoKnown = "Formosan Black Bear",
                geo = "",
                location = "",
                pioGroup = "",
                nameEnglish = "Formosan Black Bear",
                nameLatin = "Ursus thibetanus formosanus",
                phylum = "",
                clazz = "",
                order = "",
                family = "",
                conservation = "",
                distribution = "",
                habitat = "",
                feature = "",
                behavior = "",
                diet = "",
                crisis = "",
                interpretation = "",
                themeName = "",
                themeUrl = "",
                adopt = "",
                code = "",
                pic01Alt = "",
                pic01Url = "",
                pic02Alt = "",
                pic02Url = "",
                pic03Alt = "",
                pic03Url = "",
                pic04Alt = "",
                pic04Url = "",
                pdf01Alt = "",
                pdf01Url = "",
                pdf02Alt = "",
                pdf02Url = "",
                voice01Alt = "",
                voice01Url = "",
                voice02Alt = "",
                voice02Url = "",
                voice03Alt = "",
                voice03Url = "",
                vedioUrl = "",
                update = "",
                cid = ""
            )
        )
        val fakeApiService = FakeZooApiService(animals = fakeAnimals)
        val remoteDataSource = RemoteDataSource(fakeApiService)
        val repository = ZooRepository(remoteDataSource)

        val result = repository.getAnimals().toList()
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Success && (result[1] as NetworkResult.Success).data == fakeAnimals)
    }

    @Test
    fun `getAnimals emits Loading then Error on exception`() = runTest {
        val fakeApiService = object : FakeZooApiService() {
            override suspend fun getAnimals(scope: String) = throw RuntimeException("Test error")
        }
        val remoteDataSource = RemoteDataSource(fakeApiService)
        val repository = ZooRepository(remoteDataSource)

        val result = repository.getAnimals().toList()
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Error)
        assertTrue((result[1] as NetworkResult.Error).message!!.contains("Test error"))
    }

    @Test
    fun `getAnimals emits Loading then Success with empty list`() = runTest {
        val fakeApiService = FakeZooApiService(animals = emptyList())
        val remoteDataSource = RemoteDataSource(fakeApiService)
        val repository = ZooRepository(remoteDataSource)

        val result = repository.getAnimals().toList()
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Success)
        (result[1] as NetworkResult.Success).data?.let { assertTrue(it.isEmpty()) }
    }
}