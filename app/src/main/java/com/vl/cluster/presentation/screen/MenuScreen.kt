package com.vl.cluster.presentation.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vl.cluster.R

@Preview(showSystemUi = true)
@Composable
fun MenuScreen() {
    val context = LocalContext.current
    val menuItems = remember { mutableStateListOf(
        MenuItem(MenuRoute.NEWSFEED, context.getString(R.string.newsfeed), R.drawable.ic_newsfeed, checked = true),
        MenuItem(MenuRoute.MESSENGER, context.getString(R.string.messenger), R.drawable.ic_message),
    ) }

    Column {
        Box(modifier = Modifier.weight(1f)) {
            when (menuItems.first(MenuItem::checked).route) {
                MenuRoute.NEWSFEED -> NewsfeedScreen()
                MenuRoute.MESSENGER -> Unit // TODO
            }
        }
        BottomNavigationBar(
            items = menuItems,
            onItemClicked = { position ->
                for (i in menuItems.indices)
                    menuItems[i] = menuItems[i].copy(checked = i == position)
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomNavigationBar(
    items: List<MenuItem>,
    onItemClicked: (position: Int) -> Unit = {}
) {
    Surface(shadowElevation = 16.dp) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            for (item in items) MenuItemComponent(
                item = item,
                onClick = { checkedItem -> onItemClicked(items.indexOf(checkedItem)) }
            )
        }
    }
}

@Composable
fun MenuItemComponent(
    item: MenuItem,
    onClick: (item: MenuItem) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            onClick = { onClick(item) },
            indication = null, // get rid of ripple effect
            interactionSource = interactionSource // required to override indication
        )
    ) {
        val (focusedColor, unfocusedColor) =
            MaterialTheme.colorScheme.onPrimaryContainer to MaterialTheme.colorScheme.primary
        Icon(
            painter = painterResource(item.icon),
            contentDescription = null,
            modifier = Modifier.size(size = 32.dp),
            tint = if (item.checked) focusedColor else unfocusedColor
        )
        Text(
            text = item.title,
            fontWeight = FontWeight.Bold,
            color = if (item.checked) focusedColor else unfocusedColor
        )
    }
}

data class MenuItem(
    val route: MenuRoute,
    val title: String,
    @DrawableRes val icon: Int,
    val checked: Boolean = false
)

enum class MenuRoute {
    NEWSFEED,
    MESSENGER
}