package com.olddragon.app.models.charClass

object CharacterClasses {
    val fighter = Fighter()
    val wizard = Wizard()
    val cleric = Cleric()

    private val classRegistry = mapOf(
        "fighter" to fighter,
        "wizard" to wizard,
        "cleric" to cleric
    )

    fun findByName(name: String): CharacterClass? = classRegistry[name.lowercase()]

    fun listAll(): List<CharacterClass> = classRegistry.values.toList()
}