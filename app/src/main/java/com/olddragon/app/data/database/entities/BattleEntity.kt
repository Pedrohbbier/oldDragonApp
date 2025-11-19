package com.olddragon.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.olddragon.app.data.database.Converters

/**
 * Entity do Room para armazenar batalhas
 */
@Entity(tableName = "battles")
@TypeConverters(Converters::class)
data class BattleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val characterId: Long,
    val characterName: String,
    val enemyId: Long,
    val enemyName: String,

    // Status da batalha
    val status: String, // "ONGOING", "VICTORY", "DEFEAT"

    // Dados da batalha
    val characterInitialHp: Byte,
    val characterFinalHp: Byte,
    val enemyInitialHp: Byte,
    val enemyFinalHp: Byte,

    // Log de ações (armazenado como JSON)
    val battleLog: List<String>,

    // Rodadas
    val totalRounds: Int,

    // XP ganho (se vitória)
    val xpGained: Int,

    // Timestamps
    val startedAt: Long = System.currentTimeMillis(),
    val finishedAt: Long? = null
)
