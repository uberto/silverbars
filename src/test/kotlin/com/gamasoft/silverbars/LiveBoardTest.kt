package com.gamasoft.silverbars

import assertk.assert
import assertk.assertions.isEqualTo
import com.gamasoft.silverbars.model.*
import org.junit.jupiter.api.Test

class LiveBoardTest {

    val u1 = User("user1")
    val u2 = User("user2")
    val u3 = User("user3")
    val u4 = User("user4")

    val orders = listOf(
            SellOrder(3.5.ofKg(), 306.ofGBP(), u1),
            SellOrder(1.2.ofKg(), 310.ofGBP(), u2),
            BuyOrder(1.5.ofKg(), 305.ofGBP(), u1),
            BuyOrder(1.9.ofKg(), 307.ofGBP(), u2),
            BuyOrder(0.5.ofKg(), 307.ofGBP(), u3),
            SellOrder(1.5.ofKg(), 307.ofGBP(), u3),
            SellOrder(2.0.ofKg(), 306.ofGBP(), u4),
            BuyOrder(3.1.ofKg(), 306.ofGBP(), u4)
    )

    @Test
    fun liveOrderBoardDisplaySellSummary(){

        val actual = LiveOrderBoard().run {
            orders.forEach{ placeOrder(it) }
            displaySell() }

        assert(actual).isEqualTo(listOf(
                OrderSummary(5.5.ofKg(), 306.ofGBP()),
                OrderSummary(1.5.ofKg(), 307.ofGBP()),
                OrderSummary(1.2.ofKg(), 310.ofGBP())))

    }

    @Test
    fun liveOrderBoardDisplayBuySummary(){

        val actual = LiveOrderBoard().run {
            orders.forEach{ placeOrder(it) }
            displayBuy() }

        assert(actual).isEqualTo(listOf(
                OrderSummary(2.4.ofKg(), 307.ofGBP()),
                OrderSummary(3.1.ofKg(), 306.ofGBP()),
                OrderSummary(1.5.ofKg(), 305.ofGBP())))

    }

}