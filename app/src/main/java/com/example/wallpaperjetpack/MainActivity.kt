package com.example.wallpaperjetpack

import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wallpaperjetpack.ui.theme.WallpaperjetpackTheme
import java.io.IOException


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
                    StartScreen()
                }
            }
        }
    }
}

@Composable
fun StartScreen() {
    val context = LocalContext.current
    val collections = listOf("Abstract Reality", "Beautiful Diversity",
        "Beautiful Earth", "Classical Biomes", "Divine Emotions",
        "Dreamscapes", "Entropy Earth", "Fingerprint Earth", "Harmony of Hues",
        "Intimate Earth", "Look of the Wild", "On the Road Again", "Opposites Attract",
        "Seasons of Time", "Vivid Psalms", "Water Birds")

    LazyColumn() {
        items(collections.size) { index ->
            card(text = collections[index], R.drawable.ic_opp)
            //SimpleButton {setWallpaper(R.drawable.ic_opp, context)}
        }
    }
}

    //SimpleButton {setWallpaper(R.drawable.ic_opp, context)}


fun setWallpaper(resid: Int, context: Context){
    val wallpaperManager = WallpaperManager.getInstance(context)
    try {
        wallpaperManager.setResource(resid)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@Composable
fun SimpleButton(func: () -> Unit) {
    val context = LocalContext.current
    Button(onClick = {
        func()
        Toast.makeText(context, Resources.getSystem().getString(R.string.sucess_msg), Toast.LENGTH_SHORT).show()
    }, modifier = Modifier.size(50.dp, 50.dp)) {
        Text(text = "Simple Button")
    }
}


@Composable
fun card(text: String, resid: Int){
    val context = LocalContext.current
    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
        onClick = {
            Toast.makeText(context, "Clicked $text", Toast.LENGTH_SHORT).show()
        }) {
        Row() {
            Text(
                text = text,
                Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 30.dp)
                    .padding(horizontal = 15.dp),
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
            Image(
                painter = painterResource(id = resid),
                contentDescription = Resources.getSystem().getString(R.string.image_desc),
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WallpaperjetpackTheme {
        StartScreen()
    }
}