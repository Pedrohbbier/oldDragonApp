package com.olddragon.app.data.database.dao

import androidx.room.*
import com.olddragon.app.data.database.entities.CharacterEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para acessar dados de personagens
 */
@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY updatedAt DESC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :characterId")
    fun getCharacterById(characterId: Long): Flow<CharacterEntity?>

    @Query("SELECT * FROM characters WHERE isActive = 1 LIMIT 1")
    fun getActiveCharacter(): Flow<CharacterEntity?>

    @Query("SELECT * FROM characters WHERE name = :name LIMIT 1")
    suspend fun getCharacterByName(name: String): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity): Long

    @Update
    suspend fun updateCharacter(character: CharacterEntity)

    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Long)

    @Query("UPDATE characters SET isActive = 0")
    suspend fun deactivateAllCharacters()

    @Query("UPDATE characters SET isActive = 1 WHERE id = :characterId")
    suspend fun setActiveCharacter(characterId: Long)

    @Query("UPDATE characters SET currentHp = :hp, updatedAt = :timestamp WHERE id = :characterId")
    suspend fun updateCharacterHp(characterId: Long, hp: Byte, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE characters SET xp = :xp, updatedAt = :timestamp WHERE id = :characterId")
    suspend fun updateCharacterXp(characterId: Long, xp: Int, timestamp: Long = System.currentTimeMillis())
}
