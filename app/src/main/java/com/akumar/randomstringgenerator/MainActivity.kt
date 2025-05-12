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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.akumar.randomstringgenerator.repository.RandomStringRepository
import com.akumar.randomstringgenerator.ui.screens.RandomStringScreen
import com.akumar.randomstringgenerator.ui.screens.RandomStringViewModel
import com.akumar.randomstringgenerator.ui.screens.RandomStringViewModelFactory
import com.akumar.randomstringgenerator.ui.theme.RandomStringGeneratorTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: RandomStringViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = RandomStringRepository(application.contentResolver)
        val factory = RandomStringViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(RandomStringViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            RandomStringGeneratorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(stringResource(R.string.app_name))
                            }
                        })
                    }
                ) { innerPadding ->
                    RandomStringScreen(viewModel = viewModel, Modifier.padding(innerPadding))
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