package com.example.digivoter

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.digivoter.Navigation.MainScreen
import com.example.digivoter.ui.theme.DigivoterTheme
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        outputDirectory = getOutputDirectory()
        cameraExecutor  = Executors.newSingleThreadExecutor()
        setContent {

            DigivoterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    var shouldShowCamera: MutableState<Boolean> = remember{mutableStateOf(true)}
                    if (shouldShowCamera.value) {
                      MainScreen(true, outputDirectory, cameraExecutor,
                          goFromHere = {handleImageCapture(it, shouldShowCamera)})
                    }
                }

                }
            }


        }
    private fun handleImageCapture(uri: Uri, shouldShowCamera : MutableState<Boolean>) {
        Log.d("file is", uri.toString());
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        //mediaDir.let { File(it, ) }
//         val d  = File(mediaDir?.absoluteFile,"Donw").apply { mkdirs() }
        Log.d("path", mediaDir?.absolutePath.toString())
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    }

