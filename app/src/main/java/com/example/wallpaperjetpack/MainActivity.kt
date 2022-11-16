package com.example.wallpaperjetpack

import android.app.WallpaperManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
                    val prefs: SharedPreferences = getPreferences(Context.MODE_PRIVATE)

                    Scaffold {
                        NavHost(navController = navController,
                            startDestination = Screens.Start.name
                        ){
                            composable(route = Screens.Start.name){
                                StartScreen(cardClick = {name: String ->
                                    navController.navigate(Screens.Collections.name)
                                    collectionName.value = name
                                })
                            }
                            composable(route = Screens.Collections.name){
                                imageScreen(collection = collectionName.value, backButton = { navController.navigate(Screens.Start.name) }, prefs = prefs, cardClick = {navController.navigate(Screens.Wallpaper.name)})
                            }
                            composable(route = Screens.Wallpaper.name){
                                wallpaperSet()
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class Screens {
    Start,
    Collections,
    Wallpaper
}

@Composable
fun wallpaperSet() {
    Box() {
        Scaffold(
            bottomBar = {
                BottomAppBar {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(text = "Set As Wallpaper", color = Color.White)
                    }
                }
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_opp), contentDescription = "Wallpaper",
                contentScale = ContentScale.FillHeight
            )
        }
    }

}

@Composable
fun StartScreen(cardClick: (input: String) -> Unit) {
    //val context = LocalContext.current
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
fun imageScreen(collection: String, backButton: () -> Unit, cardClick: () -> Unit, prefs: SharedPreferences){

    val context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }
    val time = remember { mutableStateOf(if (prefs.getString("collection", "none") == collection) prefs.getString("time", "Never") else "Never") }
    val timeZones = listOf(
        "Never", "Every Day", "Every Other Day", "Every Week", "Every Month"
    )

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

        Row(horizontalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(horizontal = 20.dp)
                    .clickable { expanded.value = true }) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = "Change Wallpaper: " + time.value, modifier = Modifier
                        .padding(10.dp))
                    Image(painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24), contentDescription = "hello", modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(30.dp))
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (i in 0 until timeZones.size - 1) {
                            Box(modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable {
                                    time.value = timeZones[i]
                                    with(prefs.edit()) {
                                        putString("time", time.value)
                                        apply()
                                    }
                                }
                            ) {
                                Text(
                                    text = timeZones[i],
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(4.dp))
                                        .padding(15.dp),
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
            CustomCheckbox(click = { checked: Boolean ->
                with(prefs.edit()) {
                    putBoolean("checked", checked)
                    putString("collection", collection)
                    apply()
                } }, size = 40.dp, prefs = prefs, collection = collection)
        }

            val url = "https://jsonplaceholder.typicode.com/users"
            val queue = Volley.newRequestQueue(context);
            //var rtnArray = remember { mutableStateOf(arrayOf<T>()) }
            Log.e("MSG", "Sent")
            val request = StringRequest(Request.Method.GET, url,
                { response ->
                    Log.e("MSG", response.toString())
                },
                Response.ErrorListener {

                })
            queue.add(request)

        Row() {
            LazyColumn() {
                items(9) { index ->
                    Row() {
                        ImgCard(cardClick, R.drawable.ic_opp)
                        ImgCard(cardClick, R.drawable.ic_opp)
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
fun CustomCheckbox(click: (Boolean) -> Unit, size: Dp, prefs: SharedPreferences, collection: String){
    val isCheck = remember { mutableStateOf(
        if (prefs.getString("collection", "none") == collection) {
            prefs.getBoolean("checked", false)
        }else{
            false
        }
    ) }
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(3.dp))
            .border(3.dp, color = MaterialTheme.colors.primary, RoundedCornerShape(3.dp))
            .background(if (isCheck.value) MaterialTheme.colors.primary else Color.Transparent)
            .clickable {
                isCheck.value = !isCheck.value
                click(isCheck.value)
            }
    ) {
        if(isCheck.value)
            Icon(Icons.Default.Check, contentDescription = "", tint = Color.White, modifier = Modifier.size(size + 5.dp))
    }
}

@Composable
fun ImgCard(oncick: () -> Unit, resid: Int){
    Card(modifier = Modifier
        .padding(25.dp)
        .clickable {
            oncick()
        }) {
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
    //val context = LocalContext.current

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
        wallpaperSet()
    }
}