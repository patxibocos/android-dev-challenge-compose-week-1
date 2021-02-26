/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainViewModel : ViewModel() {
    private val _currentScreen = MutableLiveData<Screen>(Screen.CatsList)
    val currentScreen: LiveData<Screen> = _currentScreen

    fun onBackPressed(): Boolean {
        val whatever = _currentScreen.value == Screen.CatsList
        _currentScreen.value = Screen.CatsList
        return whatever
    }

    fun catSelected(cat: Cat) {
        _currentScreen.value = Screen.CatDetails(cat)
    }
}

class MainActivity : AppCompatActivity() {
    private val navigationViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val currentScreen: Screen by navigationViewModel.currentScreen.observeAsState(Screen.CatsList)
                MyApp(currentScreen) { cat ->
                    navigationViewModel.catSelected(cat)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (navigationViewModel.onBackPressed()) {
            super.onBackPressed()
        }
    }
}

sealed class Screen {
    object CatsList : Screen()
    class CatDetails(val cat: Cat) : Screen()
}

enum class Gender { Male, Female }
class Cat(
    val name: String,
    val gender: Gender,
    @IdRes val imageResource: Int,
    val curiosity: String
)

val sampleCats = listOf(
    Cat(
        "Tiger",
        Gender.Male,
        R.drawable.cat1,
        "Are you that clean? This behavior serves several purposes: It helps cats tone down their scent so they can avoid predators, it cools them down, it promotes blood flow, and it distributes natural oils evenly around their coat, allowing them to stay warm and dry. Grooming also serves as a sign of affection between two cats, and it’s thought that saliva contains enzymes that serve as a natural antibiotic for wounds."
    ),
    Cat(
        "Max",
        Gender.Male,
        R.drawable.cat2,
        "Ever wonder why catnip lulls felines into a trance? The herb contains several chemical compounds, including one called nepetalactone, which a cat detects with receptors in its nose and mouth. The compounds trigger the typical odd behaviors you associate with the wacky kitty weed, including sniffing, head shaking, head rubbing, and rolling around on the ground."
    ),
    Cat(
        "Lily",
        Gender.Female,
        R.drawable.cat3,
        "More than half of the world’s felines don’t respond to catnip. Scientists still don’t know quite why some kitties go crazy for the aromatic herb and others don’t, but they have figured out that catnip sensitivity is hereditary. If a kitten has one catnip-sensitive parent, there’s a one-in-two chance that it will also grow up to crave the plant. And if both parents react to 'nip, the odds increase to at least three in four."
    ),
    Cat(
        "Kitty",
        Gender.Female,
        R.drawable.cat4,
        "A rich British antique dealer named Ben Rea loved his cat Blackie so much that when he died in 1988, he left most of his estate—totaling nearly \$13 million—to the lucky (albeit likely indifferent) feline. The money was split among three cat charities, which had been instructed to keep an eye on Rea’s beloved companion. To this day, Blackie holds the Guinness World Record for Wealthiest Cat."
    ),
    Cat(
        "Simba",
        Gender.Female,
        R.drawable.cat5,
        "On October 18, 1963, French scientists used a rocket to launch the first cat into space. The feline’s name was Félicette, and she made it safely to the ground following a parachute descent. Almost definitely landing on her feet."
    ),
    Cat(
        "Alfie",
        Gender.Male,
        R.drawable.cat6,
        "A train station in Southeastern Japan is presided over by an adorable \"stationmaster\": a 7-year-old calico cat named Nitama. The Kishi train station near Wakayama City hired Nitama in 2015, just a few months after its prior feline mascot, Tama, died from acute heart failure at the age of 16."
    ),
)

// Start building your app here!
@Composable
fun MyApp(currentScreen: Screen = Screen.CatsList, catClicked: (Cat) -> Unit = {}) {
    Surface(color = MaterialTheme.colors.background) {
        when (currentScreen) {
            is Screen.CatDetails -> CatDetails(currentScreen.cat)
            Screen.CatsList -> CatsList(
                cats = sampleCats,
                catClicked = catClicked
            )
        }
    }
}

@Preview("aaa")
@Composable
fun CatDetails(cat: Cat = Cat("Luna", Gender.Female, R.drawable.cat1, "Curiosity")) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = -10f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
        )
        Image(
            painter = painterResource(id = cat.imageResource),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(200.dp)
                .rotate(rotation)
                .background(MaterialTheme.colors.secondaryVariant)
        )
        Text(
            text = cat.name,
            fontWeight = FontWeight.Light,
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )
        Text(
            text = when (cat.gender) {
                Gender.Male -> "♂️️"
                Gender.Female -> "♀️"
            },
            fontSize = 42.sp
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = cat.curiosity,
            fontSize = 24.sp,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatsList(cats: List<Cat>, catClicked: (Cat) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxHeight(),
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(
            cats,
            itemContent = { item ->
                CatItem(cat = item, catClicked)
            }
        )
    }
}

@Composable
fun CatItem(cat: Cat, catClicked: (Cat) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp)
            .clickable {
                catClicked(cat)
            }
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        border = BorderStroke(2.dp, MaterialTheme.colors.secondary)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cat.name,
                modifier = Modifier.padding(bottom = 5.dp),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = MaterialTheme.colors.primary
            )
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(bottom = 10.dp),
            )
            Image(
                painter = painterResource(id = cat.imageResource),
                contentDescription = null
            )
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
