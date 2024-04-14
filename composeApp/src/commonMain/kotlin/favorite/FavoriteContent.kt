package favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import commonui.EntityComposable

@Composable
fun FavoriteContent(component: FavoriteComponent) {
    val state by component.state.subscribeAsState()
    Column {
        TopAppBar(title = { Text("Favorite") }, navigationIcon = {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Navigate to back", Modifier.clickable { component.goToBack() })
        })
        LazyColumn {
            items(state.data) {
                EntityComposable(it, true) {
                    component.removeFavorite(it)
                }
            }
        }
    }
}