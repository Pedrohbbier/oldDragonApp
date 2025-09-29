package com.olddragon.app.models.character

data class Hability(
    val habilityName: String,
    val description: String,
    val modType: String,
    val modStat: String,
    val modAmmount: Byte
)