package com.olddragon.app.models.dice

import kotlin.random.Random

class Dice {
    fun roll(sides: Int): Int {
        return Random.nextInt(1, sides + 1)
    }

    fun rollM(sides: Int, quantity: Int): Int {
        var total = 0
        for (i in 1..quantity) {
            total += roll(sides)
        }
        return total
    }
}