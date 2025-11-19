package com.olddragon.app.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.olddragon.app.data.database.OldDragonDatabase
import com.olddragon.app.data.repository.CharacterRepository
import com.olddragon.app.models.character.*
import com.olddragon.app.models.charClass.CharacterClass
import com.olddragon.app.models.charClass.CharacterClasses
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterCreationViewModel(application: Application) : AndroidViewModel(application) {

    private val database = OldDragonDatabase.getDatabase(application)
    private val characterRepository = CharacterRepository(database.characterDao())

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

    val allCharacters: StateFlow<List<Character>> = characterRepository.getAllCharacters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createCharacter(onComplete: (Long) -> Unit = {}) {
        if (isCharacterComplete) {
            viewModelScope.launch {
                val id = characterRepository.saveCharacter(character)
                // Define como personagem ativo
                characterRepository.setActiveCharacter(id)
                onComplete(id)
            }
        }
    }

    fun loadCharacter(character: Character) {
        this.character = character
        characterName = character.name
        selectedRace = character.race
        selectedClass = character.characterClass
        selectedAlignment = character.alignment
        checkCharacterComplete()
    }

    fun getAvailableRaces(): List<Race> = Races.listAll()

    fun getAvailableClasses(): List<CharacterClass> = CharacterClasses.listAll()

    fun getAvailableAlignments(): List<Alignment> = Alignment.values().toList()

    fun getDistributionTypes(): List<AttributeDistributionType> =
        AttributeDistributionType.values().toList()
}