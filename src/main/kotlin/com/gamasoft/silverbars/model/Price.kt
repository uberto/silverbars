package com.gamasoft.silverbars.model

data class Price(val money: Int, val ccy: String): Comparable<Price> {
    override fun compareTo(other: Price): Int {
        assert(ccy == other.ccy)
        return money.compareTo(other.money)
    }

}

fun Int.ofGBP() = Price(this, "GBP")

