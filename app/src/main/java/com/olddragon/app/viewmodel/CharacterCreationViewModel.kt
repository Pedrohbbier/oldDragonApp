package com.olddragon.app.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.olddragon.app.models.character.*
import com.olddragon.app.models.charClass.CharacterClass
import com.olddragon.app.models.charClass.CharacterClasses

class CharacterCreationViewModel(application: Application) : AndroidViewModel(application) {

    private val characterStorage = CharacterStorage(application)

    var character by mutableStateOf(Character())
        private set

    var characterName by mutableStateOf("")
        private set

    var selectedRace by mutableStateOf<Race?>(null)
        private set

    var selectedClass by mutableStateOf<CharacterClass?>(null)
        private set

    var selectedAlignment by mutableStateOf<Alignment?>(null)
        private set

    var selectedDistributionType by mutableStateOf(AttributeDistributionType.CLASSIC)
        private set

    var isCharacterComplete by mutableStateOf(false)
        private set

    fun updateCharacterName(name: String) {
        characterName = name
        character.name = name
        checkCharacterComplete()
    }

    fun selectRace(race: Race) {
        selectedRace = race
        character.assignRace(race)
        if (selectedAlignment == null) {
            selectedAlignment = race.preferredAlignment
            character.assignAlignment(race.preferredAlignment)
        }
        checkCharacterComplete()
    }

    fun selectClass(characterClass: CharacterClass) {
        selectedClass = characterClass
        character.assignCharacterClass(characterClass)
        checkCharacterComplete()
    }

    fun selectAlignment(alignment: Alignment) {
        selectedAlignment = alignment
        character.assignAlignment(alignment)
        checkCharacterComplete()
    }

    fun selectDistributionType(type: AttributeDistributionType) {
        selectedDistributionType = type
        character = Character(type)
        character.name = characterName
        selectedRace?.let { character.assignRace(it) }
        selectedClass?.let { character.assignCharacterClass(it) }
        selectedAlignment?.let { character.assignAlignment(it) }
        checkCharacterComplete()
    }

    fun rollNewAttributes() {
        val newCharacter = Character(selectedDistributionType)
        newCharacter.name = characterName
        selectedRace?.let { newCharacter.assignRace(it) }
        selectedClass?.let { newCharacter.assignCharacterClass(it) }
        selectedAlignment?.let { newCharacter.assignAlignment(it) }
        character = newCharacter
    }

    private fun checkCharacterComplete() {
        isCharacterComplete = characterName.isNotBlank() &&
                            selectedRace != null &&
                            selectedClass != null &&
                            selectedAlignment != null
    }

    fun createCharacter(): Character {
        if (isCharacterComplete) {
            characterStorage.saveCharacter(character)
        }
        return character
    }

    fun loadLastCharacter() {
        characterStorage.getLastCharacter()?.let { savedCharacter ->
            character = savedCharacter
            characterName = savedCharacter.name
            selectedRace = savedCharacter.race
            selectedClass = savedCharacter.characterClass
            selectedAlignment = savedCharacter.alignment
            checkCharacterComplete()
        }
    }

    fun getAllSavedCharacters(): List<Character> {
        return characterStorage.getAllCharacters()
    }

    fun getAvailableRaces(): List<Race> = Races.listAll()

    fun getAvailableClasses(): List<CharacterClass> = CharacterClasses.listAll()

    fun getAvailableAlignments(): List<Alignment> = Alignment.values().toList()

    fun getDistributionTypes(): List<AttributeDistributionType> =
        AttributeDistributionType.values().toList()
}