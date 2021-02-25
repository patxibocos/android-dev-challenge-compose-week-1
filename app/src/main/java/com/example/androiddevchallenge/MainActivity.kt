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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
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

class Cat(val name: String, @IdRes val imageResource: Int)

val sampleCats = listOf(
    Cat("Tiger", R.drawable.ic_animal_1297724),
    Cat("Max", R.drawable.ic_cartoon_1292872),
    Cat("Smokey", R.drawable.ic_cartoon_1296251),
    Cat("Kitty", R.drawable.ic_grooming_1801287),
    Cat("Simba", R.drawable.ic_leopard_47727),
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

@Composable
fun CatDetails(cat: Cat) {
    Text(cat.name)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CatsList(cats: List<Cat>, catClicked: (Cat) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxHeight(),
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(cats, itemContent = { item ->
            CatItem(cat = item, catClicked)
        })
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
                imageVector = ImageVector.vectorResource(id = cat.imageResource),
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
