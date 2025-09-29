package com.olddragon.app.models.character

object Habilities {
    val rowdy = Hability(
        habilityName = "Rowdy",
        description = "+1 to Charisma",
        modType = "skill",
        modStat = "cha",
        modAmmount = 1
    )

    val curiosity = Hability(
        habilityName = "Curiosity",
        description = "+1 to Intelligence",
        modType = "skill",
        modStat = "int",
        modAmmount = 1
    )

    val agility = Hability(
        habilityName = "Agility",
        description = "+1 to Dexterity",
        modType = "skill",
        modStat = "dex",
        modAmmount = 1
    )

    val longLives = Hability(
        habilityName = "Long Lives",
        description = "+1 to Wisdom",
        modType = "skill",
        modStat = "wis",
        modAmmount = 1
    )

    val hardShell = Hability(
        habilityName = "Hard Shell",
        description = "+1 to Constitution",
        modType = "skill",
        modStat = "con",
        modAmmount = 1
    )

    val pubBrother = Hability(
        habilityName = "Tavern Brother",
        description = "+1 to Strength",
        modType = "skill",
        modStat = "str",
        modAmmount = 1
    )
}