package core

class PagingResult<T>(
    val data: T, // TODO почему тут не колекция?
    val hasNext: Boolean,
    val nextPage: Int
) {
    companion object {
        const val NO_NEXT_PAGE = -1
        inline fun <T> singlePageResult(data: T) = PagingResult<T>(
            data = data,
            hasNext = false,
            nextPage = NO_NEXT_PAGE
        )
    }
}
interface EntitySource <T: EntityIdentity> {
    suspend fun search(page: Int, query: String): PagingResult<List<T>>
    suspend fun get(id: ID): T?
    suspend fun getPage(page: Int): PagingResult<List<T>>
}
