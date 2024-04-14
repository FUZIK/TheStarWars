package swapi.remote

import core.PagingResult
import core.Starship
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

class StarshipRemoteImpl(
    private val httpClient: HttpClient
) : SWRemoteSource<Starship> {
    override suspend fun search(page: Int, query: String): PagingResult<List<Starship>>
        = httpClient.get(SWApi.Starships.Search(page = page, search = query))
            .body<PagingResponse<RemoteStarship>>()
            .toPagingResult(page, RemoteStarship::mapToStarship)
    override suspend fun fetch(id: Int): Starship
        = httpClient.get(SWApi.Starships.ByID(id = id))
            .body<RemoteStarship>().mapToStarship()
    override suspend fun fetchPage(page: Int): PagingResult<List<Starship>>
        = httpClient.get(SWApi.Starships(page = page))
            .body<PagingResponse<RemoteStarship>>()
            .toPagingResult(page, RemoteStarship::mapToStarship)
}
