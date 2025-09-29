package com.olddragon.app.models.character

import com.olddragon.app.models.dice.Dice

enum class AttributeDistributionType(val displayName: String) {
    CLASSIC("Clássica (3d6)"),
    HEROIC("Heróica (4d6, descarta menor)"),
    ADVENTURER("Aventureiro (Distribuição de pontos)")
}

class AttributeDistribution {
    private val dice = Dice()

    fun generateAttributes(type: AttributeDistributionType): LinkedHashMap<String, Byte> {
        val attributes = linkedMapOf<String, Byte>()

        when (type) {
            AttributeDistributionType.CLASSIC -> {
                attributes["str"] = dice.rollM(6, 3).toByte()
                attributes["dex"] = dice.rollM(6, 3).toByte()
                attributes["con"] = dice.rollM(6, 3).toByte()
                attributes["int"] = dice.rollM(6, 3).toByte()
                attributes["wis"] = dice.rollM(6, 3).toByte()
                attributes["cha"] = dice.rollM(6, 3).toByte()
            }
            AttributeDistributionType.HEROIC -> {
                attributes["str"] = rollHeroic().toByte()
                attributes["dex"] = rollHeroic().toByte()
                attributes["con"] = rollHeroic().toByte()
                attributes["int"] = rollHeroic().toByte()
                attributes["wis"] = rollHeroic().toByte()
                attributes["cha"] = rollHeroic().toByte()
            }
            AttributeDistributionType.ADVENTURER -> {
                val baseValue: Byte = 8
                attributes["str"] = (baseValue + 4).toByte()
                attributes["dex"] = (baseValue + 4).toByte()
                attributes["con"] = (baseValue + 4).toByte()
                attributes["int"] = (baseValue + 5).toByte()
                attributes["wis"] = (baseValue + 5).toByte()
                attributes["cha"] = (baseValue + 5).toByte()
            }
        }

        return attributes
    }

    private fun rollHeroic(): Int {
        val rolls = mutableListOf<Int>()
        for (i in 1..4) {
            rolls.add(dice.roll(6))
        }
        rolls.sort()
        return rolls[1] + rolls[2] + rolls[3]
    }

    fun distributePointsAdventurer(pointsDistribution: Map<String, Int>): LinkedHashMap<String, Byte> {
        val attributes = linkedMapOf<String, Byte>()
        val baseValue = 8
        var totalPoints = 0

        for ((attr, points) in pointsDistribution) {
            if (points < 0 || points > 15) {
                throw IllegalArgumentException("Points for $attr must be between 0 and 15")
            }
            totalPoints += points
        }

        if (totalPoints > 27) {
            throw IllegalArgumentException("Total points cannot exceed 27")
        }

        attributes["str"] = (baseValue + (pointsDistribution["str"] ?: 0)).toByte()
        attributes["dex"] = (baseValue + (pointsDistribution["dex"] ?: 0)).toByte()
        attributes["con"] = (baseValue + (pointsDistribution["con"] ?: 0)).toByte()
        attributes["int"] = (baseValue + (pointsDistribution["int"] ?: 0)).toByte()
        attributes["wis"] = (baseValue + (pointsDistribution["wis"] ?: 0)).toByte()
        attributes["cha"] = (baseValue + (pointsDistribution["cha"] ?: 0)).toByte()

        return attributes
    }
}