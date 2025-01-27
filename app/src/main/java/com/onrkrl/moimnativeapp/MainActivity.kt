package com.onrkrl.moimnativeapp

import WebViewScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onrkrl.moimnativeapp.ui.theme.MoimNativeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoimNativeAppTheme {
                    MyApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    val baseURL = "https://moim-web-app.web.app/"
    val params = WebViewParams(token = "tokenValue", msisdn = "msisdnValue")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
        ) {
            WebViewScreen(baseURL = baseURL, params = params)

        }
    }

}
