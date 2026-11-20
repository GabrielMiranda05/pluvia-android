package br.com.pluvia.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.com.pluvia.ui.theme.BlueAccent
import br.com.pluvia.ui.theme.BluePrimary
import br.com.pluvia.ui.theme.BlueSurface
import br.com.pluvia.ui.theme.GrayBorder
import br.com.pluvia.ui.theme.GrayText
import br.com.pluvia.ui.theme.RedLogout
import br.com.pluvia.ui.theme.WhiteSurface

@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    errorText: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = GrayText,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFFB0BEC5)
                )
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            isError = errorText != null,
            supportingText = errorText?.let {
                { Text(text = it, color = RedLogout, style = MaterialTheme.typography.bodySmall) }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = GrayBorder,
                focusedContainerColor = WhiteSurface,
                unfocusedContainerColor = BlueSurface,
                cursorColor = BluePrimary,
                focusedLabelColor = BluePrimary,
                errorBorderColor = RedLogout,
                errorContainerColor = WhiteSurface
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
