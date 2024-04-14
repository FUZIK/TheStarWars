package swapi.remote

import core.PagingResult
import core.People
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

class PeopleRemoteImpl(
    private val httpClient: HttpClient
) : SWRemoteSource<People> {
    override suspend fun search(page: Int, query: String): PagingResult<List<People>>
            = httpClient.get(SWApi.People.Search(page = page, search = query))
        .body<PagingResponse<RemotePeople>>()
        .toPagingResult(page, RemotePeople::mapToPeople)
    override suspend fun fetch(id: Int): People
            = httpClient.get(SWApi.People.ByID(id = id))
        .body<RemotePeople>().mapToPeople()
    override suspend fun fetchPage(page: Int): PagingResult<List<People>>
            = httpClient.get(SWApi.People(page = page))
        .body<PagingResponse<RemotePeople>>()
        .toPagingResult(page, RemotePeople::mapToPeople)
}
