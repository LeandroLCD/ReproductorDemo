package com.leandrolcd.reproductordemo.ui.menu

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.leandrolcd.reproductordemo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun MenuScreen() {
    var index by remember {
        mutableStateOf(0)
    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        drawerGesturesEnabled = false,
        drawerContent = {
            DrawerContent {
                scope.launch {
                    // delay for the ripple effect
                    delay(timeMillis = 250)
                    scaffoldState.drawerState.close()
                }
            }
        },
        scaffoldState = scaffoldState,
        bottomBar = {
            MyBottomBar(index) {
                index = it
            }
        },
        topBar = {
            MyTopBar {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        },
    ) {innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            RadioPlay(Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun DrawerContent(
    itemClick: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {






    }
}

@Composable
fun MyTopBar(onNavIconClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo")
            }
        },
        elevation = 8.dp,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavIconClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open Navigation Drawer"
                )
            }
        }
    )
}

@Composable
fun MyBottomBar(index: Int, onClickSelect: (Int) -> Unit) {
    val unselectedContentColor = Color.Gray
    BottomAppBar(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 8.dp
    ) {
        BottomNavigationItem(selected = index == 0, onClick = { onClickSelect(0) }, icon = {
            Icon(
                imageVector = Icons.Outlined.Home,
                contentDescription = "home"
            )
        }, unselectedContentColor = unselectedContentColor, selectedContentColor = Color.Red)
        BottomNavigationItem(selected = index == 1, onClick = { onClickSelect(1) }, icon = {
            Icon(
                imageVector = Icons.Outlined.VolunteerActivism, //PhotoCamera
                contentDescription = "Donar"
            )
        }, unselectedContentColor = unselectedContentColor, selectedContentColor = Color.Red)
    }
}

@Composable
fun RadioPlay(modifier: Modifier) {
    ConstraintLayout(
        modifier
            .background(Color.Red)
            .height(120.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
        val (button,  cart) = createRefs()
        val topGuide = createGuidelineFromTop(0.33f)
        val bottomGuide = createGuidelineFromBottom(0.33f)


        MyCard(
            Modifier
                .fillMaxWidth().fillMaxHeight(0.66f)
                .constrainAs(cart) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    top.linkTo(topGuide)
                    end.linkTo(parent.end)
                })
        MyButtonPlay(Modifier.height(80.dp).width(88.dp).padding(start = 8.dp).constrainAs(button) {
            bottom.linkTo(bottomGuide)
            start.linkTo(parent.start)
            top.linkTo(parent.top)
        })

    }
}

@Composable
fun MyCard(modifier: Modifier) {
    Card(backgroundColor = Color.Yellow, shape = RoundedCornerShape(16.dp), modifier = modifier) {
        Row(Modifier.fillMaxSize()){
Text(text = "En Vivo")
        }
    }

}

@Composable
fun MyButtonPlay(modifier: Modifier) {
    Button(onClick = { }, shape = CircleShape, modifier = modifier, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)) {
        Icon(imageVector = Icons.Outlined.PlayCircleOutline, contentDescription = "")
    }
}

