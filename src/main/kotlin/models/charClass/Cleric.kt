package models.charClass

class Cleric : CharacterClass(
    className = "Clérigo",
    hitDie = 8,
    primaryAttribute = "wis",
    allowedArmor = listOf("leather", "chainmail", "shield"),
    allowedWeapons = listOf("mace", "hammer", "staff", "sling", "crossbow")
) {
    
    override fun stdAttack() {
        val damage = dice.roll(6) + 2
        println("Ataque básico com maça causa $damage de dano")
    }
    
    override fun effectAttack() {
        println("Palavra Sagrada: Stun em mortos-vivos ou medo em inimigos vivos (efeito divino)")
    }
    
    override fun specialAttack() {
        val healing = dice.rollM(8, 2) + 5
        println("Cura Ferimentos: Restaura $healing pontos de vida a um aliado")
    }
    
    override fun ultraAttack() {
        val damage = dice.rollM(6, 5) + 15
        println("Coluna de Fogo Divino causa $damage de dano a todos os inimigos malignos")
    }
    
    override fun getClassFeatures(level: Byte): List<String> {
        val features = mutableListOf<String>()
        features.add("Conjuração de Magias Divinas")
        features.add("Expulsar Mortos-Vivos")
        
        if (level >= 2) features.add("Resistência a Feitiços")
        if (level >= 4) features.add("Cura Aprimorada")
        if (level >= 6) features.add("Proteção Divina")
        if (level >= 8) features.add("Comunhão com Divindade")
        if (level >= 10) features.add("Avatar - Poder Divino")
        
        return features
    }
    
    override fun canUseSpells(): Boolean = true
    
    override fun getSpellsPerDay(level: Byte): Map<Int, Int> {
        return when (level) {
            1.toByte() -> mapOf(1 to 1)
            2.toByte() -> mapOf(1 to 2)
            3.toByte() -> mapOf(1 to 2, 2 to 1)
            4.toByte() -> mapOf(1 to 3, 2 to 2)
            5.toByte() -> mapOf(1 to 3, 2 to 3, 3 to 1)
            6.toByte() -> mapOf(1 to 3, 2 to 3, 3 to 2)
            7.toByte() -> mapOf(1 to 4, 2 to 4, 3 to 2, 4 to 1)
            8.toByte() -> mapOf(1 to 4, 2 to 4, 3 to 3, 4 to 2)
            9.toByte() -> mapOf(1 to 5, 2 to 5, 3 to 3, 4 to 2, 5 to 1)
            else -> mapOf(1 to 6, 2 to 6, 3 to 4, 4 to 3, 5 to 2)
        }
    }
}