package search

import com.arkivanov.decompose.value.Value
import core.EntityIdentity
import core.EntityType

interface SearchComponent {
    val state: Value<State>
    fun changeQuery(query: String)
    fun changeType(type: EntityType)
    fun goToFavorite()
    fun callToFavorite(item: EntityIdentity, isFavorite: Boolean)
    data class BoxedFavoriteEntity(
        val entity: EntityIdentity,
        val isFavorite: Boolean
    ) {
        override fun equals(other: Any?) = entity == other
    }
    data class State(
        val query: String,
        val type: EntityType,
        val typesTo: List<EntityType>,
        val isLoading: Boolean,
        val data: List<BoxedFavoriteEntity>
    ) {
        companion object {
            val DEFAULT = State(
                query = "",
                type = EntityType.People,
                typesTo = listOf(EntityType.People, EntityType.Starships),
                isLoading = false,
                data = emptyList())
        }
    }
}
