package com.kuriamind

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.kuriamind.navigation.AppNavGraph
import com.kuriamind.navigation.Main
import com.kuriamind.navigation.Welcome
import com.kuriamind.ui.theme.KuriamindTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val lang = KuriamindApplication.loadAppLanguage(newBase)
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration).apply {
            setLocale(locale)
        }
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeMode = KuriamindApplication.loadAppTheme(this)
        setContent {
            KuriamindTheme(themeMode = themeMode) {
                KuriamindNavHost()
            }
        }
    }
}

@Composable
private fun KuriamindNavHost() {
    val prefs = LocalContext.current
        .getSharedPreferences("kuriamind_prefs", Context.MODE_PRIVATE)

    val isFirstTime = prefs.getBoolean("is_first_time", true)
    val startDestination: Any = if (isFirstTime) Welcome else Main

    val navController = rememberNavController()

    AppNavGraph(
        navController = navController,
        startDestination = startDestination,
    )
}
