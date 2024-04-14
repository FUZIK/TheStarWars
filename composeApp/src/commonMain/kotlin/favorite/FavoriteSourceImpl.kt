package favorite

import core.EntityIdentity
import core.FavoriteSource
import kotlinx.coroutines.flow.MutableStateFlow

class FavoriteSourceImpl : FavoriteSource {
    private val memoryFavorite = mutableSetOf<EntityIdentity>()
    override val entities = MutableStateFlow(emptyList<EntityIdentity>())
    override suspend fun add(item: EntityIdentity) {
        memoryFavorite.add(item)
        entities.emit(memoryFavorite.toList())
    }
    override suspend fun remove(item: EntityIdentity) {
        memoryFavorite.removeAll { item == it }
        entities.emit(memoryFavorite.toList())
    }
    override suspend fun isFavorite(item: EntityIdentity) = memoryFavorite.any { it == item }
}