package com.gamasoft.silverbars.model

sealed class Order()

data class SellOrder(val kg: QuantityInKg, val price: Price, val user: User): Order()

data class BuyOrder(val kg: QuantityInKg, val price: Price, val user: User): Order()



