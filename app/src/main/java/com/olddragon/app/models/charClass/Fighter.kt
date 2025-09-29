package com.olddragon.app.models.charClass

class Fighter : CharacterClass(
    className = "Guerreiro",
    hitDie = 8,
    primaryAttribute = "str",
    allowedArmor = listOf("Todas"),
    allowedWeapons = listOf("Todas")
) {
    override fun getClassFeatures(level: Byte): List<String> {
        val features = mutableListOf<String>()
        features.add("Ataque múltiplo")
        if (level >= 3) features.add("Especialização em combate")
        if (level >= 5) features.add("Liderança")
        return features
    }

    override fun canUseSpells(): Boolean = false

    override fun getSpellsPerDay(level: Byte): Map<Int, Int> = emptyMap()
}