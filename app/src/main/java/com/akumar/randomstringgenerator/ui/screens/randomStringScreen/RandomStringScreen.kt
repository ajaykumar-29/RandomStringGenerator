package com.akumar.randomstringgenerator.ui.screens.randomStringScreen

import android.util.Log
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.akumar.randomstringgenerator.R
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import com.akumar.randomstringgenerator.ui.common.CopyToClipboardButton
import com.akumar.randomstringgenerator.ui.common.DeleteDialogBox
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RandomStringScreen(
    modifier: Modifier = Modifier,
    viewModel: RandomStringViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val inputValue by viewModel.inputValue.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val keyBoardController = LocalSoftwareKeyboardController.current
    val randomStringList by viewModel.randomStringList.collectAsState()
    val result by viewModel.result.collectAsState()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val shouldShowGoToTopButton by
    remember { derivedStateOf { lazyColumnState.firstVisibleItemIndex > 0 } }

    LaunchedEffect(randomStringList) { lazyColumnState.animateScrollToItem(0) }
    SideEffect {
        Log.d("SideEffects", "RandomStringScreen Recomposed")
    }

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
                            value = inputValue,
                            label = { Text(text = stringResource(R.string.enter_string_size)) },
                            onValueChange = {
                                if (it != "") {
                                    if (viewModel.validateInput(it)) viewModel.setInputValue(it)
                                } else viewModel.setInputValue(it)
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number, imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(onSearch = {
                                if (viewModel.validateInput(inputValue))
                                    viewModel.fetchRandomString(inputValue.toInt())
                                keyBoardController?.hide()
                            }),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        AnimatedVisibility(
                            visible = errorMessage != "",
                            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                                animationSpec = tween(1000)
                            )
                        ) {
                            Text(
                                text = errorMessage,
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
                        if (viewModel.validateInput(inputValue))
                            viewModel.fetchRandomString(inputValue.toInt())
                        keyBoardController?.hide()
                    }) {
                        Text(stringResource(R.string.generate_string))
                    }
                    AnimatedVisibility(visible = randomStringList.isNotEmpty()) {
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
            }
            if (randomStringList.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_data),
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = lazyColumnState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(randomStringList, key = { item -> item.value }) { randomString ->
                            StringCard(randomString, deleteString = { viewModel.deleteString(it) })
                        }
                    }
                    if (shouldShowGoToTopButton) {
                        FloatingActionButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 8.dp, bottom = 8.dp),
                            onClick = {
                                coroutineScope.launch { lazyColumnState.animateScrollToItem(0) }
                            }) {
                            Icon(painter = painterResource(R.drawable.go_to_top), "go to top")
                        }
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
        when (result) {
            is RandomStringFetchResult.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }

            is RandomStringFetchResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator(modifier = Modifier.size(100.dp))
                }
            }

            else -> Unit
        }
    }
}

@Composable
fun StringCard(item: RandomStringItem, deleteString: (item: RandomStringItem) -> Unit) {
    val showItemDeleteDialog = remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var realLineCount by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { expanded = expanded.not() }
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
            Row {
                CopyToClipboardButton(item.value)
                FilledTonalIconButton(
                    onClick = { showItemDeleteDialog.value = true },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Text(
                text = item.value,
                fontStyle = FontStyle.Italic,
                maxLines = if (expanded) Int.MAX_VALUE else 4
            )
        }
        if (realLineCount > 4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilledTonalButton(
                    onClick = { expanded = expanded.not() }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(if (expanded) R.string.less else R.string.more))
                        Icon(
                            painter = if (expanded) painterResource(R.drawable.expand_less) else
                                painterResource(R.drawable.expand_more),
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                }
            }
        }
    }
    // Invisible text to know size of the text
    Text(
        text = item.value,
        fontStyle = FontStyle.Italic,
        maxLines = Int.MAX_VALUE,
        onTextLayout = {
            realLineCount = it.lineCount
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(0.dp)
            .alpha(0f)
            .padding(0.dp) // Prevents layout size change
    )
    if (showItemDeleteDialog.value) {
        DeleteDialogBox(onConfirm = {
            deleteString(item)
            showItemDeleteDialog.value = false
        }, onDismiss = {
            showItemDeleteDialog.value = false
        }, message = stringResource(R.string.delete_item))
    }
}