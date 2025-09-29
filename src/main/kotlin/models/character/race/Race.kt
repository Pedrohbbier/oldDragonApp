package models.character.race

import models.character.hability.Hability

enum class Alignment {
    LAWFUL_GOOD,
    NEUTRAL_GOOD, 
    CHAOTIC_GOOD,
    LAWFUL_NEUTRAL,
    TRUE_NEUTRAL,
    CHAOTIC_NEUTRAL,
    LAWFUL_EVIL,
    NEUTRAL_EVIL,
    CHAOTIC_EVIL
}

data class Race(
    val raceName: String,
    val infravision: Byte?,
    val movement: Byte,
    val preferredAlignment: Alignment,
    val racialHability: List<Hability> = listOf<Hability>()
)