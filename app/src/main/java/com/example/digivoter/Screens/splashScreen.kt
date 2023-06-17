package com.example.digivoter.Screens

import android.view.animation.OvershootInterpolator
import android.window.SplashScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digivoter.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(goToVoterScreen: () -> Unit)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(all = 25.dp), contentAlignment = Alignment.Center
    )
    {
        val scale = remember { androidx.compose.animation.core.Animatable(1f) }
        LaunchedEffect(key1 = true)
        {
            scale.animateTo(targetValue = 3.5f, animationSpec = tween(800, easing = {
                OvershootInterpolator(4f).getInterpolation(it)
            }))
            delay(2000L)

            //after showing start screen animation go from here(using lambda)
            goToVoterScreen()
        }
        Column(
            Modifier
                .scale(scale.value)
                .clip(RoundedCornerShape(5.dp))
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Powered By ", fontFamily = FontFamily(Font(R.font.ralewaybold)),
                fontSize = 4.sp )
                Image(modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.img_1),
                    contentDescription = "Logo"
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo"
            )

        }

    }
}