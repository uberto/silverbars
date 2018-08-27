package com.gamasoft.silverbars.model

data class QuantityInKg(val kg: Double) {

    operator fun plus(other: QuantityInKg): QuantityInKg {
        return QuantityInKg(kg + other.kg)
    }
}


fun Double.ofKg() = QuantityInKg(this)
