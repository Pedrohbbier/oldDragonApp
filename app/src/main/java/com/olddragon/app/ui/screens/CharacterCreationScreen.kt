@file:OptIn(ExperimentalMaterial3Api::class)

package com.olddragon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.olddragon.app.models.character.Alignment as CharacterAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.app.R
import com.olddragon.app.models.character.*
import com.olddragon.app.models.charClass.CharacterClass
import com.olddragon.app.ui.theme.*
import com.olddragon.app.viewmodel.CharacterCreationViewModel

@Composable
fun CharacterCreationScreen(
    onBackClick: () -> Unit,
    viewModel: CharacterCreationViewModel = viewModel()
) {
    var showCharacterSummary by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Parchment,
                        DarkParchment,
                        Parchment.copy(alpha = 0.8f)
                    )
                )
            )
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.create_character),
                    color = InkBlack,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = InkBlack
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DragonGold,
                titleContentColor = InkBlack
            )
        )

        if (showCharacterSummary) {
            CharacterSummaryDialog(
                character = viewModel.character,
                onDismiss = { showCharacterSummary = false },
                onConfirm = {
                    // Salva o personagem
                    viewModel.createCharacter()
                    showCharacterSummary = false
                    onBackClick()
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Scrollable content
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Character Name
                item {
                    CharacterNameSection(
                        name = viewModel.characterName,
                        onNameChange = viewModel::updateCharacterName
                    )
                }

                // Attribute Distribution Type
                item {
                    AttributeDistributionSection(
                        selectedType = viewModel.selectedDistributionType,
                        onTypeSelect = viewModel::selectDistributionType,
                        onRollAttributes = viewModel::rollNewAttributes,
                        availableTypes = viewModel.getDistributionTypes()
                    )
                }

                // Attributes Display
                item {
                    AttributesDisplaySection(character = viewModel.character)
                }

                // Race Selection
                item {
                    RaceSelectionSection(
                        selectedRace = viewModel.selectedRace,
                        onRaceSelect = viewModel::selectRace,
                        availableRaces = viewModel.getAvailableRaces()
                    )
                }

                // Class Selection
                item {
                    ClassSelectionSection(
                        selectedClass = viewModel.selectedClass,
                        onClassSelect = viewModel::selectClass,
                        availableClasses = viewModel.getAvailableClasses()
                    )
                }

                // Alignment Selection
                item {
                    AlignmentSelectionSection(
                        selectedAlignment = viewModel.selectedAlignment,
                        onAlignmentSelect = viewModel::selectAlignment,
                        availableAlignments = viewModel.getAvailableAlignments()
                    )
                }
            }

            // Fixed Create Button at bottom
            Button(
                onClick = { showCharacterSummary = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
                enabled = viewModel.isCharacterComplete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DragonGold,
                    contentColor = InkBlack,
                    disabledContainerColor = DragonSilver.copy(alpha = 0.3f),
                    disabledContentColor = InkBlack.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.create),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkBlack
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CharacterNameSection(
    name: String,
    onNameChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkParchment
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.character_name),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Digite o nome do personagem",
                        color = InkBlack.copy(alpha = 0.6f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DragonGold,
                    unfocusedBorderColor = InkBlack.copy(alpha = 0.5f),
                    focusedTextColor = InkBlack,
                    unfocusedTextColor = InkBlack,
                    cursorColor = InkBlack
                )
            )
        }
    }
}

@Composable
fun AttributeDistributionSection(
    selectedType: AttributeDistributionType,
    onTypeSelect: (AttributeDistributionType) -> Unit,
    onRollAttributes: () -> Unit,
    availableTypes: List<AttributeDistributionType>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkParchment
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Distribuição de Atributos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkBlack
                )

                IconButton(
                    onClick = onRollAttributes,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.roll_attributes),
                        tint = DragonGold
                    )
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(availableTypes) { type ->
                    FilterChip(
                        onClick = { onTypeSelect(type) },
                        label = { Text(type.displayName, fontSize = 12.sp) },
                        selected = type == selectedType,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DragonGold,
                            selectedLabelColor = InkBlack
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AttributesDisplaySection(character: Character) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkParchment
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.attributes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    AttributeItem("FOR", character.skills["str"] ?: 0, character.skillMod["str"] ?: 0)
                    AttributeItem("DES", character.skills["dex"] ?: 0, character.skillMod["dex"] ?: 0)
                    AttributeItem("CON", character.skills["con"] ?: 0, character.skillMod["con"] ?: 0)
                }
                Column {
                    AttributeItem("INT", character.skills["int"] ?: 0, character.skillMod["int"] ?: 0)
                    AttributeItem("SAB", character.skills["wis"] ?: 0, character.skillMod["wis"] ?: 0)
                    AttributeItem("CAR", character.skills["cha"] ?: 0, character.skillMod["cha"] ?: 0)
                }
            }
        }
    }
}

