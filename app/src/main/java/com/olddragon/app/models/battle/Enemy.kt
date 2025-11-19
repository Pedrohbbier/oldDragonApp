package com.olddragon.app.models.battle

import com.olddragon.app.data.database.entities.EnemyEntity

/**
 * Modelo de domínio para Inimigos
 */
data class Enemy(
    val id: Long = 0,
    val name: String,
    val level: Byte,
    val maxHp: Byte,
    var currentHp: Byte,
    val armorClass: Byte,
    val attackBonus: Byte,
    val damageRoll: String,
    val xpReward: Int
) {
    val isAlive: Boolean
        get() = currentHp > 0

    fun takeDamage(damage: Byte) {
        currentHp = (currentHp - damage).coerceAtLeast(0).toByte()
    }

    fun toEntity(): EnemyEntity {
        return EnemyEntity(
            id = id,
            name = name,
            level = level,
            maxHp = maxHp,
            currentHp = currentHp,
            armorClass = armorClass,
            attackBonus = attackBonus,
            damageRoll = damageRoll,
            xpReward = xpReward
        )
    }

    companion object {
        fun fromEntity(entity: EnemyEntity): Enemy {
            return Enemy(
                id = entity.id,
                name = entity.name,
                level = entity.level,
                maxHp = entity.maxHp,
                currentHp = entity.currentHp,
                armorClass = entity.armorClass,
                attackBonus = entity.attackBonus,
                damageRoll = entity.damageRoll,
                xpReward = entity.xpReward
            )
        }
    }
}
