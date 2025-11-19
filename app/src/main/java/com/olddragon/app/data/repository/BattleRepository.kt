package com.olddragon.app.data.repository

import com.olddragon.app.data.database.dao.BattleDao
import com.olddragon.app.models.battle.Battle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository para gerenciar batalhas
 */
class BattleRepository(private val battleDao: BattleDao) {

    /**
     * Obtém todas as batalhas
     */
    fun getAllBattles(): Flow<List<Battle>> {
        return battleDao.getAllBattles().map { entities ->
            entities.map { Battle.fromEntity(it) }
        }
    }

    /**
     * Obtém batalha por ID
     */
    fun getBattleById(id: Long): Flow<Battle?> {
        return battleDao.getBattleById(id).map { entity ->
            entity?.let { Battle.fromEntity(it) }
        }
    }

    /**
     * Obtém batalhas de um personagem
     */
    fun getBattlesByCharacter(characterId: Long): Flow<List<Battle>> {
        return battleDao.getBattlesByCharacter(characterId).map { entities ->
            entities.map { Battle.fromEntity(it) }
        }
    }

    /**
     * Obtém batalha em andamento
     */
    fun getOngoingBattle(): Flow<Battle?> {
        return battleDao.getOngoingBattle().map { entity ->
            entity?.let { Battle.fromEntity(it) }
        }
    }

    /**
     * Obtém batalha em andamento (síncrono)
     */
    suspend fun getOngoingBattleSync(): Battle? {
        val entity = battleDao.getOngoingBattleSync()
        return entity?.let { Battle.fromEntity(it) }
    }

    /**
     * Salva uma batalha
     */
    suspend fun saveBattle(battle: Battle): Long {
        return battleDao.insertBattle(battle.toEntity())
    }

    /**
     * Atualiza uma batalha
     */
    suspend fun updateBattle(battle: Battle) {
        battleDao.updateBattle(battle.toEntity())
    }

    /**
     * Deleta uma batalha
     */
    suspend fun deleteBattle(battle: Battle) {
        battleDao.deleteBattle(battle.toEntity())
    }

    /**
     * Obtém contagem de vitórias
     */
    suspend fun getVictoryCount(characterId: Long): Int {
        return battleDao.getVictoryCount(characterId)
    }

    /**
     * Obtém contagem de derrotas
     */
    suspend fun getDefeatCount(characterId: Long): Int {
        return battleDao.getDefeatCount(characterId)
    }
}
