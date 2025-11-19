package com.olddragon.app.data.repository

import com.olddragon.app.data.database.dao.CharacterDao
import com.olddragon.app.data.database.entities.CharacterEntity
import com.olddragon.app.models.character.Character
import com.olddragon.app.models.charClass.CharacterClasses
import com.olddragon.app.models.character.Alignment
import com.olddragon.app.models.character.Races
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository para gerenciar personagens
 * Camada de abstração entre ViewModel e dados (Room Database)
 */
class CharacterRepository(private val characterDao: CharacterDao) {

    /**
     * Obtém todos os personagens como Flow
     */
    fun getAllCharacters(): Flow<List<Character>> {
        return characterDao.getAllCharacters().map { entities ->
            entities.map { entityToCharacter(it) }
        }
    }

    /**
     * Obtém personagem por ID
     */
    fun getCharacterById(id: Long): Flow<Character?> {
        return characterDao.getCharacterById(id).map { entity ->
            entity?.let { entityToCharacter(it) }
        }
    }

    /**
     * Obtém personagem ativo
     */
    fun getActiveCharacter(): Flow<Character?> {
        return characterDao.getActiveCharacter().map { entity ->
            entity?.let { entityToCharacter(it) }
        }
    }

    /**
     * Salva ou atualiza um personagem
     */
    suspend fun saveCharacter(character: Character): Long {
        val entity = characterToEntity(character)
        return characterDao.insertCharacter(entity)
    }

    /**
     * Atualiza um personagem existente
     */
    suspend fun updateCharacter(character: Character) {
        val entity = characterToEntity(character)
        characterDao.updateCharacter(entity)
    }

    /**
     * Define um personagem como ativo
     */
    suspend fun setActiveCharacter(characterId: Long) {
        characterDao.deactivateAllCharacters()
        characterDao.setActiveCharacter(characterId)
    }

    /**
     * Atualiza HP do personagem
     */
    suspend fun updateCharacterHp(characterId: Long, hp: Byte) {
        characterDao.updateCharacterHp(characterId, hp)
    }

    /**
     * Atualiza XP do personagem
     */
    suspend fun updateCharacterXp(characterId: Long, xp: Int) {
        characterDao.updateCharacterXp(characterId, xp)
    }

    /**
     * Deleta um personagem
     */
    suspend fun deleteCharacter(character: Character) {
        val entity = characterToEntity(character)
        characterDao.deleteCharacter(entity)
    }

    /**
     * Deleta personagem por ID
     */
    suspend fun deleteCharacterById(characterId: Long) {
        characterDao.deleteCharacterById(characterId)
    }

    /**
     * Converte Character para CharacterEntity
     */
    private fun characterToEntity(character: Character): CharacterEntity {
        return CharacterEntity(
            id = character.id, // Usa o ID do Character, se for 0 o Room auto-gera
            name = character.name,
            level = character.level,
            currentHp = character.hp,
            maxHp = character.hp,
            xp = character.xp,
            raceName = character.race?.raceName,
            className = character.characterClass?.className,
            alignmentName = character.alignment?.name,
            skills = character.skills,
            skillMod = character.skillMod,
            attributes = character.atributes
        )
    }

    /**
     * Converte CharacterEntity para Character
     */
    private fun entityToCharacter(entity: CharacterEntity): Character {
        val character = Character()
        character.id = entity.id
        character.name = entity.name
        character.level = entity.level
        character.hp = entity.currentHp
        character.xp = entity.xp
        character.skills = entity.skills
        character.skillMod = entity.skillMod
        character.atributes = entity.attributes

        // Reconstrói raça
        entity.raceName?.let { raceName ->
            character.race = Races.listAll().find { it.raceName == raceName }
        }

        // Reconstrói classe
        entity.className?.let { className ->
            character.characterClass = CharacterClasses.listAll().find { it.className == className }
        }

        // Reconstrói alinhamento
        entity.alignmentName?.let { alignmentName ->
            character.alignment = Alignment.values().find { it.name == alignmentName }
        }

        return character
    }
}
