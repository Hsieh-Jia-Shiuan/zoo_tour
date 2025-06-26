import com.example.zoo_tour.model.dataSource.RemoteDataSource
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.util.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ZooRepositoryTest {
    @Test
    fun `getAreas emits Loading then Success`() = runTest {
        // 建立假的館區資料
        val fakeAreas = listOf(
            Area(
                id = 1,
                eNo = "1",
                eName = "Area1",
                eCategory = "",
                ePicUrl = "",
                eInfo = "",
                eMemo = "",
                eGeo = "",
                eUrl = ""
            )
        )
        // 用假資料建立假的 ApiService
        val fakeApiService = FakeZooApiService(areas = fakeAreas)
        // 用假的 ApiService 建立 RemoteDataSource
        val remoteDataSource = RemoteDataSource(fakeApiService)
        // 用 RemoteDataSource 建立 Repository
        val repository = ZooRepository(remoteDataSource)

        // 收集 getAreas() flow 發出的所有結果
        val result = repository.getAreas().toList()
        // 驗證第一個結果是 Loading
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        // 驗證第二個結果是 Success 且資料正確
        assert(result[1] is NetworkResult.Success && (result[1] as NetworkResult.Success).data == fakeAreas)
    }

    @Test
    fun `getAreas emits Loading then Error on exception`() = runTest {
        // 模擬 API 會丟出 Exception
        val fakeApiService = object : FakeZooApiService() {
            override suspend fun getAreas(scope: String) = throw RuntimeException("Test error")
        }
        val remoteDataSource = RemoteDataSource(fakeApiService)
        val repository = ZooRepository(remoteDataSource)

        val result = repository.getAreas().toList()
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Error)
        assertTrue((result[1] as NetworkResult.Error).message!!.contains("Test error"))
    }

    @Test
    fun `getAreas emits Loading then Success with empty list`() = runTest {
        // 回傳空 list
        val fakeApiService = FakeZooApiService(areas = emptyList())
        val remoteDataSource = RemoteDataSource(fakeApiService)
        val repository = ZooRepository(remoteDataSource)

        val result = repository.getAreas().toList()
        assertEquals(2, result.size)
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Success)
        (result[1] as NetworkResult.Success).data?.let { assertTrue(it.isEmpty()) }
    }
}