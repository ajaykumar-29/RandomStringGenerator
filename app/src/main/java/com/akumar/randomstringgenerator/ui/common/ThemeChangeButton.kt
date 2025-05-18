package com.akumar.randomstringgenerator.ui.common

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.akumar.randomstringgenerator.R
import com.akumar.randomstringgenerator.data.model.ThemeMode
import kotlinx.coroutines.launch
import androidx.core.content.edit


@Composable
fun ThemeChangeButton() {
    val context = LocalContext.current
    val appSharedPreferences = remember {
        context.getSharedPreferences("appPreference", Context.MODE_PRIVATE)
    }

    var currTheme by remember {
        mutableStateOf(
            appSharedPreferences.getString("appTheme", ThemeMode.DEFAULT.name.lowercase())
        )
    }

    val coroutineScope = rememberCoroutineScope()

    FilledTonalIconButton(onClick = {
        coroutineScope.launch {
            currTheme = if (currTheme == ThemeMode.LIGHT.name.lowercase()) {
                ThemeMode.DARK.name.lowercase()
            } else {
                ThemeMode.LIGHT.name.lowercase()
            }

            appSharedPreferences.edit {
                putString("appTheme", currTheme)
            }
        }
    }) {
        Icon(
            painter = painterResource(
                if (currTheme == ThemeMode.LIGHT.name.lowercase()) R.drawable.dark_mode else R.drawable.light_mode
            ),
            contentDescription = "change theme"
        )
    }
}
