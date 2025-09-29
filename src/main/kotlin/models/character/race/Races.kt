package models.character.race

import models.character.hability.Habilities

object Races {
    val human = Race(
        raceName = "Human",
        infravision = null,
        movement = 9,
        preferredAlignment = Alignment.TRUE_NEUTRAL,
        racialHability = listOf(
            Habilities.rowdy,
            Habilities.curiosity
        )
    )
    val elf = Race(
        raceName = "Elf",
        infravision = 18,
        movement = 9,
        preferredAlignment = Alignment.CHAOTIC_GOOD,
        racialHability = listOf(
            Habilities.agility,
            Habilities.longLives
        )
    )
    val dwarf = Race(
        raceName = "Dwarf",
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