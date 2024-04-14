package swapi.remote

import core.PagingResult

interface SWRemoteSource <T> {
    suspend fun search(page: Int, query: String): PagingResult<List<T>>
    suspend fun fetch(id: Int): T?
    suspend fun fetchPage(page: Int): PagingResult<List<T>>
}
