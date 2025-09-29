package models.charClass

object CharacterClasses {
    val fighter = Fighter()
    val wizard = Wizard()
    val cleric = Cleric()
    
    private val classRegistry = mapOf(
        "fighter" to fighter,
        "guerreiro" to fighter,
        "wizard" to wizard,
        "mago" to wizard,
        "cleric" to cleric,
        "clerigo" to cleric
    )
    
    fun findByName(name: String): CharacterClass? = classRegistry[name.lowercase()]
    
    fun listAll(): List<CharacterClass> = listOf(fighter, wizard, cleric)
    
    fun getClassDescription(characterClass: CharacterClass): String {
        return when (characterClass) {
            is Fighter -> "Guerreiros são mestres do combate corpo a corpo, usando força e técnica para derrotar seus inimigos."
            is Wizard -> "Magos manipulam as forças arcanas através de estudo e inteligência, conjurando poderosas magias."
            is Cleric -> "Clérigos canalizam o poder divino para curar aliados e destruir inimigos, servindo suas divindades."
            else -> "Classe desconhecida"
        }
    }
}