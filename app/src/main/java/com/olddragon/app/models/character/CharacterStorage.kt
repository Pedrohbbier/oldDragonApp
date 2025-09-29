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
            Log.d("CharacterStorage", "Saving character: ${character.name}")

            // Create a simple object to save
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

            // Remove existing character with same name
            characters.removeAll { it.name == character.name }

            // Add new character at the beginning
            characters.add(0, simpleCharacter)

            // Keep only the 10 most recent characters
            if (characters.size > 10) {
                characters.subList(10, characters.size).clear()
            }

            val charactersJson = gson.toJson(characters)
            val success = sharedPreferences.edit()
                .putString("saved_characters", charactersJson)
                .commit() // Use commit() instead of apply() to ensure save

            Log.d("CharacterStorage", "Character saved successfully: $success")
            Log.d("CharacterStorage", "Total characters: ${characters.size}")

        } catch (e: Exception) {
            Log.e("CharacterStorage", "Error saving character", e)
        }
    }

    private fun getAllSimpleCharacters(): List<SimpleCharacter> {
        val charactersJson = sharedPreferences.getString("saved_characters", null)
        return if (charactersJson != null) {
            try {
                val type = object : TypeToken<List<SimpleCharacter>>() {}.type
                gson.fromJson(charactersJson, type) ?: emptyList()
            } catch (e: Exception) {
                Log.e("CharacterStorage", "Error deserializing characters", e)
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

            // Reconstitute race
            simple.raceName?.let { raceName ->
                character.race = Races.listAll().find { it.raceName == raceName }
            }

            // Reconstitute class
            simple.className?.let { className ->
                character.characterClass = com.olddragon.app.models.charClass.CharacterClasses.listAll().find { it.className == className }
            }

            // Reconstitute alignment
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