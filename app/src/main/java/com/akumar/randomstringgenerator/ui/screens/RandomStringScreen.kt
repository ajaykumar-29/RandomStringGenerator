package com.akumar.randomstringgenerator.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akumar.randomstringgenerator.R
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import com.akumar.randomstringgenerator.ui.commonComposable.DeleteDialogBox

@Composable
fun RandomStringScreen(viewModel: RandomStringViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val inputValue = remember { mutableStateOf("") }
    val errorMessage = viewModel.errorMessage.collectAsState()
    val keyBoardController = LocalSoftwareKeyboardController.current
    val randomStringList = viewModel.randomStringList.collectAsState()
    val result = viewModel.result.collectAsState()
    val showDeleteDialog = remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 26.dp)
                    ) {
                        OutlinedTextField(
                            value = inputValue.value,
                            label = { Text(text = stringResource(R.string.enter_string_size)) },
                            onValueChange = {
                                if (it != "") {
                                    if (viewModel.validateInput(it)) inputValue.value = it
                                } else inputValue.value = it
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number, imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(onSearch = {
                                if (viewModel.validateInput(inputValue.value))
                                    viewModel.fetchRandomString(inputValue.value.toInt())
                                keyBoardController?.hide()
                            }),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        AnimatedVisibility(
                            visible = errorMessage.value != "",
                            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                                animationSpec = tween(1000)
                            )
                        ) {
                            Text(
                                text = errorMessage.value,
                                modifier = Modifier.padding(vertical = 4.dp),
                                fontSize = 12.sp,
                                color = Color(0xFFFF0000),
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .animateContentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(modifier = Modifier.padding(horizontal = 10.dp), onClick = {
                        if (viewModel.validateInput(inputValue.value))
                            viewModel.fetchRandomString(inputValue.value.toInt())
                        keyBoardController?.hide()
                    }) {
                        Text(stringResource(R.string.generate_string))
                    }
                    Button(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = { showDeleteDialog.value = true }) {
                        Text(stringResource(R.string.delete_all))
                    }
                }
            }
            if (randomStringList.value.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_data),
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(randomStringList.value) { randomString ->
                        StringCard(randomString, deleteString = { viewModel.deleteString(it) })
                    }
                }
            }
        }
        if (showDeleteDialog.value) {
            DeleteDialogBox(onConfirm = {
                viewModel.deleteAllStrings()
                showDeleteDialog.value = false
            }, onDismiss = {
                showDeleteDialog.value = false
            }, message = stringResource(R.string.delete_all_strings))
        }
        when (result.value) {
            is RandomStringFetchResult.Error -> {
                Toast.makeText(context, result.value.message, Toast.LENGTH_SHORT).show()
            }

            is RandomStringFetchResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                    )
                }
            }

            is RandomStringFetchResult.Success -> {}
            is RandomStringFetchResult.None -> {}
        }
    }
}

@Composable
fun StringCard(item: RandomStringItem, deleteString: (item: RandomStringItem) -> Unit) {
    val showItemDeleteDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    item.created,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
                Text("Length: ${item.length}")
            }
            FilledTonalIconButton(
                onClick = { showItemDeleteDialog.value = true },
                colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Text(item.value, fontStyle = FontStyle.Italic)
        }
    }
    if (showItemDeleteDialog.value) {
        DeleteDialogBox(onConfirm = {
            deleteString(item)
            showItemDeleteDialog.value = false
        }, onDismiss = {
            showItemDeleteDialog.value = false
        }, message = stringResource(R.string.delete_item))
    }
}