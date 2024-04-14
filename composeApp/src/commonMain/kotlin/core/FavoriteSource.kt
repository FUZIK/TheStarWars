package core

import kotlinx.coroutines.flow.Flow

interface FavoriteSource {
    val entities: Flow<List<EntityIdentity>>
    suspend fun add(item: EntityIdentity)
    suspend fun remove(item: EntityIdentity)
    suspend fun isFavorite(item: EntityIdentity): Boolean
}