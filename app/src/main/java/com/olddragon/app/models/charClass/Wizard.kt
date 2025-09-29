package com.olddragon.app.models.charClass

class Wizard : CharacterClass(
    className = "Mago",
    hitDie = 4,
    primaryAttribute = "int",
    allowedArmor = listOf("None"),
    allowedWeapons = listOf("Dagger", "Staff", "Dart")
) {
    override fun getClassFeatures(level: Byte): List<String> {
        val features = mutableListOf<String>()
        features.add("Spellcasting")
        features.add("Read magic")
        if (level >= 3) features.add("Magic item creation")
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