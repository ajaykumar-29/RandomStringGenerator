package com.akumar.randomstringgenerator.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.akumar.randomstringgenerator.R

@Composable
fun DeleteDialogBox(
    modifier: Modifier = Modifier,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(modifier = modifier, onDismissRequest = onDismiss, icon = {
        Icon(
            painter = painterResource(R.drawable.delete),
            contentDescription = "delete",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(56.dp),
        )
    }, confirmButton = {
        Button(
            onClick = onConfirm, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) { Text(stringResource(R.string.delete)) }
    }, dismissButton = {
        Button(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
    }, text = { Text(message) })
}