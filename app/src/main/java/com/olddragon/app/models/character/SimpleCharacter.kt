package com.olddragon.app.models.character

data class SimpleCharacter(
    val name: String,
    val level: Byte,
    val hp: Byte,
    val xp: Int,
    val raceName: String?,
    val className: String?,
    val alignmentName: String?,
    val skills: LinkedHashMap<String, Byte>,
    val skillMod: LinkedHashMap<String, Byte>,
    val atributes: LinkedHashMap<String, Byte>
)