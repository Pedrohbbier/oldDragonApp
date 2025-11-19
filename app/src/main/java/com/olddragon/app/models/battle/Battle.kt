package com.olddragon.app.models.battle

import com.olddragon.app.data.database.entities.BattleEntity

/**
 * Modelo de domínio para Batalha
 */
data class Battle(
    val id: Long = 0,
    val characterId: Long,
    val characterName: String,
    val enemyId: Long,
    val enemyName: String,
    var status: BattleStatus,
    val characterInitialHp: Byte,
    var characterFinalHp: Byte,
    val enemyInitialHp: Byte,
    var enemyFinalHp: Byte,
    val battleLog: MutableList<String> = mutableListOf(),
    var totalRounds: Int = 0,
    var xpGained: Int = 0,
    val startedAt: Long = System.currentTimeMillis(),
    var finishedAt: Long? = null
) {
    fun addLogEntry(action: BattleAction) {
        battleLog.add(action.toString())
    }

    fun toEntity(): BattleEntity {
        return BattleEntity(
            id = id,
            characterId = characterId,
            characterName = characterName,
            enemyId = enemyId,
            enemyName = enemyName,
            status = status.name,
            characterInitialHp = characterInitialHp,
            characterFinalHp = characterFinalHp,
            enemyInitialHp = enemyInitialHp,
            enemyFinalHp = enemyFinalHp,
            battleLog = battleLog.toList(),
            totalRounds = totalRounds,
            xpGained = xpGained,
            startedAt = startedAt,
            finishedAt = finishedAt
        )
    }

    companion object {
        fun fromEntity(entity: BattleEntity): Battle {
            return Battle(
                id = entity.id,
                characterId = entity.characterId,
                characterName = entity.characterName,
                enemyId = entity.enemyId,
                enemyName = entity.enemyName,
                status = BattleStatus.fromString(entity.status),
                characterInitialHp = entity.characterInitialHp,
                characterFinalHp = entity.characterFinalHp,
                enemyInitialHp = entity.enemyInitialHp,
                enemyFinalHp = entity.enemyFinalHp,
                battleLog = entity.battleLog.toMutableList(),
                totalRounds = entity.totalRounds,
                xpGained = entity.xpGained,
                startedAt = entity.startedAt,
                finishedAt = entity.finishedAt
            )
        }
    }
}
