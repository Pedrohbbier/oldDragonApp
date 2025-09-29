package models.character

import models.dice.Dice

enum class AttributeDistributionType {
    CLASSIC,    // Clássica: 3d6 para cada atributo
    HEROIC,     // Heróica: 4d6, descarta o menor
    ADVENTURER  // Aventureiro: distribuição de pontos
}

class AttributeDistribution {
    private val dice = Dice()
    
    fun generateAttributes(type: AttributeDistributionType): LinkedHashMap<String, Byte> {
        val attributes = linkedMapOf<String, Byte>()
        
        when (type) {
            AttributeDistributionType.CLASSIC -> {
                // Clássica: 3d6 para cada atributo
                attributes["str"] = dice.rollM(6, 3).toByte()
                attributes["dex"] = dice.rollM(6, 3).toByte()
                attributes["con"] = dice.rollM(6, 3).toByte()
                attributes["int"] = dice.rollM(6, 3).toByte()
                attributes["wis"] = dice.rollM(6, 3).toByte()
                attributes["cha"] = dice.rollM(6, 3).toByte()
            }
            AttributeDistributionType.HEROIC -> {
                // Heróica: 4d6, descarta o menor
                attributes["str"] = rollHeroic().toByte()
                attributes["dex"] = rollHeroic().toByte()
                attributes["con"] = rollHeroic().toByte()
                attributes["int"] = rollHeroic().toByte()
                attributes["wis"] = rollHeroic().toByte()
                attributes["cha"] = rollHeroic().toByte()
            }
            AttributeDistributionType.ADVENTURER -> {
                // Aventureiro: sistema de pontos (27 pontos para distribuir)
                // Base de 8 em cada atributo, 27 pontos extras para distribuir
                val baseValue: Byte = 8
                var pointsToDistribute = 27
                
                // Distribuição equilibrada para demonstração
                attributes["str"] = (baseValue + 4).toByte() // 12
                attributes["dex"] = (baseValue + 4).toByte() // 12
                attributes["con"] = (baseValue + 4).toByte() // 12
                attributes["int"] = (baseValue + 5).toByte() // 13
                attributes["wis"] = (baseValue + 5).toByte() // 13
                attributes["cha"] = (baseValue + 5).toByte() // 13
                // Total: 27 pontos distribuídos
            }
        }
        
        return attributes
    }
    
    private fun rollHeroic(): Int {
        // Rola 4d6 e descarta o menor
        val rolls = mutableListOf<Int>()
        for (i in 1..4) {
            rolls.add(dice.roll(6))
        }
        rolls.sort()
        // Soma os 3 maiores valores
        return rolls[1] + rolls[2] + rolls[3]
    }
    
    fun distributePointsAdventurer(pointsDistribution: Map<String, Int>): LinkedHashMap<String, Byte> {
        val attributes = linkedMapOf<String, Byte>()
        val baseValue = 8
        var totalPoints = 0
        
        // Verifica se a distribuição é válida
        for ((attr, points) in pointsDistribution) {
            if (points < 0 || points > 15) {
                throw IllegalArgumentException("Pontos para $attr devem estar entre 0 e 15")
            }
            totalPoints += points
        }
        
        if (totalPoints > 27) {
            throw IllegalArgumentException("Total de pontos não pode exceder 27")
        }
        
        // Distribui os pontos
        attributes["str"] = (baseValue + (pointsDistribution["str"] ?: 0)).toByte()
        attributes["dex"] = (baseValue + (pointsDistribution["dex"] ?: 0)).toByte()
        attributes["con"] = (baseValue + (pointsDistribution["con"] ?: 0)).toByte()
        attributes["int"] = (baseValue + (pointsDistribution["int"] ?: 0)).toByte()
        attributes["wis"] = (baseValue + (pointsDistribution["wis"] ?: 0)).toByte()
        attributes["cha"] = (baseValue + (pointsDistribution["cha"] ?: 0)).toByte()
        
        return attributes
    }
}