package com.olddragon.app.models.charClass

class Wizard : CharacterClass(
    className = "Mago",
    hitDie = 4,
    primaryAttribute = "int",
    allowedArmor = listOf("Nenhuma"),
    allowedWeapons = listOf("Adaga", "Cajado", "Dardo")
) {
    override fun getClassFeatures(level: Byte): List<String> {
        val features = mutableListOf<String>()
        features.add("Conjuração de magias")
        features.add("Leitura de magias")
        if (level >= 3) features.add("Criação de itens mágicos")
        return features
    }

    override fun canUseSpells(): Boolean = true

    override fun getSpellsPerDay(level: Byte): Map<Int, Int> {
        return when (level.toInt()) {
            1 -> mapOf(1 to 1)
            2 -> mapOf(1 to 2)
            3 -> mapOf(1 to 2, 2 to 1)
            4 -> mapOf(1 to 3, 2 to 2)
            5 -> mapOf(1 to 4, 2 to 2, 3 to 1)
            else -> mapOf(1 to 4, 2 to 3, 3 to 2)
        }
    }
}