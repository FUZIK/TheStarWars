package favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import core.EntityIdentity
import core.FavoriteSource
import coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DefaultFavoriteComponent(context: ComponentContext,
                               private val favoriteSource: FavoriteSource,
                               private val onBack: () -> Unit
): ComponentContext by context, FavoriteComponent {
    override val state = MutableValue(FavoriteComponent.State.DEFAULT)
    private val scope = coroutineScope(Dispatchers.IO)
    init {
        favoriteSource.entities.onEach { entities ->
            state.update {
                it.copy(data = entities)
            }
        }.launchIn(scope)
    }
    override fun goToBack() {
        onBack()
    }
    override fun removeFavorite(entity: EntityIdentity) {
        scope.launch {
            favoriteSource.remove(entity)
        }
    }
}
