package com.maureen.wandevelop.feature.profile.sign

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maureen.wandevelop.R
import com.maureen.wandevelop.ui.composable.ProgressDialog
import com.maureen.wandevelop.ui.composable.WanDevTpoAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignInOrUpScreen(
    isSignInMode: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignViewModel = viewModel()
) {
    LaunchedEffect(isSignInMode) {
        val keys = mutableListOf<Int>(R.string.label_username, R.string.label_password)
        if (isSignInMode.not()) {
            keys.add(R.string.label_password_again)
        }
        viewModel.setupInput(keys)
    }

    val snackBarHostState = remember { SnackbarHostState() }
    val inputs by viewModel.inputState.collectAsStateWithLifecycle()
    val buttonEnable by remember { derivedStateOf { inputs.isNotEmpty() && inputs.none { state -> state.value.input.isBlank() or state.value.isError } } }
    val signState by viewModel.signState.collectAsStateWithLifecycle()
    Log.d("TAG", "SignInOrUpScreen: $signState")
    if (signState.isLoading) {
        ProgressDialog(
            message = stringResource(if (isSignInMode) R.string.prompt_sign_in_loading else R.string.prompt_sign_up_loading)
        )
    }
    if (signState.result) {
        // 登录成功
        onBackClick()
    } else if (signState.resultMsg.isNotBlank()) {
        // 登录失败
        AlertDialog(
            onDismissRequest = { viewModel.clearSignState() },
            title = {
                Text(
                    text = stringResource(R.string.prompt_sign_in_failed),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = signState.resultMsg,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearSignState() },
                    content = {
                        Text(
                            text = stringResource(R.string.confirm),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            WanDevTpoAppBar(
                title = "",
                onNavigationIconClick = onBackClick
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = padding.calculateTopPadding(), horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.prompt_welcome_back),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.fillMaxSize(0.1F))
                CustomOutlinedTextField(
                    key = R.string.label_username,
                    state = inputs,
                    onValueChange = viewModel::confirmInput
                )
                Spacer(modifier = Modifier.size(20.dp))
                CustomOutlinedTextField(
                    key = R.string.label_password,
                    state = inputs,
                    onValueChange = viewModel::confirmInput,
                    toggleValueVisible = viewModel::toggleInputVisibility,
                    imeAction = if (isSignInMode.not()) ImeAction.Next else ImeAction.Done
                )
                if (isSignInMode.not()) {
                    Spacer(modifier = Modifier.size(20.dp))
                    CustomOutlinedTextField(
                        key = R.string.label_password_again,
                        state = inputs,
                        onValueChange = viewModel::confirmInput,
                        toggleValueVisible = viewModel::toggleInputVisibility,
                        imeAction = ImeAction.Done
                    )
                }
                Text(
                    text = stringResource(R.string.action_forgot_password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.End)
                        .padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.size(20.dp))
                Button(
                    onClick = {
                        viewModel.signInOrUp(isSignInMode)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraSmall,
                    enabled = buttonEnable,
                    contentPadding = PaddingValues(vertical = 14.dp),
                    content = {
                        Text(
                            text = stringResource(if (isSignInMode) R.string.action_sign_in else R.string.action_sign_up),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                )
                Text(
                    text = stringResource(R.string.prompt_no_account),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                        .padding(vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}

@Composable
private fun CustomOutlinedTextField(
    key: Int,
    state: Map<Int, SignInputState>,
    onValueChange: (Int, String) -> Unit,
    toggleValueVisible: ((Int) -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next
) {
    val focusManager = LocalFocusManager.current
    val valueVisible = state[key]?.showInput == true
    OutlinedTextField(
        value = state[key]?.input ?: "",
        isError = state[key]?.isError == true,
        onValueChange = { onValueChange(key, it) },
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = buildAnnotatedString {
                    append(text = stringResource(key))
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                        append(text = stringResource(R.string.label_star))
                    }
                }
            )
        },
        trailingIcon = {
            if (toggleValueVisible == null) {
                null
            } else {
                IconButton(
                    onClick = { toggleValueVisible(key) },
                    content = {
                        Icon(
                            painter = painterResource(if (valueVisible) R.drawable.ic_round_visibility_off_24 else R.drawable.ic_round_visibility_24),
                            contentDescription = ""
                        )
                    }
                )
            }
        },
        visualTransformation = if (valueVisible || toggleValueVisible == null) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = if (toggleValueVisible == null) KeyboardType.Unspecified else KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        singleLine = true
    )
}