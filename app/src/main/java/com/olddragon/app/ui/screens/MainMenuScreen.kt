@file:OptIn(ExperimentalMaterial3Api::class)

package com.olddragon.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.app.R
import com.olddragon.app.ui.theme.*
import com.olddragon.app.viewmodel.MainMenuViewModel

@Composable
fun MainMenuScreen(
    onCreateCharacterClick: () -> Unit,
    onBattleClick: () -> Unit = {},
    onLoadCharacterClick: (String) -> Unit = {},
    viewModel: MainMenuViewModel = viewModel()
) {
    var showCharacterList by remember { mutableStateOf(false) }
    val hasCharacters by viewModel.hasCharacters.collectAsState()
    val savedCharacters by viewModel.savedCharacters.collectAsState()
    val activeCharacter by viewModel.activeCharacter.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DragonBlack,
                        InkBlack,
                        DragonRed.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "OLD DRAGON",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = DragonGold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "RPG Clássico",
                fontSize = 18.sp,
                color = DragonSilver,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Dragon emblem or placeholder
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 48.dp),
                shape = RoundedCornerShape(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DragonGold.copy(alpha = 0.2f)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🐉",
                        fontSize = 60.sp
                    )
                }
            }

            // Menu buttons
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Parchment.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Create Character Button
                    Button(
                        onClick = onCreateCharacterClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DragonGold,
                            contentColor = DragonBlack
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.create_character),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Load Character Button
                    Button(
                        onClick = { showCharacterList = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = hasCharacters,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MedievalBlue,
                            contentColor = Parchment,
                            disabledContainerColor = DragonSilver.copy(alpha = 0.3f),
                            disabledContentColor = InkBlack.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (hasCharacters) "Carregar Personagem" else "Nenhum Personagem Salvo",
                            fontSize = 16.sp
                        )
                    }

                    // Battle Button
                    Button(
                        onClick = onBattleClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = activeCharacter != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DragonRed,
                            contentColor = Parchment,
                            disabledContainerColor = DragonSilver.copy(alpha = 0.3f),
                            disabledContentColor = InkBlack.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (activeCharacter != null) "Iniciar Batalha" else "Selecione um Personagem",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DragonSilver.copy(alpha = 0.3f),
                            contentColor = InkBlack.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Configurações",
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Text(
                text = "Versão 1.0",
                fontSize = 12.sp,
                color = DragonSilver.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }

    // Character List Dialog
    if (showCharacterList) {
        CharacterListDialog(
            characters = savedCharacters,
            onCharacterSelect = { character ->
                viewModel.setActiveCharacter(character)
                showCharacterList = false
                onLoadCharacterClick(character.name)
            },
            onDismiss = { showCharacterList = false }
        )
    }
}

@Composable
fun CharacterListDialog(
    characters: List<com.olddragon.app.models.character.Character>,
    onCharacterSelect: (com.olddragon.app.models.character.Character) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Selecionar Personagem",
                fontWeight = FontWeight.Bold,
                color = InkBlack
            )
        },
        text = {
            LazyColumn {
                items(characters.size) { index ->
                    val character = characters[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onCharacterSelect(character) },
                        colors = CardDefaults.cardColors(
                            containerColor = DarkParchment
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = character.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = InkBlack
                            )
                            Text(
                                text = "${character.race?.raceName ?: "Sem raça"} ${character.characterClass?.className ?: "Sem classe"}",
                                fontSize = 14.sp,
                                color = InkBlack.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "Nível ${character.level}",
                                fontSize = 12.sp,
                                color = InkBlack.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = InkBlack
                )
            ) {
                Text("Cancelar")
            }
        },
        containerColor = Parchment
    )
}