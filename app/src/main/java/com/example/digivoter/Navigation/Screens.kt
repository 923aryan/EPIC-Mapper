package com.example.digivoter.Navigation

sealed class Screens(val route : String)
{
    object splashScreen : Screens(route = "splashScreen")
    object voterScreen : Screens(route = "voterScreen")
}