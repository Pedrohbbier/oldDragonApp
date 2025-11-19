package com.olddragon.app.models.battle

/**
 * Status da batalha
 */
enum class BattleStatus {
    ONGOING,
    VICTORY,
    DEFEAT;

    companion object {
        fun fromString(value: String): BattleStatus {
            return values().find { it.name == value } ?: ONGOING
        }
    }
}
