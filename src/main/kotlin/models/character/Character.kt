package models.character

import models.character.race.Race
import models.character.race.Alignment
import models.charClass.CharacterClass
import models.items.Item
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
    var inventory: MutableList<Item>
    var atributes: LinkedHashMap<String, Byte>
    var equippedItems: LinkedHashMap<String, Item?>

    constructor(){
        name = ""
        level = 1
        hp = 1
        xp = 0
        race = null
        characterClass = null
        alignment = null
        
        // Inicializa com distribuição clássica por padrão
        val attributeDistribution = AttributeDistribution()
        skills = attributeDistribution.generateAttributes(AttributeDistributionType.CLASSIC)
        
        skillMod = linkedMapOf()
        calculateSkillModifiers()

        atributes = linkedMapOf()
        atributes["ca"] = 0
        atributes["ba"] = 0
        atributes["jp"] = 0

        inventory = mutableListOf()

        equippedItems = linkedMapOf()
        equippedItems["leftHand"] = null
        equippedItems["rightHand"] = null
        equippedItems["body"] = null
    }
    
    constructor(distributionType: AttributeDistributionType) {
        name = ""
        level = 1
        hp = 1
        xp = 0
        race = null
        characterClass = null
        alignment = null
        
        // Usa o tipo de distribuição especificado
        val attributeDistribution = AttributeDistribution()
        skills = attributeDistribution.generateAttributes(distributionType)
        
        skillMod = linkedMapOf()
        calculateSkillModifiers()

        atributes = linkedMapOf()
        atributes["ca"] = 0
        atributes["ba"] = 0
        atributes["jp"] = 0

        inventory = mutableListOf()

        equippedItems = linkedMapOf()
        equippedItems["leftHand"] = null
        equippedItems["rightHand"] = null
        equippedItems["body"] = null
    }
    
    constructor(customAttributes: LinkedHashMap<String, Byte>) {
        name = ""
        level = 1
        hp = 1
        xp = 0
        race = null
        characterClass = null
        alignment = null
        
        // Usa atributos customizados (para sistema aventureiro)
        skills = customAttributes
        
        skillMod = linkedMapOf()
        calculateSkillModifiers()

        atributes = linkedMapOf()
        atributes["ca"] = 0
        atributes["ba"] = 0
        atributes["jp"] = 0

        inventory = mutableListOf()

        equippedItems = linkedMapOf()
        equippedItems["leftHand"] = null
        equippedItems["rightHand"] = null
        equippedItems["body"] = null
    }
    
    private fun calculateSkillModifiers() {
        for(skill in skills) {
            when{
                skill.value < 1 -> error("${skill.key} should not be less than 1")
                skill.value in 9..12 -> skillMod[skill.key] = 0
                skill.value > 12 -> skillMod[skill.key] = floor((skill.value.toDouble()-10)/2).toInt().toByte()
                skill.value < 9 -> skillMod[skill.key] = floor((skill.value.toDouble()-10)/2).toInt().toByte()
            }
        }
    }
    
    fun applyRacialBonuses() {
        race?.racialHability?.forEach { hability ->
            when(hability.modType) {
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
        // Recalcula modificadores após aplicar bônus raciais
        calculateSkillModifiers()
    }
    
    fun assignCharacterClass(newClass: CharacterClass) {
        this.characterClass = newClass
        updateHitPoints()
    }
    
    fun assignRace(newRace: Race) {
        this.race = newRace
        // Alinhamento padrão baseado na raça
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
        sb.appendLine("=== PERSONAGEM ===")
        sb.appendLine("Nome: $name")
        sb.appendLine("Nível: $level")
        sb.appendLine("HP: $hp")
        sb.appendLine("XP: $xp")
        sb.appendLine("Raça: ${race?.raceName ?: "Não definida"}")
        sb.appendLine("Classe: ${characterClass?.className ?: "Não definida"}")
        sb.appendLine("Alinhamento: $alignment")
        sb.appendLine()
        sb.appendLine("=== ATRIBUTOS ===")
        skills.forEach { (attr, value) ->
            val mod = skillMod[attr] ?: 0
            val modStr = if (mod >= 0) "+$mod" else "$mod"
            sb.appendLine("$attr: $value ($modStr)")
        }
        sb.appendLine()
        sb.appendLine("=== CARACTERÍSTICAS RACIAIS ===")
        race?.let { r ->
            sb.appendLine("Movimento: ${r.movement}")
            sb.appendLine("Infravisão: ${r.infravision?.let { "${it}m" } ?: "Nenhuma"}")
            sb.appendLine("Habilidades Raciais:")
            r.racialHability.forEach { hability ->
                sb.appendLine("- ${hability.habilityName}: ${hability.description}")
            }
        }
        sb.appendLine()
        sb.appendLine("=== CARACTERÍSTICAS DE CLASSE ===")
        characterClass?.let { c ->
            sb.appendLine("Atributo Principal: ${c.primaryAttribute}")
            sb.appendLine("Dado de Vida: d${c.hitDie}")
            sb.appendLine("Características:")
            c.getClassFeatures(level).forEach { feature ->
                sb.appendLine("- $feature")
            }
            if (c.canUseSpells()) {
                sb.appendLine("Magias por dia:")
                c.getSpellsPerDay(level).forEach { (level, count) ->
                    sb.appendLine("- Nível $level: $count magias")
                }
            }
        }
        
        return sb.toString()
    }
}