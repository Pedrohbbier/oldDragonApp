package com.olddragon.app.models.charClass

interface ICharacterClass {
    fun getClassFeatures(level: Byte): List<String>
    fun canUseSpells(): Boolean
    fun getSpellsPerDay(level: Byte): Map<Int, Int>
}