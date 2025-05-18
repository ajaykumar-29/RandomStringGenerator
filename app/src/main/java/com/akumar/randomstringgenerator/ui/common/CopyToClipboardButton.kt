package com.akumar.randomstringgenerator.ui.common

import android.content.ClipData
import android.widget.Toast
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.akumar.randomstringgenerator.R
import kotlinx.coroutines.launch

@Composable
fun CopyToClipboardButton(textToCopy: CharSequence) {
    val clipboardManager = LocalClipboard.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    FilledTonalIconButton(onClick = {
        coroutineScope.launch {
            clipboardManager.setClipEntry(
                ClipEntry(ClipData.newPlainText("Plain Text", textToCopy))
            )
        }
        Toast.makeText(context, "Copy to clipboard", Toast.LENGTH_SHORT).show()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.copy), // Your copy icon
            contentDescription = "Copy to clipboard"
        )
    }
}