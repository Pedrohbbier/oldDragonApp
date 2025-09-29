package models.character.hability

data class Hability(
    val habilityName: String,
    val description: String,
    val modType: String,
    val modStat: String,
    val modAmmount: Byte
)