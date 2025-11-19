package com.olddragon.app.data.repository

import com.olddragon.app.data.database.dao.EnemyDao
import com.olddragon.app.models.battle.Enemies
import com.olddragon.app.models.battle.Enemy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository para gerenciar inimigos
 */
class EnemyRepository(private val enemyDao: EnemyDao) {

    /**
     * Obtém todos os inimigos
     */
    fun getAllEnemies(): Flow<List<Enemy>> {
        return enemyDao.getAllEnemies().map { entities ->
            entities.map { Enemy.fromEntity(it) }
        }
    }

    /**
     * Obtém inimigo por ID
     */
    fun getEnemyById(id: Long): Flow<Enemy?> {
        return enemyDao.getEnemyById(id).map { entity ->
            entity?.let { Enemy.fromEntity(it) }
        }
    }

    /**
     * Obtém inimigo aleatório baseado no nível máximo
     */
    suspend fun getRandomEnemyByLevel(maxLevel: Byte): Enemy? {
        val entity = enemyDao.getRandomEnemyByLevel(maxLevel)
        return entity?.let { Enemy.fromEntity(it) }
    }

    /**
     * Salva um inimigo
     */
    suspend fun saveEnemy(enemy: Enemy): Long {
        return enemyDao.insertEnemy(enemy.toEntity())
    }

    /**
     * Popula o banco com inimigos padrão
     */
    suspend fun populateDefaultEnemies() {
        val count = enemyDao.getEnemyCount()
        if (count == 0) {
            val defaultEnemies = Enemies.getDefaultEnemies()
            enemyDao.insertEnemies(defaultEnemies.map { it.toEntity() })
        }
    }

    /**
     * Deleta um inimigo
     */
    suspend fun deleteEnemy(enemy: Enemy) {
        enemyDao.deleteEnemy(enemy.toEntity())
    }
}
