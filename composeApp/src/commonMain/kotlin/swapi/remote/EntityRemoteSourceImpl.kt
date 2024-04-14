package swapi.remote

import core.EntityIdentity
import core.Gender
import core.GenderSerializer
import core.PagingResult
import core.People
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.resources.Resource
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@Resource("api")
class SWApi1 {
    @Resource("/{entity}")
    class Entity(val parent: SWApi = SWApi(), val entity: String, val page: Int = 1) {
        @Resource("/{id}")
        class ByID(val entity: String, val parent: Entity = Entity(entity = entity), val id: Int)
        @Resource("/")
        class Search(val entity: String, val parent: Entity = Entity(entity = entity), val page: Int = 1, val search: String)
    }
}
@Serializable
class PagingResponse1<I>(
    val next: String?,
    val results: List<I>
) {
    suspend inline fun <reified T: I> HttpResponse.serialize(): T {
        return body<T>()
    }

}
@Serializable
sealed interface RemoteEntity<T: EntityIdentity> {
    fun mapTo(): T
}

@Serializable
class RemotePeople1(
    val name: String,
    @Serializable(with = GenderSerializer::class)
    val gender: Gender,
    val starships: List<String>
): RemoteEntity<People> {
    override fun mapTo(): People {
        return People(1,"TestName", Gender.FEMALE, emptyList())
    }
}
class EntityRemoteSourceImpl<T: EntityIdentity, R: RemoteEntity<T>>(
    private val kclazz: KClass<R>,
    private val httpClient: HttpClient,
    private val entity: String,
) : SWRemoteSource<T> {
    override suspend fun search(page: Int, query: String): PagingResult<List<T>> {
        val result: PagingResponse<R> = httpClient.get(SWApi1.Entity.Search(entity = entity, page = page, search = query)).bodyAsText().run {
            Json.decodeFromString<PagingResponse<R>>(this)
        }
        return result.toPagingResult(page) { it.mapTo() }
    }
    @OptIn(InternalSerializationApi::class)
    override suspend fun fetch(id: Int): T {
        val result: R = httpClient
            .get(SWApi1.Entity.ByID(entity = entity, id = id))
            .bodyAsText().run {
            Json.decodeFromString(kclazz.serializer(), this)
        }
        return result.mapTo()
    }
    override suspend fun fetchPage(page: Int): PagingResult<List<T>> {
        val result: PagingResponse<R> = httpClient.get(SWApi1.Entity(entity = entity, page = page)).body()
        return result.toPagingResult(page) { it.mapTo() }
    }
}