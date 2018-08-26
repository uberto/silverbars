package com.gamasoft.silverbars.model

data class QuantityInKg(val kg: Double)


fun Double.ofKg() = QuantityInKg(this)
