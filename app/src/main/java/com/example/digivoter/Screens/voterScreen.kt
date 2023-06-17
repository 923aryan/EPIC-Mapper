package com.example.digivoter.Screens

import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.digivoter.R
import com.example.digivoter.util.CameraView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService

@Composable
fun voterScreen(
    outputDirectory: File,
    cameraExecutor: ExecutorService,
    go: (uri: Uri) -> Unit
)
{
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp
    val main = ((width - 40 + 10).toFloat() / 5f).dp
    var width_ by remember {
        mutableStateOf(0.1.dp)
    }
    var EPIC by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var finally by remember {
        mutableStateOf(false)
    }
    var barCount by remember { mutableStateOf(0) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .padding(all = 15.dp)
    )
    {
        var shouldShowCamera: MutableState<Boolean> = remember { mutableStateOf(false) }
        val textInBox = "Tap to Add Photo"
        var isBorderDimmed by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }
        var Done by remember { mutableStateOf(false) }
        var coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted)
            {
                // Permission Accepted: Do something
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else
            {
                // Permission Denied: Do something
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        Box(
            modifier = Modifier
                .weight(3f)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 5.dp, shape = RoundedCornerShape(10.dp),
                    color = Color.Black
                )
                .background(MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity))
                .fillMaxSize()
                .clickable {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    )
                    {
                        launcher.launch(android.Manifest.permission.CAMERA)
                    }
                    isBorderDimmed = true
                    focusRequester.requestFocus()
                    finally = false

                },
            contentAlignment = Alignment.Center
        ) {
            var allowed = false


            if (shouldShowCamera.value)
            {
                if (EPIC == "")
                {
                    focusRequester.requestFocus()
                }
                else if (Done)
                {
                    focusRequester.freeFocus()
                    CameraView(FileName = EPIC,
                        outputDirectory = outputDirectory,
                        executor = cameraExecutor,
                        onImageCaptured = { it ->
                            shouldShowCamera.value = false;
                            width_ += main;barCount++;Log.d("count", barCount.toString())
                            if (barCount == 5)
                            {
                                EPIC = ""
                                Done = false
                                finally = true
                                barCount = 0
                                coroutineScope.launch(Dispatchers.IO) {  delay(200L)   }
                                width_ = 0.1.dp
                                //Toast.makeText(context, "Photo Saved", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onError = { Log.e("kilo", "View error:", it) }
                    )

                }

            }
            else
            {
                Text(
                    modifier = Modifier.clickable {
                        focusRequester.requestFocus()
                    },
                    text = textInBox,
                    fontFamily = FontFamily(Font(R.font.ralewaybold)),
                    fontSize = 25.sp,
                    color = Color(140, 134, 133)
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            extracted(
                configuration,
                width_,
                focusRequester,
                Done,
                focusManager,
                finally, EPIC =

                { a, b ->
                    EPIC = a;
                    shouldShowCamera.value = b
                    Done = b
                    Log.d("done", b.toString())
                },
                clicked = { finally = false }
            )

        }
    }

}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun extracted(
    configuration: Configuration,
    width_: Dp,
    focusRequester: FocusRequester,
    Done: Boolean,
    focusManager: FocusManager,
    finally: Boolean,
    EPIC: (e: String, shouldShowCamera: Boolean) -> Unit,
    clicked: () -> Unit
)
{
    var EPIC1 by remember { mutableStateOf("") }
    if (finally) EPIC1 = ""
    var Done1 = Done
    val height = configuration.screenHeightDp
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {

        var barAnimate = animateDpAsState(
            targetValue = width_,
            animationSpec = spring(Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                //.align(Alignment.Start)
                .clip(RoundedCornerShape(5.dp))
                .border(2.dp, color = Color.Black, RoundedCornerShape(5.dp))
                .size(width = width_, height = 40.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(barAnimate.value)
                    .height(40.dp)
                    .background(Color.Green)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))

        TextField(value = EPIC1,

            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                disabledLabelColor = Color.Transparent,
                disabledPlaceholderColor = Color.Transparent,
                placeholderColor = Color.Transparent,
                backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity)
            ),

            onValueChange = { EPIC1 = it; },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .border(5.dp, color = Color.Black, shape = RoundedCornerShape(2.dp))
                .background(color = Color.White)
                //.clickable { clicked() }
                .onFocusEvent { focusState -> if (focusState.hasFocus) clicked() }
                .focusRequester(focusRequester), placeholder = {
                Text(
                    text = "Enter Your EPIC Here",
                    fontFamily = FontFamily(Font(R.font.ralewaybold)),
                    color = Color(140, 134, 133),
                    fontSize = 15.sp
                )
            },
            textStyle = TextStyle(color = Color.Black, fontSize = 25.sp),
            maxLines = 1, singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                //   saveWithName(EPIC);
                Done1 = true
                keyboardController?.hide()
                // shouldShowCamera.value = true
                focusRequester.freeFocus()
                focusManager.clearFocus()
                EPIC(EPIC1, true)
            }
            )
        )

        Spacer(modifier = Modifier.size(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                text = "Powered By ", fontFamily = FontFamily(Font(R.font.ralewaybold)),
                color = Color.Black, fontSize = 15.sp
            )
            Image(
                painter = painterResource(id = R.drawable.img_1),
                contentDescription = "FireChain logo",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .padding(top = 2.dp)
            )
        }

    }
}


