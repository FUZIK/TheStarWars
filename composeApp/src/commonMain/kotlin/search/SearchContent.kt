package search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import commonui.EntityComposable

@Composable
fun SearchContent(component: SearchComponent, modifier: Modifier) {
    val state by component.state.subscribeAsState()
    Column(modifier) {
        TopAppBar(title = { Text("The Star Wars") }, actions = {
            Icon(Icons.Default.Star, "Show Favorite page", modifier = Modifier.clickable { component.goToFavorite() })
        })
        TextField(
            modifier = Modifier.background(Color.White),
            value = state.query,
            placeholder = { Text("Search") },
            onValueChange = component::changeQuery,
            singleLine = true
        )
        LazyRow {
            items(state.typesTo) {
                Button(modifier = if (state.type == it) Modifier.background(Color.Cyan) else Modifier,
                    onClick = { component.changeType(it) }) {
                    Text(text = it.name)
                }
            }
        }
        LazyColumn {
            items(state.data) { (entity, isFavorite) ->
                EntityComposable(entity, isFavorite) {
                    component.callToFavorite(entity, !isFavorite)
                }
            }
        }
        AnimatedVisibility(state.isLoading) {
            Column(Modifier.fillMaxWidth()) {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
