package com.example.wallpaperjetpack

import android.app.WallpaperManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                    val collectionClicked = remember { mutableStateOf(false)}
                    var prefs: SharedPreferences = getPreferences(Context.MODE_PRIVATE)

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
                                imageScreen(collection = collectionName.value, backButton = { navController.navigate(Screens.Start.name) }, prefs = prefs, cardClick = {})
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
fun StartScreen(cardClick: (input: String) -> Unit) {
    val context = LocalContext.current
    val collections = listOf(
        "Abstract Reality", "Beautiful Diversity",
        "Beautiful Earth", "Classical Biomes", "Divine Emotions",
        "Dreamscapes", "Entropy Earth", "Fingerprint Earth", "Harmony of Hues",
        "Intimate Earth", "Look of the Wild", "On the Road Again", "Opposites Attract",
        "Seasons of Time", "Vivid Psalms", "Water Birds"
    )

    val resids = listOf(
        R.drawable.abstract_real, R.drawable.diversity,
        R.drawable.beautiful_earth, R.drawable.biomes, R.drawable.emotions,
        R.drawable.dreamscapes, R.drawable.entropy, R.drawable.fingerprint, R.drawable.hues,
        R.drawable.intimate, R.drawable.wild, R.drawable.cars, R.drawable.opposites,
        R.drawable.seasons, R.drawable.psalms, R.drawable.water_birds
    )


    LazyColumn() {
        items(collections.size) { index ->
            card(text = collections[index],resids[index], cardClick)
        }
    }
}

@Composable
fun imageScreen(collection: String, backButton: () -> Unit, cardClick: (input: String) -> Unit, prefs: SharedPreferences){

    val context = LocalContext.current

    Column() {
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
            onClick = {
                backButton()
            }) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                contentDescription = "Back Arrow",
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(text = "Back", color = Color.White)
        }
        val expanded = remember { mutableStateOf(false) }
        val checked = remember { mutableStateOf(prefs.getBoolean("checked", false)) }

        Row(horizontalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(horizontal = 10.dp)
                    .clickable { expanded.value = true }) {
                Text(text = "Time", modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(10.dp))
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (i in 0..4) {
                            Text(
                                text = "Option $i",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
            Checkbox(checked = checked.value, onCheckedChange = {
                checked.value = it

                with(prefs.edit()) {
                    putBoolean("checked", checked.value)
                    putString("Collection", collection)
                    apply()
                }
                                                                }, modifier = Modifier.width(500.dp)
            )
        }

        //    val url = "http://seekingzionorg.ipage.com/thebeautifulai/wp-content/uploads/2022/09/home_hero-1054x1536.png"
        //    val queue = Volley.newRequestQueue(context);
        //    //var rtnArray = remember { mutableStateOf(arrayOf<T>()) }
        //
        //    val request = StringRequest(Request.Method.GET, url,
        //        { response ->
        //           Log.e("MSG", response.toString())
        //        },
        //        Response.ErrorListener {
        //
        //        })
        //    queue.add(request)

        Row() {
            LazyColumn() {
                items(9) { index ->
                    Row() {
                        imgCard({}, R.drawable.ic_opp)
                        imgCard({}, R.drawable.ic_opp)
                    }
                }
            }
//            LazyColumn() {
//                items(9) { index ->
//                    imgCard({}, R.drawable.ic_opp)
//                }
//            }
        }
    }

}

@Composable
fun imgCard(oncick: () -> Unit, resid: Int){
    Card(modifier = Modifier.padding(25.dp)) {
        Image(painter = painterResource(id = resid), contentDescription = "Image", modifier=Modifier.width(150.dp))
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
fun card(text: String, resid: Int, click: (input: String) -> Unit){
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
    ) {
        Row(modifier = Modifier
           // .background(MaterialTheme.colors.primary)
            .clickable {
                click(text)
            }, horizontalArrangement = Arrangement.Center) {
            Text(
                text = text,
                Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 30.dp)
                    .padding(horizontal = 15.dp),
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
            Box() {
                Image(
                    painter = painterResource(id = resid),
                    contentDescription = "Collection Image",
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WallpaperjetpackTheme {
        imageScreen(collection = "Abstract") {  }
    }
}