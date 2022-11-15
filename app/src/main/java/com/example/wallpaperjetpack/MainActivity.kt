package com.example.wallpaperjetpack

import android.app.WallpaperManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                    val collectionName = remember { mutableStateOf("Abstract") }
                    val navController = rememberNavController()
                    Scaffold() {
                        NavHost(navController = navController,
                            startDestination = Screens.Start.name
                        ){
                                //https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#4
                            composable(route = Screens.Start.name){
                                StartScreen(cardClick = {name: String ->
                                    navController.navigate(Screens.Collections.name)
                                    collectionName.value = name
                                })
                            }
                            composable(route = Screens.Collections.name){
                                val context = LocalContext.current
                                imageScreen(collection = collectionName.value) { navController.navigate(Screens.Start.name) }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class Screens() {
    Start,
    Collections
}

@Composable
fun imageScreen(collection: String, click: () -> Unit){
    SimpleButton(func = click, buttonText = "$collection")
}

@Composable
fun StartScreen(cardClick: (input: String) -> Unit) {
    val context = LocalContext.current
    val collections = listOf(
        "Abstract Reality", "Beautiful Diversity",
        "Beautiful Earth", "Classical Biomes", "Divine Emotions",
        "Dreamscapes", "Entropy Earth", "Fingerprint Earth", "Harmony of Hues",
        "Intimate Earth", "Look of the Wild", "On the Road Again", "Opposites Attract",
        "Seasons of Time", "Vivid Psalms", "Water Birds"
    )

    LazyColumn() {
        items(collections.size) { index ->
            card(text = collections[index], R.drawable.ic_opp, cardClick)
        }
    }
}

fun setWallpaper(resid: Int, context: Context){
    val wallpaperManager = WallpaperManager.getInstance(context)
    try {
        wallpaperManager.setResource(resid)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@Composable
fun SimpleButton(func: () -> Unit, buttonText: String) {
    val context = LocalContext.current
    Button(onClick = {
        func()
    }, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        Text(text = buttonText, color = MaterialTheme.colors.primaryVariant)
    }
}


@Composable
fun card(text: String, resid: Int, click: (input: String) -> Unit){
    val grad = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.primaryVariant
        )
    )
    val context = LocalContext.current

    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
        ) {
        Row(modifier = Modifier
            .background(grad)
            .clickable {
                click(text)
            }) {
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
                contentDescription = "Collection Image",
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
        //StartScreen()
    }
}