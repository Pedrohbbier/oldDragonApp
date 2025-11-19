package com.olddragon.app.data.database.dao

import androidx.room.*
import com.olddragon.app.data.database.entities.EnemyEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para acessar dados de inimigos
 */
@Dao
interface EnemyDao {

    @Query("SELECT * FROM enemies ORDER BY level ASC, name ASC")
    fun getAllEnemies(): Flow<List<EnemyEntity>>

    @Query("SELECT * FROM enemies WHERE id = :enemyId")
    fun getEnemyById(enemyId: Long): Flow<EnemyEntity?>

    @Query("SELECT * FROM enemies WHERE level <= :maxLevel ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomEnemyByLevel(maxLevel: Byte): EnemyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnemy(enemy: EnemyEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnemies(enemies: List<EnemyEntity>)

    @Update
    suspend fun updateEnemy(enemy: EnemyEntity)

    @Delete
    suspend fun deleteEnemy(enemy: EnemyEntity)

    @Query("DELETE FROM enemies WHERE id = :enemyId")
    suspend fun deleteEnemyById(enemyId: Long)

    @Query("SELECT COUNT(*) FROM enemies")
    suspend fun getEnemyCount(): Int
}
