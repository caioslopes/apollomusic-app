package br.com.apollomusic.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ApolloInputText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle.Default,
        singleLine = true,
        shape = RoundedCornerShape(50),
        isError = isError,
        supportingText = {
            if (supportingText != null) {
                Text(text = supportingText)
            }
        },
        trailingIcon = trailingIcon,
    )
}