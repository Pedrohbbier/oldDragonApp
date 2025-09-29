package com.olddragon.app.models.character

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class CharacterStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("characters", Context.MODE_PRIVATE)
    private val gson = GsonBuilder()
        .enableComplexMapKeySerialization()
        .setPrettyPrinting()
        .create()

    fun saveCharacter(character: Character) {
        try {
            Log.d("CharacterStorage", "Salvando personagem: ${character.name}")

            // Cria um objeto simples para salvar
            val simpleCharacter = SimpleCharacter(
                name = character.name,
                level = character.level,
                hp = character.hp,
                xp = character.xp,
                raceName = character.race?.raceName,
                className = character.characterClass?.className,
                alignmentName = character.alignment?.name,
                skills = character.skills,
                skillMod = character.skillMod,
                atributes = character.atributes
            )

            val characters = getAllSimpleCharacters().toMutableList()

            // Remove personagem existente com mesmo nome
            characters.removeAll { it.name == character.name }

            // Adiciona o novo personagem no início
            characters.add(0, simpleCharacter)

            // Mantém apenas os 10 personagens mais recentes
            if (characters.size > 10) {
                characters.subList(10, characters.size).clear()
            }

            val charactersJson = gson.toJson(characters)
            val success = sharedPreferences.edit()
                .putString("saved_characters", charactersJson)
                .commit() // Usar commit() em vez de apply() para garantir que salve

            Log.d("CharacterStorage", "Personagem salvo com sucesso: $success")
            Log.d("CharacterStorage", "Total de personagens: ${characters.size}")

        } catch (e: Exception) {
            Log.e("CharacterStorage", "Erro ao salvar personagem", e)
        }
    }

    private fun getAllSimpleCharacters(): List<SimpleCharacter> {
        val charactersJson = sharedPreferences.getString("saved_characters", null)
        return if (charactersJson != null) {
            try {
                val type = object : TypeToken<List<SimpleCharacter>>() {}.type
                gson.fromJson(charactersJson, type) ?: emptyList()
            } catch (e: Exception) {
                Log.e("CharacterStorage", "Erro ao deserializar personagens", e)
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun getAllCharacters(): List<Character> {
        return getAllSimpleCharacters().map { simple ->
            val character = Character()
            character.name = simple.name
            character.level = simple.level
            character.hp = simple.hp
            character.xp = simple.xp
            character.skills = simple.skills
            character.skillMod = simple.skillMod
            character.atributes = simple.atributes

            // Reconstituir raça
            simple.raceName?.let { raceName ->
                character.race = Races.listAll().find { it.raceName == raceName }
            }

            // Reconstituir classe
            simple.className?.let { className ->
                character.characterClass = com.olddragon.app.models.charClass.CharacterClasses.listAll().find { it.className == className }
            }

            // Reconstituir alinhamento
            simple.alignmentName?.let { alignmentName ->
                character.alignment = Alignment.values().find { it.name == alignmentName }
            }

            character
        }
    }

    fun getLastCharacter(): Character? {
        return getAllCharacters().firstOrNull()
    }

    fun deleteCharacter(characterName: String) {
        val characters = getAllSimpleCharacters().toMutableList()
        characters.removeAll { it.name == characterName }

        val charactersJson = gson.toJson(characters)
        sharedPreferences.edit()
            .putString("saved_characters", charactersJson)
            .commit()
    }
}