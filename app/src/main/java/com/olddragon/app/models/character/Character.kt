package com.olddragon.app.models.character

import com.olddragon.app.models.charClass.CharacterClass
import kotlin.math.floor

open class Character {

    var name: String
    var level: Byte
    var hp: Byte
    var xp: Int
    var race: Race?
    var characterClass: CharacterClass?
    var alignment: Alignment?
    var skills: LinkedHashMap<String, Byte>
    var skillMod: LinkedHashMap<String, Byte>
    var atributes: LinkedHashMap<String, Byte>

    constructor() {
        name = ""
        level = 1
        hp = 1
        xp = 0
        race = null
        characterClass = null
        alignment = null

        val attributeDistribution = AttributeDistribution()
        skills = attributeDistribution.generateAttributes(AttributeDistributionType.CLASSIC)

        skillMod = linkedMapOf()
        calculateSkillModifiers()

        atributes = linkedMapOf()
        atributes["ca"] = 0
        atributes["ba"] = 0
        atributes["jp"] = 0
    }

    constructor(distributionType: AttributeDistributionType) {
        name = ""
        level = 1
        hp = 1
        xp = 0
        race = null
        characterClass = null
        alignment = null

        val attributeDistribution = AttributeDistribution()
        skills = attributeDistribution.generateAttributes(distributionType)

        skillMod = linkedMapOf()
        calculateSkillModifiers()

        atributes = linkedMapOf()
        atributes["ca"] = 0
        atributes["ba"] = 0
        atributes["jp"] = 0
    }

    constructor(customAttributes: LinkedHashMap<String, Byte>) {
        name = ""
        level = 1
        hp = 1
        xp = 0
        race = null
        characterClass = null
        alignment = null

        skills = customAttributes

        skillMod = linkedMapOf()
        calculateSkillModifiers()

        atributes = linkedMapOf()
        atributes["ca"] = 0
        atributes["ba"] = 0
        atributes["jp"] = 0
    }

    private fun calculateSkillModifiers() {
        for (skill in skills) {
            when {
                skill.value < 1 -> error("${skill.key} should not be less than 1")
                skill.value in 9..12 -> skillMod[skill.key] = 0
                skill.value > 12 -> skillMod[skill.key] = floor((skill.value.toDouble() - 10) / 2).toInt().toByte()
                skill.value < 9 -> skillMod[skill.key] = floor((skill.value.toDouble() - 10) / 2).toInt().toByte()
            }
        }
    }

    fun applyRacialBonuses() {
        race?.racialHability?.forEach { hability ->
            when (hability.modType) {
                "skill" -> {
                    val currentValue = skills[hability.modStat] ?: 0
                    skills[hability.modStat] = (currentValue + hability.modAmmount).toByte()
                }
                "atribute" -> {
                    val currentValue = atributes[hability.modStat] ?: 0
                    atributes[hability.modStat] = (currentValue + hability.modAmmount).toByte()
                }
            }
        }
        calculateSkillModifiers()
    }

    fun assignCharacterClass(newClass: CharacterClass) {
        this.characterClass = newClass
        updateHitPoints()
    }

    fun assignRace(newRace: Race) {
        this.race = newRace
        if (this.alignment == null) {
            this.alignment = newRace.preferredAlignment
        }
        applyRacialBonuses()
    }

    fun assignAlignment(newAlignment: Alignment) {
        this.alignment = newAlignment
    }

    private fun updateHitPoints() {
        characterClass?.let { charClass ->
            val conModifier = skillMod["con"] ?: 0
            hp = charClass.calculateHitPoints(level, conModifier)
        }
    }

    fun levelUp() {
        if (level < 20) {
            level++
            updateHitPoints()
        }
    }

    fun getCharacterSummary(): String {
        val sb = StringBuilder()
        sb.appendLine("=== CHARACTER ===")
        sb.appendLine("Name: $name")
        sb.appendLine("Level: $level")
        sb.appendLine("HP: $hp")
        sb.appendLine("XP: $xp")
        sb.appendLine("Race: ${race?.raceName ?: "Not defined"}")
        sb.appendLine("Class: ${characterClass?.className ?: "Not defined"}")
        sb.appendLine("Alignment: ${alignment?.displayName ?: "Not defined"}")
        sb.appendLine()
        sb.appendLine("=== ATTRIBUTES ===")
        skills.forEach { (attr, value) ->
            val mod = skillMod[attr] ?: 0
            val modStr = if (mod >= 0) "+$mod" else "$mod"
            val attrName = when (attr) {
                "str" -> "Strength"
                "dex" -> "Dexterity"
                "con" -> "Constitution"
                "int" -> "Intelligence"
                "wis" -> "Wisdom"
                "cha" -> "Charisma"
                else -> attr.uppercase()
            }
            sb.appendLine("$attrName: $value ($modStr)")
        }
        return sb.toString()
    }
}