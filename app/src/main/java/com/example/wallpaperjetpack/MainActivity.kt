package com.example.wallpaperjetpack

import android.app.WallpaperManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
                                }, collection = collectionName.value, prefs = prefs)
                            }
                            composable(route = Screens.Collections.name){
                                imageScreen(backButton = { navController.navigate(Screens.Start.name) }, 
                                    cardClick = {navController.navigate(Screens.Wallpaper.name)},
                                    collection = collectionName.value)
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
    val context = LocalContext.current
    Box() {
        Scaffold(
            bottomBar = {
                BottomAppBar {
                    Button(
                        onClick = {
                                  setWallpaper(context = context, resid = R.drawable.ic_opp)
                                  },
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
fun StartScreen(cardClick: (input: String) -> Unit, prefs: SharedPreferences, collection: String) {
    //val context = LocalContext.current
    val collectionsArt = listOf(
        "Abstract Reality", "Beautiful Diversity",
        "Classical Biomes", "Divine Emotions",
        "Dreamscapes", "Harmony of Hues", "Look of the Wild",
        "On the Road Again", "Opposites Attract",
        "Seasons of Time", "Vivid Psalms", "Water Birds"
    )

    val collectionsPhoto = listOf(
        "Beautiful Earth", "Entropy Earth", "Fingerprint Earth",
        "Intimate Earth",
    )

    val residsPhoto = listOf(
        R.drawable.beautiful_earth, R.drawable.entropy, R.drawable.fingerprint ,R.drawable.intimate
    )

    val residsArt = listOf(
        R.drawable.abstract_real, R.drawable.diversity,R.drawable.biomes, R.drawable.emotions,
        R.drawable.dreamscapes, R.drawable.hues,
        R.drawable.wild, R.drawable.cars, R.drawable.opposites,
        R.drawable.seasons, R.drawable.psalms, R.drawable.water_birds
    )

    val artContext = remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            topbar(prefs = prefs, collection = collection)
        },
        bottomBar = {
            bottombar(artContext)
        }
    ) {

        LazyColumn(modifier = Modifier
            .height(630.dp)
            .padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (artContext.value) {
                items(collectionsArt.size / 2) { index ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)) {

                        card(text = collectionsArt[index * 2], residsArt[index * 2], cardClick, prefs = prefs)
                        card(text = collectionsArt[(index * 2) + 1], residsArt[(index * 2) + 1], cardClick, prefs = prefs)


                    }
                }
            }else{
                items(collectionsPhoto.size / 2) { index ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)) {
                        card(text = collectionsPhoto[index * 2], residsPhoto[index * 2], cardClick, prefs = prefs)
                        card(text = collectionsPhoto[(index * 2) + 1], residsPhoto[(index * 2) + 1], cardClick, prefs = prefs)
                    }
                }
            }
        }
    }
}

@Composable
fun card(text: String, resid: Int, click: (input: String) -> Unit, prefs: SharedPreferences){
    //val context = LocalContext.current
    val collections = prefs.getString("collections", "none")

    val collectionArray = collections?.split(",")
    val checked = remember { mutableStateOf(false) }
    collectionArray?.forEach {
         if (it == text) checked.value = true
    }

    Card(modifier = Modifier
        .padding(5.dp)
        .clickable {
            click(text)
        }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box() {
                Image(
                    painter = painterResource(id = resid), contentDescription = "Hello",
                    modifier = Modifier
                        .size(190.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
                Box(modifier = Modifier.offset(x = 155.dp, y = 10.dp)) {
                    CustomCheckbox(click = {text ->
                        with(prefs.edit()){
                            val collectionsNew = "$collections,$text"
                            putString("collections", collectionsNew)
                            apply()
                        }
                    }, size = 25.dp, isChecked = checked.value, collection = text)
                }
            }
            Text(text = text, modifier = Modifier.padding(vertical = 5.dp))
        }
    }
}
@Composable
fun bottombar(artContext: MutableState<Boolean>){
    var context = LocalContext.current

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.background)) {
        Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .width(200.dp)
            .padding(5.dp)
            .clip(
                RoundedCornerShape(3.dp)
            )
            .clickable {
                artContext.value = true;
            }) {
            Image(painter = painterResource(id = R.drawable.ic_baseline_palette_24), contentDescription = "Ai Art", modifier = Modifier.size(40.dp))
            Text(text = "Ai Art")
        }
        Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .width(200.dp)
            .padding(5.dp)
            .clip(
                RoundedCornerShape(3.dp)
            )
            .clickable {
                artContext.value = false;
            }) {
            Image(painter = painterResource(id = R.drawable.ic_baseline_camera_alt_24), contentDescription = "Photography", modifier = Modifier.size(40.dp))
            Text(text = "Photography")
        }
    }
}

@Composable
fun topbar(prefs: SharedPreferences, collection: String) {
    val time = remember {
        mutableStateOf(
            prefs.getString("time", "Never")
        )
    }
    val expanded = remember { mutableStateOf(false) }
    val timeZones = listOf(
        "Never", "Every Day", "Every Other Day", "Every Week", "Every Month"
    )
    Column(modifier = Modifier.background(Color.White)) {
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "logo", modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(
                RoundedCornerShape(5.dp)
            )
            .size(70.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(vertical = 10.dp)
                    .clickable { expanded.value = true }) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = stringResource(R.string.dropdownmsg) + " " + time.value,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                        contentDescription = "Dropdown",
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .size(30.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }) {
                    Column(
                        modifier = Modifier
                            .width(310.dp)
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
        }
    }
}
@Composable
fun imageScreen(collection: String, backButton: () -> Unit, cardClick: () -> Unit) {

    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        Text(text = collection, modifier = Modifier.padding(vertical = 5.dp))
//
//            val url = "https://jsonplaceholder.typicode.com/users"
//            val queue = Volley.newRequestQueue(context);
//            //var rtnArray = remember { mutableStateOf(arrayOf<T>()) }
//            Log.e("MSG", "Sent")
//            val request = StringRequest(Request.Method.GET, url,
//                { response ->
//                    Log.e("MSG", response.toString())
//                },
//                Response.ErrorListener {
//
//                })
//            queue.add(request)


        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(9) { index ->
                Row() {
                    ImgCard(cardClick, R.drawable.ic_opp)
                    ImgCard(cardClick, R.drawable.ic_opp)
                }
            }
        }
    }
}

@Composable
fun CustomCheckbox(click: (String) -> Unit, size: Dp, isChecked: Boolean, collection: String){
    val isCheck = remember { mutableStateOf(isChecked) }
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(2.dp))
            .border(3.dp, color = MaterialTheme.colors.primary, RoundedCornerShape(3.dp))
            .background(if (isCheck.value) MaterialTheme.colors.primary else Color.Transparent)
            .clickable {
                isCheck.value = !isCheck.value
                click(collection)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WallpaperjetpackTheme {
       // bottombar()
    }
}