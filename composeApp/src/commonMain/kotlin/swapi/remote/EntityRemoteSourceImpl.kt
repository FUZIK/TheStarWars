package swapi.remote

import core.EntityIdentity
import core.PagingResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.platformType
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class EntityRemoteSourceImpl<T: EntityIdentity, R: RemoteEntity<T>>(
    kType: KType,
    pagkType: KType,
    private val httpClient: HttpClient,
    private val entity: String
) : SWRemoteSource<T> {
    private val typeInfo = kType.run {
        TypeInfo(
            type = this.classifier as KClass<*>,
            reifiedType = this.platformType,
            kotlinType = this
        )
    }
    private val pagingTypeInfo = pagkType.run {
        TypeInfo(
            type = this.classifier as KClass<*>,
            reifiedType = this.platformType,
            kotlinType = this
        )
    }
    override suspend fun search(page: Int, query: String): PagingResult<List<T>> =
        httpClient.get(SWApiResource.Entity.Search(e = entity, page = page, search = query))
            .body<PagingResponse<R>>(pagingTypeInfo)
            .toPagingResult(page) { it.mapTo() }
    override suspend fun fetch(id: Int): T = httpClient.get(SWApiResource.Entity.ByID(e = entity, id = id))
        .body<R>(typeInfo)
        .mapTo()

    override suspend fun fetchPage(page: Int): PagingResult<List<T>> =
        httpClient.get(SWApiResource.Entity(entity = entity, page = page))
        .body<PagingResponse<R>>(pagingTypeInfo)
        .toPagingResult(page) { it.mapTo() }
}

inline fun <reified T: EntityIdentity, reified R: RemoteEntity<T>> EntityRemoteSourceImpl(
    httpClient: HttpClient,
    entityPath: String
) = EntityRemoteSourceImpl<T, R>(
    kType = typeOf<R>(),
    pagkType = typeOf<PagingResponse<R>>(),
    httpClient = httpClient,
    entity = entityPath
)