package swapi

import core.EntityIdentity
import core.EntitySource
import core.PagingResult
import swapi.local.SWLocalSource
import swapi.remote.SWRemoteSource

class EntitySourceImpl<T: EntityIdentity>(
    private val localSource: SWLocalSource<T>,
    private val remoteSource: SWRemoteSource<T>,
): EntitySource<T> {
    override suspend fun search(page: Int, query: String): PagingResult<List<T>> =
        try {
            remoteSource.search(page, query).also {
                it.data.forEach { localSource.add(it) } // TODO addAll
            }
        } catch (e: Exception) {
            PagingResult.singlePageResult(localSource.getAll())
        }
    override suspend fun get(id: Int) =
        try {
            localSource.get(id) ?: throw RuntimeException("id $id not found")
        } catch (e: Exception) {
            try {
                remoteSource.fetch(id).also { item ->
                    if (item == null) {
                        throw RuntimeException("id $id not found")
                    } else {
                        localSource.add(item)
                    }
                }
            } catch (e: Exception) {
                throw RuntimeException("id $id not found")
            }
        }
    override suspend fun getPage(page: Int): PagingResult<List<T>> =
        try {
            println("Load getPage $page")
            remoteSource.fetchPage(page).also {
                it.data.forEach { localSource.add(it) } // TODO addAll
            }
        } catch (e: Exception) {
            PagingResult.singlePageResult(localSource.getAll())
        }
}