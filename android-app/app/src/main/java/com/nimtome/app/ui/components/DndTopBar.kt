package com.nimtome.app.ui.components

import android.app.Activity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nimtome.app.R

@Composable
fun DndTopBar(
    titleText: String? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val activity = (LocalContext.current as? Activity)

    TopAppBar(
        title = { Text(text = titleText?: stringResource(id = R.string.app_name)) },
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.primary,
        navigationIcon = {
            IconButton(onClick = { activity?.onBackPressed() }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "back arrow"
                )
            }
        },
        actions = actions,
    )
}