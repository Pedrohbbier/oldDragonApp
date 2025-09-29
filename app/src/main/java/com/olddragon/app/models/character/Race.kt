package com.olddragon.app.models.character

data class Race(
    val raceName: String,
    val infravision: Byte?,
    val movement: Byte,
    val preferredAlignment: Alignment,
    val racialHability: List<Hability> = listOf()
)