package swapi.local

interface SWLocalSource <T> {
    suspend fun get(id: Int): T?
    suspend fun add(item: T)
    suspend fun getAll(): List<T>
}
