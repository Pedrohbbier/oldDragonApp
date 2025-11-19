package com.olddragon.app.data.database.dao

import androidx.room.*
import com.olddragon.app.data.database.entities.BattleEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para acessar dados de batalhas
 */
@Dao
interface BattleDao {

    @Query("SELECT * FROM battles ORDER BY startedAt DESC")
    fun getAllBattles(): Flow<List<BattleEntity>>

    @Query("SELECT * FROM battles WHERE id = :battleId")
    fun getBattleById(battleId: Long): Flow<BattleEntity?>

    @Query("SELECT * FROM battles WHERE characterId = :characterId ORDER BY startedAt DESC")
    fun getBattlesByCharacter(characterId: Long): Flow<List<BattleEntity>>

    @Query("SELECT * FROM battles WHERE status = 'ONGOING' LIMIT 1")
    fun getOngoingBattle(): Flow<BattleEntity?>

    @Query("SELECT * FROM battles WHERE status = 'ONGOING' LIMIT 1")
    suspend fun getOngoingBattleSync(): BattleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBattle(battle: BattleEntity): Long

    @Update
    suspend fun updateBattle(battle: BattleEntity)

    @Delete
    suspend fun deleteBattle(battle: BattleEntity)

    @Query("DELETE FROM battles WHERE id = :battleId")
    suspend fun deleteBattleById(battleId: Long)

    @Query("SELECT COUNT(*) FROM battles WHERE characterId = :characterId AND status = 'VICTORY'")
    suspend fun getVictoryCount(characterId: Long): Int

    @Query("SELECT COUNT(*) FROM battles WHERE characterId = :characterId AND status = 'DEFEAT'")
    suspend fun getDefeatCount(characterId: Long): Int
}
