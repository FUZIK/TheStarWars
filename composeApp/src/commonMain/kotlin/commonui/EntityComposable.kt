package commonui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.EntityIdentity
import core.EntityNamed
import core.People
import core.Starship

@Composable
inline fun EntityComposable(entity: EntityIdentity, isFavorite: Boolean, crossinline onFavoriteClick: () -> Unit) {
    Row(Modifier
        .padding(4.dp)
        .clip(AbsoluteCutCornerShape(2.dp))
        .background(Color.LightGray)) {
        Column {
            when (entity) {
                is People -> {
                    Text(entity.name)
                    Text(entity.gender.name)
                    Text(entity.starships.size.toString())
                }
                is Starship -> {
                    Text(entity.model)
                    Text(entity.name)
                    Text(entity.manufacturer)
                    Text(entity.passengers.toString())
                }
            }
        }
        val description =
            if (entity is EntityNamed)
                "Add ${entity.name} to favorites"
            else
                "Add item to favorites"
        if (isFavorite) {
            Icon(Icons.Filled.Star, description,
                tint = Color.Yellow,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .clickable { onFavoriteClick() })
        } else {
            Icon(Icons.Outlined.Star, description,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .clickable { onFavoriteClick() })
        }
    }
}