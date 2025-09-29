package models.charClass

class Wizard : CharacterClass(
    className = "Mago",
    hitDie = 4,
    primaryAttribute = "int",
    allowedArmor = listOf("robes"),
    allowedWeapons = listOf("dagger", "staff", "dart")
) {
    
    override fun stdAttack() {
        val damage = dice.roll(4) + 1
        println("Ataque básico com cajado causa $damage de dano")
    }
    
    override fun effectAttack() {
        println("Míssil Mágico: Causa dano garantido que ignora armadura (efeito mágico)")
    }
    
    override fun specialAttack() {
        val damage = dice.rollM(6, 3)
        println("Bola de Fogo causa $damage de dano em área")
    }
    
    override fun ultraAttack() {
        val damage = dice.rollM(8, 4) + 20
        println("Meteoro causa $damage de dano massivo em grande área")
    }
    
    override fun getClassFeatures(level: Byte): List<String> {
        val features = mutableListOf<String>()
        features.add("Conjuração de Magias Arcanas")
        features.add("Leitura de Pergaminhos Mágicos")
        
        if (level >= 3) features.add("Escolas de Magia Especializadas")
        if (level >= 5) features.add("Criação de Itens Mágicos")
        if (level >= 7) features.add("Magias de Alto Nível")
        if (level >= 9) features.add("Archmage - Magias Épicas")
        
        return features
    }
    
    override fun canUseSpells(): Boolean = true
    
    override fun getSpellsPerDay(level: Byte): Map<Int, Int> {
        return when (level) {
            1.toByte() -> mapOf(1 to 1)
            2.toByte() -> mapOf(1 to 2)
            3.toByte() -> mapOf(1 to 2, 2 to 1)
            4.toByte() -> mapOf(1 to 3, 2 to 2)
            5.toByte() -> mapOf(1 to 4, 2 to 2, 3 to 1)
            6.toByte() -> mapOf(1 to 4, 2 to 3, 3 to 2)
            7.toByte() -> mapOf(1 to 4, 2 to 3, 3 to 2, 4 to 1)
            8.toByte() -> mapOf(1 to 4, 2 to 4, 3 to 3, 4 to 2)
            9.toByte() -> mapOf(1 to 5, 2 to 4, 3 to 3, 4 to 2, 5 to 1)
            else -> mapOf(1 to 5, 2 to 5, 3 to 4, 4 to 3, 5 to 2)
        }
    }
}