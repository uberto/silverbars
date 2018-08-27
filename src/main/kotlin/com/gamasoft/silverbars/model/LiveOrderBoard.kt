package com.gamasoft.silverbars.model

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

//It's specified but I assume that Buy and Sell orders are displayed separately
//I also assume the Board must be threadsafe
//When placing an order the Board will return an id that user can use to cancel the order

class LiveOrderBoard {

    private val nextOrderId = AtomicInteger(1)

    private val orders:MutableMap<Int, Order> = ConcurrentHashMap()

    fun placeOrder(o: Order): Int =
            nextOrderId.getAndIncrement().also { orders.put(it, o) }


    fun cancelOrder(orderId: Int) =
        orders.remove(orderId)


    fun displaySell(): List<OrderSummary> = orders.values
                .filterIsInstance<SellOrder>()
                .map { OrderSummary(it.kg, it.price) }
                .groupingBy {it.price }
                .reduce{p,tot,ord -> OrderSummary(tot.weight + ord.weight, p)}
                .values.sortedBy { it.price }


    fun displayBuy(): List<OrderSummary> = orders.values
            .filterIsInstance<BuyOrder>()
            .map { OrderSummary(it.kg, it.price) }
            .groupingBy {it.price }
            .reduce{p,tot,ord -> OrderSummary(tot.weight + ord.weight, p)}
            .values.sortedBy { it.price }
            .reversed()

}
