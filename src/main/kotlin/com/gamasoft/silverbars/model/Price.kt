package com.gamasoft.silverbars.model

data class Price(val gbp: Int) //only one currency supported

fun Int.ofGBP() = Price(this)

