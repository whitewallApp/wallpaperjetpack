package com.example.wallpaperjetpack

import android.app.WallpaperManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wallpaperjetpack.ui.theme.WallpaperjetpackTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WallpaperjetpackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    StartScreen("Android")
                }
            }
        }
    }
}

@Composable
fun StartScreen(name: String) {
    Text(text = "Hello $name!")
    Image(
        painterResource(id = R.drawable.ic_opp),
        contentDescription = stringResource(id = R.string.testing),
        modifier = Modifier.fillMaxSize()
    )
    SimpleButton()
}

@Composable
fun SimpleButton() {
    val context = LocalContext.current
    Button(onClick = {
        Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT)
    }, modifier = Modifier.size(100.dp)) {
        Text(text = "Simple Button")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WallpaperjetpackTheme {
        StartScreen("Android")
    }
}