package models.dice

import kotlin.random.Random

class Dice{
    fun roll(type: Int): Int{
        when(type){
            2,3,4,6,8,10,12,20,100 -> return Random.Default.nextInt(1,type+1)
            else -> error("Invalid class parameters: $type")
        }
    }
    fun rollM(type: Int, times: Int): Int{
        var sum = 0
        for(i in 1..times){
            sum += roll(type)
        }
        return sum
    }
}