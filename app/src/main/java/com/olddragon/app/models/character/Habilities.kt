package com.olddragon.app.models.character

object Habilities {
    val rowdy = Hability(
        habilityName = "Bagunceiro",
        description = "+1 em Carisma",
        modType = "skill",
        modStat = "cha",
        modAmmount = 1
    )

    val curiosity = Hability(
        habilityName = "Curiosidade",
        description = "+1 em Inteligência",
        modType = "skill",
        modStat = "int",
        modAmmount = 1
    )

    val agility = Hability(
        habilityName = "Agilidade",
        description = "+1 em Destreza",
        modType = "skill",
        modStat = "dex",
        modAmmount = 1
    )

    val longLives = Hability(
        habilityName = "Longa Vida",
        description = "+1 em Sabedoria",
        modType = "skill",
        modStat = "wis",
        modAmmount = 1
    )

    val hardShell = Hability(
        habilityName = "Casco Duro",
        description = "+1 em Constituição",
        modType = "skill",
        modStat = "con",
        modAmmount = 1
    )

    val pubBrother = Hability(
        habilityName = "Irmão de Taverna",
        description = "+1 em Força",
        modType = "skill",
        modStat = "str",
        modAmmount = 1
    )
}