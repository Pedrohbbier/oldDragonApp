package models.charClass

class Fighter : CharacterClass(
    className = "Guerreiro",
    hitDie = 10,
    primaryAttribute = "str",
    allowedArmor = listOf("leather", "chainmail", "platemail", "shield"),
    allowedWeapons = listOf("sword", "axe", "bow", "crossbow", "spear", "mace", "hammer")
) {
    
    override fun stdAttack() {
        val damage = dice.roll(8) + 2
        println("Ataque básico causa $damage de dano")
    }
    
    override fun effectAttack() {
        println("Golpe Atordoante: O inimigo perde a próxima ação (efeito negativo aplicado)")
    }
    
    override fun specialAttack() {
        val damage = dice.rollM(8, 2) + 5
        println("Golpe Poderoso causa $damage de dano")
    }
    
    override fun ultraAttack() {
        val damage = dice.rollM(10, 3) + 10
        println("Fúria Berserker causa $damage de dano em todos os inimigos próximos")
    }
    
    override fun getClassFeatures(level: Byte): List<String> {
        val features = mutableListOf<String>()
        features.add("Proficiência em todas as armas e armaduras")
        
        if (level >= 2) features.add("Ataque Extra")
        if (level >= 4) features.add("Especialização em Armas")
        if (level >= 6) features.add("Ataque Extra Adicional")
        if (level >= 8) features.add("Resistência a Medo")
        if (level >= 10) features.add("Maestria em Combate")
        
        return features
    }
    
    override fun canUseSpells(): Boolean = false
    
    override fun getSpellsPerDay(level: Byte): Map<Int, Int> = emptyMap()
}