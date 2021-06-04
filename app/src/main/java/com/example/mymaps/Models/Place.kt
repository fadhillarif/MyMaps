package com.example.mymaps.Models

import java.io.Serializable

data class Place(val title: String, val desciption: String, val latitude: Double, val longtitude: Double) : Serializable