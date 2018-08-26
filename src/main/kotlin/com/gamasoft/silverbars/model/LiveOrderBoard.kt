package com.gamasoft.silverbars.model

import java.util.concurrent.atomic.AtomicInteger

//It's specified but I assume that Buy and Sell orders are displayed separately
//I also assume the Board must be threadsafe
//When placing an order the Board will return an id that user can use to cancel the order

class LiveOrderBoard {

    val orderId = AtomicInteger(1)

    fun placeOrder(o: Order): Int {

        return orderId.getAndIncrement()
    }

    fun cancelOrder(orderId: Int) {

    }

    fun displaySell(): List<OrderSummary> = listOf()
    fun displayBuy(): List<OrderSummary> = listOf()

}
