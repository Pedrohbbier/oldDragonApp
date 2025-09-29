package com.olddragon.app.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.olddragon.app.models.character.Character
import com.olddragon.app.models.character.CharacterStorage

class MainMenuViewModel(application: Application) : AndroidViewModel(application) {

    private val characterStorage = CharacterStorage(application)

    var savedCharacters by mutableStateOf<List<Character>>(emptyList())
        private set

    var hasCharacters by mutableStateOf(false)
        private set

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        savedCharacters = characterStorage.getAllCharacters()
        hasCharacters = savedCharacters.isNotEmpty()
    }

    fun loadLastCharacter(): Character? {
        return characterStorage.getLastCharacter()
    }
}