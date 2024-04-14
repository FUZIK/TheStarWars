package favorite

import com.arkivanov.decompose.value.Value
import core.EntityIdentity

interface FavoriteComponent {
    val state: Value<State>
    fun goToBack()
    fun removeFavorite(entity: EntityIdentity)
    data class State(
        val isLoading: Boolean,
        val data: List<EntityIdentity>
    ) {
        companion object {
            val DEFAULT = State(false, emptyList())
        }
    }
}