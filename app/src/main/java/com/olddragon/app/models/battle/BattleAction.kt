package com.olddragon.app.models.battle

/**
 * Ação de batalha que pode ser registrada no log
 */
sealed class BattleAction {
    data class Attack(
        val attacker: String,
        val defender: String,
        val attackRoll: Int,
        val targetAC: Int,
        val hit: Boolean,
        val damage: Int = 0
    ) : BattleAction() {
        override fun toString(): String {
            return if (hit) {
                "$attacker ataca $defender! Rolou $attackRoll vs CA $targetAC. Acertou! Dano: $damage"
            } else {
                "$attacker ataca $defender! Rolou $attackRoll vs CA $targetAC. Errou!"
            }
        }
    }

    data class Death(val name: String, val isPlayer: Boolean) : BattleAction() {
        override fun toString(): String {
            return if (isPlayer) {
                "$name foi derrotado!"
            } else {
                "$name foi eliminado!"
            }
        }
    }

    data class RoundStart(val round: Int) : BattleAction() {
        override fun toString(): String {
            return "=== Rodada $round ==="
        }
    }

    data class BattleStart(val playerName: String, val enemyName: String) : BattleAction() {
        override fun toString(): String {
            return "Batalha iniciada: $playerName vs $enemyName!"
        }
    }

    data class BattleEnd(val winner: String, val xpGained: Int = 0) : BattleAction() {
        override fun toString(): String {
            return if (xpGained > 0) {
                "$winner venceu a batalha! XP ganho: $xpGained"
            } else {
                "$winner venceu a batalha!"
            }
        }
    }
}
