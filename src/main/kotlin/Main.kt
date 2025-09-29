import models.character.*
import models.character.race.*
import models.charClass.*

fun main(){
    println("=== SISTEMA DE CRIAÇÃO DE PERSONAGEM OLD DRAGON ===\n")
    
    // Exemplo 1: Personagem com distribuição clássica
    println("1. Personagem com distribuição CLÁSSICA de atributos:")
    val warrior = Character(AttributeDistributionType.CLASSIC)
    warrior.name = "Thorin"
    warrior.assignRace(Races.dwarf)
    warrior.assignCharacterClass(CharacterClasses.fighter)
    println(warrior.getCharacterSummary())
    
    // Exemplo 2: Personagem com distribuição heróica
    println("\n" + "=".repeat(50) + "\n")
    println("2. Personagem com distribuição HERÓICA de atributos:")
    val mage = Character(AttributeDistributionType.HEROIC)
    mage.name = "Gandalf"
    mage.assignRace(Races.human)
    mage.assignCharacterClass(CharacterClasses.wizard)
    mage.assignAlignment(Alignment.LAWFUL_GOOD)
    println(mage.getCharacterSummary())
    
    // Exemplo 3: Personagem com distribuição aventureiro (customizada)
    println("\n" + "=".repeat(50) + "\n")
    println("3. Personagem com distribuição AVENTUREIRO (pontos customizados):")
    val attributeDistribution = AttributeDistribution()
    val customAttributes = attributeDistribution.distributePointsAdventurer(
        mapOf(
            "str" to 2,  // 8 + 2 = 10
            "dex" to 4,  // 8 + 4 = 12
            "con" to 3,  // 8 + 3 = 11
            "int" to 2,  // 8 + 2 = 10
            "wis" to 8,  // 8 + 8 = 16
            "cha" to 6   // 8 + 6 = 14
            // Total: 25 pontos (dentro do limite de 27)
        )
    )
    val cleric = Character(customAttributes)
    cleric.name = "Elaria"
    cleric.assignRace(Races.elf)
    cleric.assignCharacterClass(CharacterClasses.cleric)
    println(cleric.getCharacterSummary())
    
    // Exemplo 4: Demonstração dos ataques das classes
    println("\n" + "=".repeat(50) + "\n")
    println("4. Demonstração de ataques por classe:")
    
    println("\n--- GUERREIRO ---")
    CharacterClasses.fighter.stdAttack()
    CharacterClasses.fighter.effectAttack()
    CharacterClasses.fighter.specialAttack()
    CharacterClasses.fighter.ultraAttack()
    
    println("\n--- MAGO ---")
    CharacterClasses.wizard.stdAttack()
    CharacterClasses.wizard.effectAttack()
    CharacterClasses.wizard.specialAttack()
    CharacterClasses.wizard.ultraAttack()
    
    println("\n--- CLÉRIGO ---")
    CharacterClasses.cleric.stdAttack()
    CharacterClasses.cleric.effectAttack()
    CharacterClasses.cleric.specialAttack()
    CharacterClasses.cleric.ultraAttack()
    
    // Exemplo 5: Sistema de raças disponíveis
    println("\n" + "=".repeat(50) + "\n")
    println("5. Raças disponíveis:")
    Races.listAll().forEach { race ->
        println("${race.raceName}:")
        println("  Movimento: ${race.movement}")
        println("  Infravisão: ${race.infravision?.let { "${it}m" } ?: "Nenhuma"}")
        println("  Alinhamento Preferido: ${race.preferredAlignment}")
        println("  Habilidades:")
        race.racialHability.forEach { hability ->
            println("    - ${hability.habilityName}: ${hability.description}")
        }
        println()
    }
    
    // Exemplo 6: Sistema de classes disponíveis  
    println("6. Classes disponíveis:")
    CharacterClasses.listAll().forEach { charClass ->
        println("${charClass.className}:")
        println("  ${CharacterClasses.getClassDescription(charClass)}")
        println("  Atributo Principal: ${charClass.primaryAttribute}")
        println("  Dado de Vida: d${charClass.hitDie}")
        println("  Pode usar magias: ${if (charClass.canUseSpells()) "Sim" else "Não"}")
        println()
    }
}