package com.example.hotmovies.presentation.shared.views

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme

@Stable
@Immutable
data class CustomTextFieldConfigurator(
    val error: String? = null,
    val isEnabled: Boolean = true,
    val isAnimated: Boolean = false,
    val isPassword: Boolean = false,
    val percentCorners: Int = 20,
    val animatedProgress: Float = 0f
)

@Composable
fun CustomTextField(
    modifier: Modifier,
    configuratorClosure: () -> CustomTextFieldConfigurator,
    label: String,
    text: State<String>,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextChange: (String) -> Unit
) {
    val configurator = configuratorClosure()
    val animationContainerColor = MaterialTheme.colorScheme.secondaryContainer
    var showPassword by rememberSaveable { mutableStateOf(value = configurator.isPassword) }

    ExtractedOutlinedTextField(if (configurator.isAnimated) "" else {
        text.value
    },
        modifier = modifier.drawWithCache {
            onDrawWithContent {
                if (configurator.isAnimated) {
                    val offset = Offset(0f, 10.dp.toPx())
                    val modifiedSize = this.size.copy(height = this.size.height - 25.dp.toPx())
                    val radius = lerp(
                        modifiedSize.height * 0.01f * configurator.percentCorners,
                        modifiedSize.height,
                        configurator.animatedProgress
                    )
                    drawRoundRect(
                        color = animationContainerColor,
                        topLeft = offset,
                        size = modifiedSize,
                        cornerRadius = CornerRadius(radius)
                    )
                } else {
                    this@onDrawWithContent.drawContent()
                }
            }
        },
        onValueChange = if (configurator.isAnimated) {
            {}
        } else onTextChange,
        enabled = configurator.isEnabled && !configurator.isAnimated,
        isError = if (configurator.isAnimated) false else configurator.error != null,
        visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions.copy(keyboardType = if (configurator.isPassword) KeyboardType.Password else KeyboardType.Text),
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(percent = configurator.percentCorners),
        label = if (configurator.isAnimated) null else {
            { Text(label) }
        },
        supportingText = {
            Text(configurator.error?.takeIf { !configurator.isAnimated }.orEmpty())
        },
        trailingIcon = {
            if (configurator.isPassword) {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(Icons.Filled.Visibility, contentDescription = "Show password")
                    }
                } else {
                    IconButton(onClick = { showPassword = true }) {
                        Icon(Icons.Filled.VisibilityOff, contentDescription = "Hide password")
                    }
                }
            }
        },
        contentPadding = PaddingValues(20.dp),
        borderStrokeThickness = 2.dp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExtractedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    borderStrokeThickness: Dp = OutlinedTextFieldDefaults.FocusedBorderThickness,
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding()
) {

    val textColor = textStyle.color.takeOrElse {
        extractedTextColor(enabled, isError, colors, interactionSource).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
    BasicTextField(value = value,
        modifier = if (label != null) {
            modifier
                .semantics(mergeDescendants = true) {}
                .padding(top = 8.dp)
        } else {
            modifier
        },
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(extractedCursorColor(isError, colors).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                prefix = prefix,
                suffix = suffix,
                supportingText = supportingText,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = contentPadding,
                container = {
                    Container(
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = shape,
                        focusedBorderThickness = borderStrokeThickness,
                        unfocusedBorderThickness = borderStrokeThickness,
                    )
                })
        })
}

@Composable
private fun extractedTextColor(
    enabled: Boolean,
    isError: Boolean,
    textFieldColors: TextFieldColors,
    interactionSource: InteractionSource
): State<Color> {
    val focused by interactionSource.collectIsFocusedAsState()

    val targetValue = when {
        !enabled -> textFieldColors.disabledTextColor
        isError -> textFieldColors.errorTextColor
        focused -> textFieldColors.focusedTextColor
        else -> textFieldColors.unfocusedTextColor
    }
    return rememberUpdatedState(targetValue)
}

@Composable
private fun extractedCursorColor(
    isError: Boolean, textFieldColors: TextFieldColors
): State<Color> {
    return rememberUpdatedState(if (isError) textFieldColors.errorCursorColor else textFieldColors.cursorColor)
}

@Preview
@Composable
private fun PreviewCustomTextField() {
    HotMoviesAppComposeTheme {
        var text = remember { mutableStateOf("Test text") }
        val configurator = CustomTextFieldConfigurator(
            isPassword = false,
        )
        CustomTextField(Modifier.fillMaxWidth(),
            { configurator },
            label = "Label",
            text,
            onTextChange = { text.value = it })
    }
}