@Composable
fun AttributeItem(label: String, value: Byte, modifier: Byte) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = InkBlack.copy(alpha = 0.7f)
        )
        Text(
            text = value.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = InkBlack
        )
        Text(
            text = if (modifier >= 0) "+$modifier" else "$modifier",
            fontSize = 12.sp,
            color = if (modifier >= 0) DragonGold else DragonRed
        )
    }
}

@Composable
fun RaceSelectionSection(
    selectedRace: Race?,
    onRaceSelect: (Race) -> Unit,
    availableRaces: List<Race>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkParchment
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_race),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableRaces) { race ->
                    FilterChip(
                        onClick = { onRaceSelect(race) },
                        label = { Text(race.raceName) },
                        selected = race == selectedRace,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DragonGold,
                            selectedLabelColor = InkBlack
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ClassSelectionSection(
    selectedClass: CharacterClass?,
    onClassSelect: (CharacterClass) -> Unit,
    availableClasses: List<CharacterClass>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkParchment
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_class),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableClasses) { characterClass ->
                    FilterChip(
                        onClick = { onClassSelect(characterClass) },
                        label = { Text(characterClass.className) },
                        selected = characterClass == selectedClass,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DragonGold,
                            selectedLabelColor = InkBlack
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AlignmentSelectionSection(
    selectedAlignment: CharacterAlignment?,
    onAlignmentSelect: (CharacterAlignment) -> Unit,
    availableAlignments: List<CharacterAlignment>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkParchment
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.select_alignment),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = InkBlack,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Primeira linha: 3 alinhamentos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    availableAlignments.take(3).forEach { alignment ->
                        FilterChip(
                            onClick = { onAlignmentSelect(alignment) },
                            label = {
                                Text(
                                    text = alignment.displayName,
                                    fontSize = 10.sp,
                                    color = if (alignment == selectedAlignment) InkBlack else InkBlack.copy(alpha = 0.8f)
                                )
                            },
                            selected = alignment == selectedAlignment,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DragonGold,
                                selectedLabelColor = InkBlack,
                                containerColor = DarkParchment,
                                labelColor = InkBlack
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Segunda linha: 3 alinhamentos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    availableAlignments.drop(3).take(3).forEach { alignment ->
                        FilterChip(
                            onClick = { onAlignmentSelect(alignment) },
                            label = {
                                Text(
                                    text = alignment.displayName,
                                    fontSize = 10.sp,
                                    color = if (alignment == selectedAlignment) InkBlack else InkBlack.copy(alpha = 0.8f)
                                )
                            },
                            selected = alignment == selectedAlignment,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DragonGold,
                                selectedLabelColor = InkBlack,
                                containerColor = DarkParchment,
                                labelColor = InkBlack
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Terceira linha: 3 alinhamentos restantes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    availableAlignments.drop(6).forEach { alignment ->
                        FilterChip(
                            onClick = { onAlignmentSelect(alignment) },
                            label = {
                                Text(
                                    text = alignment.displayName,
                                    fontSize = 10.sp,
                                    color = if (alignment == selectedAlignment) InkBlack else InkBlack.copy(alpha = 0.8f)
                                )
                            },
                            selected = alignment == selectedAlignment,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DragonGold,
                                selectedLabelColor = InkBlack,
                                containerColor = DarkParchment,
                                labelColor = InkBlack
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterSummaryDialog(
    character: Character,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Personagem Criado!",
                fontWeight = FontWeight.Bold,
                color = InkBlack
            )
        },
        text = {
            LazyColumn {
                item {
                    Text(
                        text = character.getCharacterSummary(),
                        color = InkBlack,
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Criar e salvar o personagem
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DragonGold,
                    contentColor = InkBlack
                )
            ) {
                Text("Finalizar", color = InkBlack)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = InkBlack
                )
            ) {
                Text("Editar", color = InkBlack)
            }
        },
        containerColor = Parchment
    )
}