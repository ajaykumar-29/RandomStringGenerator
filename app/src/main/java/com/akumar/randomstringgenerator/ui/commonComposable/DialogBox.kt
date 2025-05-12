package com.akumar.randomstringgenerator.ui.commonComposable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akumar.randomstringgenerator.ui.theme.RandomStringGeneratorTheme

@Composable
fun DeleteDialogBox(
    modifier: Modifier = Modifier,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(modifier = modifier, onDismissRequest = {}, icon = {
        Icon(
            Icons.Filled.Delete,
            "delete",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(56.dp),
        )
    }, confirmButton = {
        Button(
            onClick = { onConfirm() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) { Text("Delete") }
    }, dismissButton = {
        Button(
            onClick = { onDismiss() }) { Text("Cancel") }
    }, text = {
        Text(message)
    })
}