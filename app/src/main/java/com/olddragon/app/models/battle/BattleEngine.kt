package com.olddragon.app.models.battle

import com.olddragon.app.models.character.Character
import com.olddragon.app.models.dice.Dice
import kotlin.math.max

/**
 * Engine de batalha implementando regras do Old Dragon
 */
class BattleEngine(
    private val player: Character,
    private val enemy: Enemy
) {
    private val dice = Dice()
    private val battle: Battle
    private var currentRound = 0

    init {
        battle = Battle(
            characterId = 0, // Será atualizado ao salvar
            characterName = player.name,
            enemyId = enemy.id,
            enemyName = enemy.name,
            status = BattleStatus.ONGOING,
            characterInitialHp = player.hp,
            characterFinalHp = player.hp,
            enemyInitialHp = enemy.maxHp,
            enemyFinalHp = enemy.currentHp
        )

        battle.addLogEntry(BattleAction.BattleStart(player.name, enemy.name))
    }

    /**
     * Executa uma rodada completa de batalha
     */
    fun executeRound(): Battle {
        currentRound++
        battle.totalRounds = currentRound
        battle.addLogEntry(BattleAction.RoundStart(currentRound))

        // Determina iniciativa (quem ataca primeiro)
        val playerDex = player.skillMod["dex"] ?: 0
        val playerInitiative = dice.roll(20) + playerDex
        val enemyInitiative = dice.roll(20)

        if (playerInitiative >= enemyInitiative) {
            // Jogador ataca primeiro
            performPlayerAttack()
            if (enemy.isAlive) {
                performEnemyAttack()
            }
        } else {
            // Inimigo ataca primeiro
            performEnemyAttack()
            if (player.hp > 0) {
                performPlayerAttack()
            }
        }

        // Atualiza HP final
        battle.characterFinalHp = player.hp
        battle.enemyFinalHp = enemy.currentHp

        // Verifica condição de vitória/derrota
        checkBattleEnd()

        return battle
    }

    /**
     * Executa ataque do jogador
     */
    private fun performPlayerAttack() {
        val attackRoll = dice.roll(20)
        val attackBonus = (player.atributes["ba"] ?: 0).toInt()
        val totalAttack = attackRoll + attackBonus

        val hit = totalAttack >= enemy.armorClass

        if (hit) {
            // Calcula dano baseado na classe
            val damageRoll = calculatePlayerDamage()
            val strMod = player.skillMod["str"] ?: 0
            val totalDamage = max(1, damageRoll + strMod)

            enemy.takeDamage(totalDamage.toByte())

            battle.addLogEntry(
                BattleAction.Attack(
                    attacker = player.name,
                    defender = enemy.name,
                    attackRoll = totalAttack,
                    targetAC = enemy.armorClass.toInt(),
                    hit = true,
                    damage = totalDamage
                )
            )

            if (!enemy.isAlive) {
                battle.addLogEntry(BattleAction.Death(enemy.name, isPlayer = false))
            }
        } else {
            battle.addLogEntry(
                BattleAction.Attack(
                    attacker = player.name,
                    defender = enemy.name,
                    attackRoll = totalAttack,
                    targetAC = enemy.armorClass.toInt(),
                    hit = false
                )
            )
        }
    }

    /**
     * Executa ataque do inimigo
     */
    private fun performEnemyAttack() {
        val attackRoll = dice.roll(20)
        val totalAttack = attackRoll + enemy.attackBonus

        val playerAC = (player.atributes["ca"] ?: 10).toInt()
        val hit = totalAttack >= playerAC

        if (hit) {
            val damage = parseDamageRoll(enemy.damageRoll)
            val totalDamage = max(1, damage)

            player.hp = (player.hp - totalDamage).coerceAtLeast(0).toByte()

            battle.addLogEntry(
                BattleAction.Attack(
                    attacker = enemy.name,
                    defender = player.name,
                    attackRoll = totalAttack,
                    targetAC = playerAC,
                    hit = true,
                    damage = totalDamage
                )
            )

            if (player.hp <= 0) {
                battle.addLogEntry(BattleAction.Death(player.name, isPlayer = true))
            }
        } else {
            battle.addLogEntry(
                BattleAction.Attack(
                    attacker = enemy.name,
                    defender = player.name,
                    attackRoll = totalAttack,
                    targetAC = playerAC,
                    hit = false
                )
            )
        }
    }

    /**
     * Calcula dano do jogador baseado na classe
     */
    private fun calculatePlayerDamage(): Int {
        return when (player.characterClass?.className) {
            "Fighter" -> dice.rollM(8, 1) // 1d8
            "Wizard" -> dice.rollM(4, 1)  // 1d4 (dagger)
            "Cleric" -> dice.rollM(6, 1)  // 1d6 (mace)
            else -> dice.rollM(6, 1)       // 1d6 padrão
        }
    }

    /**
     * Parse damage roll string (ex: "1d8+2", "2d6")
     */
    private fun parseDamageRoll(damageRoll: String): Int {
        try {
            val parts = damageRoll.split("+", "-")
            val dicePart = parts[0].trim()
            val modifier = if (parts.size > 1) {
                if (damageRoll.contains("+")) {
                    parts[1].trim().toInt()
                } else {
                    -parts[1].trim().toInt()
                }
            } else {
                0
            }

            val diceComponents = dicePart.split("d")
            val quantity = diceComponents[0].toInt()
            val sides = diceComponents[1].toInt()

            return dice.rollM(sides, quantity) + modifier
        } catch (e: Exception) {
            return dice.rollM(6, 1) // Fallback para 1d6
        }
    }

    /**
     * Verifica se a batalha terminou
     */
    private fun checkBattleEnd() {
        when {
            player.hp <= 0 -> {
                battle.status = BattleStatus.DEFEAT
                battle.finishedAt = System.currentTimeMillis()
                battle.addLogEntry(BattleAction.BattleEnd(enemy.name))
            }
            !enemy.isAlive -> {
                battle.status = BattleStatus.VICTORY
                battle.finishedAt = System.currentTimeMillis()
                battle.xpGained = enemy.xpReward
                player.xp += enemy.xpReward
                battle.addLogEntry(BattleAction.BattleEnd(player.name, enemy.xpReward))
            }
        }
    }

    /**
     * Executa batalha completa (para simulação automática)
     */
    fun executeBattle(): Battle {
        while (battle.status == BattleStatus.ONGOING) {
            executeRound()

            // Proteção contra loops infinitos
            if (currentRound > 100) {
                break
            }
        }
        return battle
    }

    /**
     * Retorna estado atual da batalha
     */
    fun getCurrentBattle(): Battle {
        return battle
    }

    /**
     * Retorna informações do jogador
     */
    fun getPlayerInfo(): Pair<String, Byte> {
        return Pair(player.name, player.hp)
    }

    /**
     * Retorna informações do inimigo
     */
    fun getEnemyInfo(): Pair<String, Byte> {
        return Pair(enemy.name, enemy.currentHp)
    }
}
