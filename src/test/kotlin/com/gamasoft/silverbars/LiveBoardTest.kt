package com.gamasoft.silverbars

import assertk.assert
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.gamasoft.silverbars.model.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Test

class LiveBoardTest {

    val u1 = User("user1")
    val u2 = User("user2")
    val u3 = User("user3")
    val u4 = User("user4")

    val orders = listOf(
            SellOrder(3.5.ofKg(), 306.ofGBP(), u1),
            BuyOrder(1.5.ofKg(), 305.ofGBP(), u1),
            SellOrder(1.2.ofKg(), 310.ofGBP(), u2),
            BuyOrder(1.9.ofKg(), 307.ofGBP(), u2),
            BuyOrder(0.5.ofKg(), 307.ofGBP(), u3),
            SellOrder(1.5.ofKg(), 307.ofGBP(), u3),
            BuyOrder(3.1.ofKg(), 306.ofGBP(), u4),
            SellOrder(2.0.ofKg(), 306.ofGBP(), u4)
    )


    @Test
    fun placeAnOrderReturnsUniqueId() {

        val board = LiveOrderBoard()

        val ordNo = orders.map { board.placeOrder(it) }

        assert(ordNo.toSet()).hasSize(orders.size)
    }


        @Test
    fun liveOrderBoardDisplaysSellSummaryOrderedByPrice(){

        val actual = LiveOrderBoard().run {
            orders.forEach{ placeOrder(it) }
            displaySell() }

        assert(actual).isEqualTo(listOf(
                OrderSummary(5.5.ofKg(), 306.ofGBP()),
                OrderSummary(1.5.ofKg(), 307.ofGBP()),
                OrderSummary(1.2.ofKg(), 310.ofGBP())))

    }

    @Test
    fun liveOrderBoardDisplaysBuySummaryOrderedByReversedPrice(){

        val actual = LiveOrderBoard().run {
            orders.forEach{ placeOrder(it) }
            displayBuy() }

        assert(actual).isEqualTo(listOf(
                OrderSummary(2.4.ofKg(), 307.ofGBP()),
                OrderSummary(3.1.ofKg(), 306.ofGBP()),
                OrderSummary(1.5.ofKg(), 305.ofGBP())))

    }

    @Test
    fun cancelOrderRemovesItFromBoard(){

        with(LiveOrderBoard()) {

            val ordNo = orders.map { placeOrder(it) }

            cancelOrder(ordNo.first())
            cancelOrder(ordNo.last())


            assert(displayBuy()).isEqualTo(listOf(
                    OrderSummary(2.4.ofKg(), 307.ofGBP()),
                    OrderSummary(3.1.ofKg(), 306.ofGBP()),
                    OrderSummary(1.5.ofKg(), 305.ofGBP())))

        }
    }

    @Test
    fun addingAndRemovingConcurrently() {

        with(LiveOrderBoard()) {
            val jobs = mutableListOf<Job>() // replaces waitGroup

            runBlocking {
                (1..10000).forEach {
                    jobs += launch {
                        placeOrder(BuyOrder(1.0.ofKg(), (100 + (it % 10)).ofGBP(), u1))
                        placeOrder(SellOrder(1.0.ofKg(), (110 + (it % 10)).ofGBP(), u2))
                        if (it % 10 == 0) cancelOrder(it / 10)
                    }
                }
                jobs.forEach { it.join() }
            }
            val totBuy = displayBuy().sumByDouble{it.weight.kg}
            val totSell = displaySell().sumByDouble{it.weight.kg}

            assert(totBuy).isEqualTo(9500.0)
            assert(totSell).isEqualTo(9500.0)

        }
    }

}