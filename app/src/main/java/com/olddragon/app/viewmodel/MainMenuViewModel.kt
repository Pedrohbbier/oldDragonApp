package com.olddragon.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.olddragon.app.data.database.OldDragonDatabase
import com.olddragon.app.data.repository.CharacterRepository
import com.olddragon.app.models.character.Character
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainMenuViewModel(application: Application) : AndroidViewModel(application) {

    private val database = OldDragonDatabase.getDatabase(application)
    private val characterRepository = CharacterRepository(database.characterDao())

    val savedCharacters: StateFlow<List<Character>> = characterRepository.getAllCharacters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hasCharacters: StateFlow<Boolean> = savedCharacters.map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val activeCharacter: StateFlow<Character?> = characterRepository.getActiveCharacter()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setActiveCharacter(character: Character) {
        viewModelScope.launch {
            characterRepository.setActiveCharacter(character.id)
        }
    }

    fun deleteCharacter(character: Character) {
        viewModelScope.launch {
            characterRepository.deleteCharacter(character)
        }
    }
}