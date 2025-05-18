package com.akumar.randomstringgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.akumar.randomstringgenerator.ui.common.ThemeChangeButton
import com.akumar.randomstringgenerator.ui.screens.randomStringScreen.RandomStringScreen
import com.akumar.randomstringgenerator.ui.screens.randomStringScreen.RandomStringViewModel
import com.akumar.randomstringgenerator.ui.screens.randomStringScreen.RandomStringViewModelFactory
import com.akumar.randomstringgenerator.ui.theme.RandomStringGeneratorTheme

class MainActivity : ComponentActivity() {
    private val viewModel: RandomStringViewModel by viewModels {
        RandomStringViewModelFactory(application.contentResolver)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomStringGeneratorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(stringResource(R.string.app_name)) },
                            actions = { ThemeChangeButton() }
                        )
                    }
                ) { innerPadding ->
                    RandomStringScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomStringGeneratorTheme {
    }
}