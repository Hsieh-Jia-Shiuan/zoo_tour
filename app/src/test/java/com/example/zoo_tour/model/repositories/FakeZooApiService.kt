import com.example.zoo_tour.model.dataSource.ZooApiService
import com.example.zoo_tour.model.entities.Animal
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.entities.Plant
import com.example.zoo_tour.util.ApiResponse
import com.example.zoo_tour.util.ApiResult

open class FakeZooApiService(
    private val areas: List<Area> = emptyList(),
    private val animals: List<Animal> = emptyList(),
    private val plants: List<Plant> = emptyList()
) : ZooApiService {

    override suspend fun getAreas(scope: String): ApiResponse<Area> {
        return ApiResponse(
            ApiResult(
                limit = areas.size,
                offset = 0,
                count = areas.size,
                sort = "",
                results = areas
            )
        )
    }

    override suspend fun getAnimals(scope: String): ApiResponse<Animal> {
        return ApiResponse(
            ApiResult(
                limit = animals.size,
                offset = 0,
                count = animals.size,
                sort = "",
                results = animals
            )
        )
    }

    override suspend fun getPlants(scope: String): ApiResponse<Plant> {
        return ApiResponse(
            ApiResult(
                limit = plants.size,
                offset = 0,
                count = plants.size,
                sort = "",
                results = plants
            )
        )
    }
}