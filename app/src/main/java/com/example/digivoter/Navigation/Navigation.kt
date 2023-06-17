package com.example.digivoter.Navigation

import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digivoter.Screens.SplashScreen
import com.example.digivoter.Screens.voterScreen
import java.io.File
import java.util.concurrent.ExecutorService


@Composable
fun MainScreen(shouldShowCamera: Boolean, outputDirectory: File, cameraExecutor: ExecutorService, goFromHere : (uri : Uri) -> Unit)
{
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.splashScreen.route
    )
    {
        composable(route = Screens.splashScreen.route)
        {
           SplashScreen()
           {
               navController.navigate(Screens.voterScreen.route)
               {
                   popUpTo(Screens.splashScreen.route)
                   {
                       inclusive = true
                   }
               }
           }
        }
        composable(route = Screens.voterScreen.route)
        {
            voterScreen( outputDirectory, cameraExecutor, go = {goFromHere(it)} )
        }

    }
}