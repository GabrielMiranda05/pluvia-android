package br.com.pluvia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import br.com.pluvia.ui.component.AppButton
import br.com.pluvia.ui.component.AppLogo
import br.com.pluvia.ui.component.AppTextField
import br.com.pluvia.ui.theme.BlueBackground
import br.com.pluvia.ui.theme.BluePrimary
import br.com.pluvia.ui.theme.GrayText
import br.com.pluvia.ui.viewmodel.AuthViewModel
import br.com.pluvia.util.Resource
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is Resource.Success -> {
                viewModel.resetLoginState()
                onLoginSuccess()
            }
            is Resource.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                viewModel.resetLoginState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueBackground)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 48.dp)
        ) {
            AppLogo(size = 88.dp)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pluvia",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Monitore seus terrenos e previna incêndios com irrigação inteligente",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(44.dp))

            AppTextField(
                label = "E-mail",
                value = email,
                onValueChange = viewModel::onEmailChange,
                placeholder = "voce@email.com.br",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                label = "Senha",
                value = password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "••••••••",
                visualTransformation = if (passwordVisible) VisualTransformation.None
                                       else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility
                                          else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = GrayText
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(36.dp))

            AppButton(
                text = "Entrar",
                onClick = viewModel::login,
                isLoading = loginState is Resource.Loading
            )

            Spacer(modifier = Modifier.height(28.dp))

            val annotatedText = buildAnnotatedString {
                withStyle(SpanStyle(color = GrayText)) {
                    append("Ainda não tem uma conta? ")
                }
                pushStringAnnotation(tag = "REGISTER", annotation = "register")
                withStyle(SpanStyle(color = BluePrimary, fontWeight = FontWeight.SemiBold)) {
                    append("Criar conta")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations("REGISTER", offset, offset)
                        .firstOrNull()?.let { onNavigateToRegister() }
                }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
