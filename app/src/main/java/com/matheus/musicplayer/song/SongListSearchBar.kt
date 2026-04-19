package com.matheus.musicplayer.song

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.matheus.musicplayer.R
import com.matheus.musicplayer.ui.theme.GreyMedium

@Composable
fun SongSearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onImeSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.search_placeholder),
                style = MaterialTheme.typography.labelMedium,
                color = GreyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = GreyMedium
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
            ) {
                IconButton(onClick = { onSearchQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.25f)
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(
            onSearch = { onImeSearch() }
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                shape = RoundedCornerShape(10.dp),
                color = Color.White.copy(alpha = 0.1f)
            )
    )
}