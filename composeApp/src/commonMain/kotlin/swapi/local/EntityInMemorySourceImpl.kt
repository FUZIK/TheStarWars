package swapi.local

import core.EntityIdentity

class EntityInMemorySourceImpl<T: EntityIdentity>: SWLocalSource<T> {
    private val list = arrayListOf<T>()
    override suspend fun get(id: Int): T? = list.firstOrNull { id == it.id }
    override suspend fun add(item: T) {
        list.add(item)
    }
    override suspend fun getAll(): List<T> = list.toList()
}
