package com.olddragon.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.olddragon.app.data.database.Converters

/**
 * Entity do Room para armazenar personagens
 */
@Entity(tableName = "characters")
@TypeConverters(Converters::class)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val level: Byte,
    val currentHp: Byte,
    val maxHp: Byte,
    val xp: Int,

    // Raça, Classe e Alinhamento armazenados como strings
    val raceName: String?,
    val className: String?,
    val alignmentName: String?,

    // Atributos (STR, DEX, CON, INT, WIS, CHA)
    val skills: LinkedHashMap<String, Byte>,
    val skillMod: LinkedHashMap<String, Byte>,

    // Atributos de combate (CA, BA, JP)
    val attributes: LinkedHashMap<String, Byte>,

    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    // Flag para personagem ativo/principal
    val isActive: Boolean = false
)
