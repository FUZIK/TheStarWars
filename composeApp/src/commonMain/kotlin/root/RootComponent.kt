package root

import Dependencies
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import favorite.DefaultFavoriteComponent
import favorite.FavoriteComponent
import kotlinx.serialization.Serializable
import search.DefaultSearchComponent
import search.SearchComponent

class RootComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    val childStack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Search,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild,
        )

    private fun createChild(config: Config, context: ComponentContext) =
        when(config) {
            is Config.Search -> {
                Child.SearchChild(
                    DefaultSearchComponent(context, Dependencies.starshipSource, Dependencies.peopleSource, Dependencies.favoriteSource) {
                        navigation.push(Config.Favorite)
                    }
                )
            }
            is Config.Favorite -> {
                Child.FavoriteChild(
                    DefaultFavoriteComponent(context, Dependencies.favoriteSource) { navigation.pop() }
                )
            }
        }

    @Serializable
    sealed class Child {
        class SearchChild(val component: SearchComponent) : Child()
        class FavoriteChild(val component: FavoriteComponent) : Child()
    }

    @Serializable
    sealed class Config {
        data object Search : Config()
        data object Favorite : Config()
    }
}
