package com.olddragon.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity do Room para armazenar inimigos/monstros
 */
@Entity(tableName = "enemies")
data class EnemyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val level: Byte,
    val maxHp: Byte,
    val currentHp: Byte,

    // Atributos de combate
    val armorClass: Byte,
    val attackBonus: Byte,
    val damageRoll: String, // Ex: "1d8+2"

    // XP que dá ao derrotar
    val xpReward: Int,

    // Timestamps
    val createdAt: Long = System.currentTimeMillis()
)
