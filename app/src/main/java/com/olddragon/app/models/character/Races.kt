package com.olddragon.app.models.character

object Races {
    val human = Race(
        raceName = "Humano",
        infravision = null,
        movement = 9,
        preferredAlignment = Alignment.TRUE_NEUTRAL,
        racialHability = listOf(
            Habilities.rowdy,
            Habilities.curiosity
        )
    )

    val elf = Race(
        raceName = "Elfo",
        infravision = 18,
        movement = 9,
        preferredAlignment = Alignment.CHAOTIC_GOOD,
        racialHability = listOf(
            Habilities.agility,
            Habilities.longLives
        )
    )

    val dwarf = Race(
        raceName = "Anão",
        infravision = 18,
        movement = 6,
        preferredAlignment = Alignment.LAWFUL_GOOD,
        racialHability = listOf(
            Habilities.hardShell,
            Habilities.pubBrother
        )
    )

    private val raceRegistry = mapOf(
        "human" to human,
        "elf" to elf,
        "dwarf" to dwarf,
    )

    fun findByName(name: String): Race? = raceRegistry[name.lowercase()]

    fun listAll(): List<Race> = raceRegistry.values.toList()
}