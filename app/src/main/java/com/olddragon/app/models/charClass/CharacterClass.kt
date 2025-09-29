package com.olddragon.app.models.charClass

import com.olddragon.app.models.dice.Dice

abstract class CharacterClass(
    val className: String,
    val hitDie: Int,
    val primaryAttribute: String,
    val allowedArmor: List<String>,
    val allowedWeapons: List<String>
) : ICharacterClass {

    protected val dice = Dice()

    fun calculateHitPoints(level: Byte, conModifier: Byte): Byte {
        var totalHP = 0

        totalHP += hitDie + conModifier

        for (i in 2..level) {
            totalHP += dice.roll(hitDie) + conModifier
        }

        return maxOf(1, totalHP).toByte()
    }

    abstract override fun getClassFeatures(level: Byte): List<String>
    abstract override fun canUseSpells(): Boolean
    abstract override fun getSpellsPerDay(level: Byte): Map<Int, Int>
}