package com.olddragon.app.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.olddragon.app.data.database.OldDragonDatabase
import com.olddragon.app.data.repository.BattleRepository
import com.olddragon.app.data.repository.CharacterRepository
import com.olddragon.app.data.repository.EnemyRepository
import com.olddragon.app.models.battle.Battle
import com.olddragon.app.models.battle.BattleEngine
import com.olddragon.app.models.battle.BattleStatus
import com.olddragon.app.models.battle.Enemy
import com.olddragon.app.models.character.Character
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

/**
 * Foreground Service para executar batalhas em background
 */
class BattleService : Service() {

    private val binder = BattleServiceBinder()
    private lateinit var notificationManager: BattleNotificationManager
    private lateinit var characterRepository: CharacterRepository
    private lateinit var enemyRepository: EnemyRepository
    private lateinit var battleRepository: BattleRepository

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var battleJob: Job? = null

    private val _battleState = MutableStateFlow<BattleState>(BattleState.Idle)
    val battleState: StateFlow<BattleState> = _battleState

    private var currentBattleEngine: BattleEngine? = null

    companion object {
        const val TAG = "BattleService"
        const val ACTION_START_BATTLE = "com.olddragon.app.START_BATTLE"
        const val ACTION_STOP_BATTLE = "com.olddragon.app.STOP_BATTLE"
        const val EXTRA_CHARACTER_ID = "character_id"
        const val ROUND_DELAY_MS = 5000L // 5 segundos entre rodadas (mais tempo para ver)
    }

