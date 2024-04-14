package search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.getAndUpdate
import com.arkivanov.decompose.value.update
import core.EntityIdentity
import core.EntitySource
import core.EntityType
import core.FavoriteSource
import core.People
import core.Starship
import coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DefaultSearchComponent(
    private val context: ComponentContext,
    private val starshipSource: EntitySource<Starship>,
    private val peopleSource: EntitySource<People>,
    private val favoriteSource: FavoriteSource,
    private val navigateToFavorite: () -> Unit
): ComponentContext by context, SearchComponent {
    override val state = MutableValue(SearchComponent.State.DEFAULT)
    private val scope = coroutineScope(Dispatchers.IO)
    private var searchBy: Job? = null
    override fun changeQuery(query: String) {
        state.update { it.copy(query = query) }
        if (query.length >= 2) {
            searchBy(query)
        }
    }
    init {
        favoriteSource.entities.onEach { favorites ->
            state.update {
                it.copy(data = it.data.map { m -> m.copy(isFavorite = favorites.any { it == m.entity })})
            }
        }.launchIn(scope)
    }
    private fun searchBy(query: String) {
        val source = when(state.value.type) {
            EntityType.Starships -> starshipSource
            EntityType.People -> peopleSource
            else -> { return }
        }
        searchBy?.cancel()
        state.update {
            it.copy(isLoading = true) }
        searchBy = scope.launch { source.search(1, query).also { result ->
            val boxedData = result.data.map {
                SearchComponent.BoxedFavoriteEntity(it, favoriteSource.isFavorite(it))
            }
            state.update {
                it.copy(data = boxedData,
                    isLoading = false)
            }
        }}
    }
    override fun changeType(type: EntityType) {
        changeQuery(state.getAndUpdate { it.copy(type = type) }.query)
    }
    override fun goToFavorite() {
        navigateToFavorite()
    }
    override fun callToFavorite(item: EntityIdentity, isFavorite: Boolean) {
        scope.launch {
            if (isFavorite) {
                favoriteSource.add(item)
            } else {
                favoriteSource.remove(item)
            }
        }
    }
}
