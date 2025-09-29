package com.olddragon.app.models.charClass

class Fighter : CharacterClass(
    className = "Guerreiro",
    hitDie = 8,
    primaryAttribute = "str",
    allowedArmor = listOf("All"),
    allowedWeapons = listOf("All")
) {
    override fun getClassFeatures(level: Byte): List<String> {
        val features = mutableListOf<String>()
        features.add("Multiple attacks")
        if (level >= 3) features.add("Combat specialization")
        if (level >= 5) features.add("Leadership")
        return features
    }

    override fun canUseSpells(): Boolean = false

    override fun getSpellsPerDay(level: Byte): Map<Int, Int> = emptyMap()
}