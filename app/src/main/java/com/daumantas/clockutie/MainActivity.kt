package com.daumantas.clockutie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.daumantas.clockutie.ui.Clock
import com.daumantas.clockutie.ui.theme.ClockutieTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendar = Calendar.getInstance()
        val currentHours = calendar.get(Calendar.HOUR)
        val currentMins = calendar.get(Calendar.MINUTE)
        val currentSecs = calendar.get(Calendar.SECOND)

        setContent {
            ClockutieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Clock(currentHours, currentMins, currentSecs)
                }
            }
        }
    }
}