    inner class BattleServiceBinder : Binder() {
        fun getService(): BattleService = this@BattleService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Battle Service Created")

        notificationManager = BattleNotificationManager(this)

        val database = OldDragonDatabase.getDatabase(this)
        characterRepository = CharacterRepository(database.characterDao())
        enemyRepository = EnemyRepository(database.enemyDao())
        battleRepository = BattleRepository(database.battleDao())
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_BATTLE -> {
                val characterId = intent.getLongExtra(EXTRA_CHARACTER_ID, -1)
                if (characterId != -1L) {
                    startBattle(characterId)
                }
            }
            ACTION_STOP_BATTLE -> {
                stopBattle()
            }
        }
        return START_STICKY
    }

    /**
     * Inicia uma batalha
     */
    private fun startBattle(characterId: Long) {
        battleJob?.cancel()

        battleJob = serviceScope.launch {
            try {
                _battleState.value = BattleState.Loading

                // Carrega personagem
                Log.d(TAG, "Carregando personagem ID: $characterId")
                val character = loadCharacter(characterId)
                if (character == null) {
                    Log.e(TAG, "Personagem não encontrado!")
                    _battleState.value = BattleState.Error("Personagem não encontrado")
                    return@launch
                }
                Log.d(TAG, "Personagem carregado: ${character.name}, HP: ${character.hp}, Level: ${character.level}")

                // Carrega inimigo aleatório (máximo nível do personagem + 1)
                val maxEnemyLevel = (character.level + 1).toByte()
                Log.d(TAG, "Buscando inimigo de nível máximo: $maxEnemyLevel")
                val enemy = enemyRepository.getRandomEnemyByLevel(maxEnemyLevel)
                if (enemy == null) {
                    Log.e(TAG, "Nenhum inimigo disponível!")
                    _battleState.value = BattleState.Error("Nenhum inimigo disponível")
                    return@launch
                }
                Log.d(TAG, "Inimigo selecionado: ${enemy.name}, HP: ${enemy.currentHp}, Level: ${enemy.level}")

                // Inicia batalha
                val battleEngine = BattleEngine(character, enemy)
                currentBattleEngine = battleEngine

                // Inicia foreground service com notificação
                Log.d(TAG, "Iniciando foreground service...")
                val notification = notificationManager.createBattleNotification(
                    character.name,
                    enemy.name,
                    0,
                    character.hp,
                    enemy.currentHp
                )
                startForeground(BattleNotificationManager.NOTIFICATION_ID, notification)

                // Pequeno delay para garantir que a UI está pronta
                delay(500)

                // Salva batalha inicial
                val initialBattle = battleEngine.getCurrentBattle()
                val battleId = battleRepository.saveBattle(initialBattle)
                Log.d(TAG, "Batalha salva com ID: $battleId")

                // Atualiza estado inicial
                _battleState.value = BattleState.InProgress(
                    battle = initialBattle,
                    round = 0,
                    playerHp = character.hp,
                    enemyHp = enemy.currentHp
                )

                // Pequeno delay antes de começar as rodadas
                delay(1000)

                // Executa rodadas
                Log.d(TAG, "Iniciando rodadas de batalha...")
                executeBattleRounds(battleEngine, character, enemy, battleId)

            } catch (e: Exception) {
                Log.e(TAG, "Error in battle", e)
                _battleState.value = BattleState.Error(e.message ?: "Erro desconhecido")
                stopForeground(STOP_FOREGROUND_REMOVE)
            }
        }
    }

    /**
     * Executa rodadas de batalha
     */
    private suspend fun executeBattleRounds(
        battleEngine: BattleEngine,
        character: Character,
        enemy: Enemy,
        battleId: Long
    ) {
        var battleOngoing = true
        while (battleOngoing && serviceScope.isActive) {
            // Executa rodada
            Log.d(TAG, "Executando rodada ${enemy.currentHp > 0 && character.hp > 0}")
            val battle = battleEngine.executeRound()
            Log.d(TAG, "Rodada ${battle.totalRounds}: Player HP=${character.hp}, Enemy HP=${enemy.currentHp}, Status=${battle.status}")

            // Atualiza estado
            _battleState.value = BattleState.InProgress(
                battle = battle,
                round = battle.totalRounds,
                playerHp = character.hp,
                enemyHp = enemy.currentHp
            )

            // Atualiza notificação
            notificationManager.updateBattleNotification(
                character.name,
                enemy.name,
                battle.totalRounds,
                character.hp,
                enemy.currentHp
            )

            // Atualiza batalha no banco
            battleRepository.updateBattle(battle.copy(id = battleId))

            // Verifica fim da batalha
            if (battle.status != BattleStatus.ONGOING) {
                Log.d(TAG, "Batalha finalizada com status: ${battle.status}")
                battleOngoing = false
                handleBattleEnd(battle, character, battleId)
            } else {
                // Aguarda antes da próxima rodada
                delay(ROUND_DELAY_MS)
            }
        }
    }

    /**
     * Trata fim da batalha
     */
    private suspend fun handleBattleEnd(battle: Battle, character: Character, battleId: Long) {
        when (battle.status) {
            BattleStatus.VICTORY -> {
                Log.d(TAG, "VITÓRIA! XP ganho: ${battle.xpGained}")
                // Atualiza XP do personagem
                characterRepository.updateCharacterXp(character.id, character.xp)
                characterRepository.updateCharacterHp(character.id, character.hp)

                // Envia notificação de vitória
                notificationManager.sendVictoryNotification(
                    battle.characterName,
                    battle.enemyName,
                    battle.xpGained
                )

                _battleState.value = BattleState.Completed(battle, victory = true)
            }
            BattleStatus.DEFEAT -> {
                Log.d(TAG, "DERROTA! ${battle.characterName} morreu!")
                // Atualiza HP do personagem (morto)
                characterRepository.updateCharacterHp(character.id, 0)

                // Envia notificação de morte - IMPORTANTE!
                Log.d(TAG, "Enviando notificação de morte...")
                notificationManager.sendDeathNotification(battle.characterName)
                Log.d(TAG, "Notificação de morte enviada!")

                _battleState.value = BattleState.Completed(battle, victory = false)
            }
            else -> {}
        }

        // Atualiza batalha final
        battleRepository.updateBattle(battle.copy(id = battleId))

        // Aguarda um pouco antes de parar o service
        delay(2000)

        // Para o foreground service
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**
     * Para a batalha
     */
    private fun stopBattle() {
        battleJob?.cancel()
        currentBattleEngine = null
        _battleState.value = BattleState.Idle
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**
     * Carrega personagem do banco de dados
     */
    private suspend fun loadCharacter(characterId: Long): Character? {
        return withContext(Dispatchers.IO) {
            characterRepository.getCharacterById(characterId).first()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Battle Service Destroyed")
        serviceScope.cancel()
        notificationManager.cancelAll()
    }
}

/**
 * Estados possíveis do serviço de batalha
 */
sealed class BattleState {
    object Idle : BattleState()
    object Loading : BattleState()
    data class InProgress(
        val battle: Battle,
        val round: Int,
        val playerHp: Byte,
        val enemyHp: Byte
    ) : BattleState()
    data class Completed(val battle: Battle, val victory: Boolean) : BattleState()
    data class Error(val message: String) : BattleState()
}
