package com.tarekbohdima.bakingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tarekbohdima.bakingapp.ui.navigation.BakingNavGraph
import com.tarekbohdima.bakingapp.ui.theme.BakingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BakingAppTheme {
                BakingNavGraph()
            }
        }
    }
}
