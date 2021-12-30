package com.app.accompanisnavissue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.app.accompanisnavissue.ui.theme.AccompanisNavIssueTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccompanisNavIssueTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ExperimentalAnimationNav()
                }
            }
        }
    }
}


@Composable
fun BottomBar() {
    BottomNavigation(
        backgroundColor = Color.Green,
        contentColor = Color.Black
    ) {
        Text("This is a bottom bar nav ")
    }
}

@ExperimentalAnimationApi
@Composable
fun ExperimentalAnimationNav() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = "Blue",
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        }

    ) {
        composable(
            "Blue",
        ) { BlueScreen(navController) }
        composable(
            "Red"
        ) { RedScreen(navController) }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityScope.BlueScreen(navController: NavHostController) {
    val rems = rememberAnimatedNavController()
    Scaffold(
        bottomBar = { BottomBar() }
    ) { innerPadding ->
        AnimatedNavHost(
            rems,
            startDestination = "HomeScreen",
            Modifier.padding(innerPadding)
        ) {
            composable(route = "HomeScreen") {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                ) {
                    Spacer(Modifier.height(Dp(25f)))
                    NavigateButton(
                        "Navigate Horizontal",
                        Modifier
                            .wrapContentWidth()
                            .then(Modifier.align(Alignment.CenterHorizontally))
                    ) { navController.navigate("Red") }
                    NavigateBackButton(navController)
                }
            }
        }
    }


}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityScope.RedScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        NavigateBackButton(navController)
    }
}


@Composable
fun NavigateButton(
    text: String,
    modifier: Modifier = Modifier,
    listener: () -> Unit = { }
) {
    Button(
        onClick = listener,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
fun NavigateBackButton(navController: NavController) {
    // Use LocalLifecycleOwner.current as a proxy for the NavBackStackEntry
    // associated with this Composable
    if (navController.currentBackStackEntry == LocalLifecycleOwner.current &&
        navController.previousBackStackEntry != null
    ) {
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go to Previous screen")
        }
    }
}