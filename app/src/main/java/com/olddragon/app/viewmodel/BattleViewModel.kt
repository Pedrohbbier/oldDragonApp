package com.olddragon.app.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.olddragon.app.data.database.OldDragonDatabase
import com.olddragon.app.data.repository.BattleRepository
import com.olddragon.app.data.repository.CharacterRepository
import com.olddragon.app.data.repository.EnemyRepository
import com.olddragon.app.models.battle.Battle
import com.olddragon.app.models.character.Character
import com.olddragon.app.service.BattleService
import com.olddragon.app.service.BattleState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel para gerenciar batalhas
 */
class BattleViewModel(application: Application) : AndroidViewModel(application) {

    private val database = OldDragonDatabase.getDatabase(application)
    private val characterRepository = CharacterRepository(database.characterDao())
    private val enemyRepository = EnemyRepository(database.enemyDao())
    private val battleRepository = BattleRepository(database.battleDao())

    private var battleService: BattleService? = null
    private var isBound = false

    private val _battleState = MutableStateFlow<BattleState>(BattleState.Idle)
    val battleState: StateFlow<BattleState> = _battleState

    private val _currentCharacter = MutableStateFlow<Character?>(null)
    val currentCharacter: StateFlow<Character?> = _currentCharacter

    val allBattles: StateFlow<List<Battle>> = battleRepository.getAllBattles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BattleService.BattleServiceBinder
            battleService = binder.getService()
            isBound = true

            // Observa estado do serviço
            viewModelScope.launch {
                battleService?.battleState?.collect { state ->
                    _battleState.value = state
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            battleService = null
            isBound = false
        }
    }

    init {
        // Carrega personagem ativo
        viewModelScope.launch {
            characterRepository.getActiveCharacter().collect { character ->
                _currentCharacter.value = character
            }
        }

        // Popula inimigos padrão
        viewModelScope.launch {
            enemyRepository.populateDefaultEnemies()
        }
    }

    /**
     * Inicia uma batalha
     */
    fun startBattle(characterId: Long) {
        val context = getApplication<Application>()

        // Bind ao serviço
        val intent = Intent(context, BattleService::class.java).apply {
            action = BattleService.ACTION_START_BATTLE
            putExtra(BattleService.EXTRA_CHARACTER_ID, characterId)
        }

        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        context.startForegroundService(intent)
    }

    /**
     * Para uma batalha em andamento
     */
    fun stopBattle() {
        val context = getApplication<Application>()
        val intent = Intent(context, BattleService::class.java).apply {
            action = BattleService.ACTION_STOP_BATTLE
        }
        context.startService(intent)

        unbindService()
    }

    /**
     * Obtém histórico de batalhas de um personagem
     */
    fun getBattleHistory(characterId: Long): Flow<List<Battle>> {
        return battleRepository.getBattlesByCharacter(characterId)
    }

    /**
     * Obtém estatísticas de batalha
     */
    suspend fun getBattleStats(characterId: Long): BattleStats {
        val victories = battleRepository.getVictoryCount(characterId)
        val defeats = battleRepository.getDefeatCount(characterId)
        return BattleStats(victories, defeats)
    }

    /**
     * Unbind do serviço
     */
    private fun unbindService() {
        if (isBound) {
            getApplication<Application>().unbindService(serviceConnection)
            isBound = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        unbindService()
    }
}

/**
 * Estatísticas de batalha
 */
data class BattleStats(
    val victories: Int,
    val defeats: Int
) {
    val total: Int
        get() = victories + defeats

    val winRate: Float
        get() = if (total > 0) victories.toFloat() / total else 0f
}
