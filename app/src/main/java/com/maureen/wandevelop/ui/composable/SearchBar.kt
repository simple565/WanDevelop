package com.maureen.wandevelop.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maureen.wandevelop.ui.theme.WanDevelopTheme
import com.maureen.wandevelop.ui.theme.WanDevelopTypography

/**
 * 通用搜索框
 * @author lianml
 * @date 2025/2/3
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    placeholderText: String,
    query: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    BasicTextField(
        value = query,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            if (query.isBlank()) {
                return@KeyboardActions
            }
            onSearch(query)
        }),
        singleLine = true
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = query,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            placeholder = {
                Text(
                    text = placeholderText,
                    modifier = Modifier,
                    style = WanDevelopTypography.labelLarge
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "")
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onValueChange("")
                        },
                        modifier = Modifier.size(24.dp),
                        content = {
                            Icon(Icons.Default.Clear, contentDescription = "")
                        }
                    )
                }
            },
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            contentPadding = OutlinedTextFieldDefaults.contentPadding(
                top = 0.dp,
                bottom = 0.dp
            )
        )
    }
}

@Preview
@Composable
fun SearchViewPreview() {
    WanDevelopTheme {
        Surface(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .height(56.dp)
                .padding(6.dp)
        ) {
            SearchView(
                modifier = Modifier
                    .fillMaxWidth(),
                placeholderText = "搜索关键词以空格形式隔开",
                query = "",
                onValueChange = {},
                onSearch = {}
            )
        }
    }
}