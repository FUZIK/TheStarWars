package root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import favorite.FavoriteContent
import search.SearchContent

@Composable
fun RootContent(component: RootComponent) {
    MaterialTheme {
        Surface(modifier = Modifier, color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
            ) {
                Children(component = component, modifier = Modifier.weight(1F))
            }
        }
    }
}

@Composable
private fun Children(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.childStack,
        modifier = modifier,

        // Workaround for https://issuetracker.google.com/issues/270656235
        animation = stackAnimation(fade()),
//            animation = tabAnimation(),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.SearchChild -> SearchContent(
                component = child.component,
                modifier = modifier.fillMaxSize()
            )
            is RootComponent.Child.FavoriteChild -> FavoriteContent(
                component = child.component
            )
        }
    }
}