package com.safescroll.freevirtualgf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.safescroll.freevirtualgf.ui.companion.VRMCompanionScreen
import com.safescroll.freevirtualgf.ui.theme.FreevirtualgfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreevirtualgfTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VRMCompanionScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}