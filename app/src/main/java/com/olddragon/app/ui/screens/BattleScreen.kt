package com.olddragon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.olddragon.app.models.battle.BattleStatus
import com.olddragon.app.service.BattleState
import com.olddragon.app.ui.theme.DarkParchment
import com.olddragon.app.ui.theme.DragonGold
import com.olddragon.app.ui.theme.DragonRed
import com.olddragon.app.ui.theme.InkBlack
import com.olddragon.app.viewmodel.BattleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleScreen(
    onNavigateBack: () -> Unit,
    viewModel: BattleViewModel = viewModel()
) {
    val battleState by viewModel.battleState.collectAsState()
    val currentCharacter by viewModel.currentCharacter.collectAsState()

    // Pede permissão de notificação ao entrar
    var permissionGranted by remember { mutableStateOf(false) }
    RequestNotificationPermission(
        onPermissionGranted = { permissionGranted = true },
        onPermissionDenied = { permissionGranted = true } // Continua mesmo sem permissão
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Simulador de Batalha") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkParchment,
                    titleContentColor = DragonGold
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkParchment)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = battleState) {
                is BattleState.Idle -> {
                    IdleScreen(
                        character = currentCharacter,
                        onStartBattle = {
                            currentCharacter?.let { char ->
                                viewModel.startBattle(char.id)
                            }
                        }
                    )
                }
                is BattleState.Loading -> {
                    LoadingScreen()
                }
                is BattleState.InProgress -> {
                    BattleInProgressScreen(
                        state = state,
                        onStopBattle = { viewModel.stopBattle() }
                    )
                }
                is BattleState.Completed -> {
                    BattleCompletedScreen(
                        state = state,
                        onNavigateBack = onNavigateBack
                    )
                }
                is BattleState.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onNavigateBack = onNavigateBack
                    )
                }
            }
        }
    }
}

@Composable
fun IdleScreen(
    character: com.olddragon.app.models.character.Character?,
    onStartBattle: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (character != null && character.id > 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkParchment),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = DragonGold,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nível ${character.level}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = InkBlack
                    )
                    Text(
                        text = "HP: ${character.hp}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = InkBlack
                    )
                    Text(
                        text = "XP: ${character.xp}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = InkBlack
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onStartBattle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DragonGold)
            ) {
                Text(
                    text = "Iniciar Batalha",
                    style = MaterialTheme.typography.titleMedium,
                    color = InkBlack,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkParchment)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "⚠️",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhum personagem ativo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DragonRed,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Volte ao menu e selecione um personagem antes de iniciar uma batalha.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkBlack,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = DragonGold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Preparando batalha...",
            style = MaterialTheme.typography.bodyLarge,
            color = InkBlack
        )
    }
}

@Composable
fun BattleInProgressScreen(
    state: BattleState.InProgress,
    onStopBattle: () -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.battle.battleLog.size) {
        if (state.battle.battleLog.isNotEmpty()) {
            listState.animateScrollToItem(state.battle.battleLog.size - 1)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header com info da batalha
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkParchment),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Rodada ${state.round}",
                    style = MaterialTheme.typography.titleLarge,
                    color = DragonGold,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Info jogador
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = state.battle.characterName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = InkBlack,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "HP: ${state.playerHp}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (state.playerHp > 10) InkBlack else DragonRed
                        )
                    }

                    Text(
                        text = "VS",
                        style = MaterialTheme.typography.titleMedium,
                        color = DragonGold,
                        fontWeight = FontWeight.Bold
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = state.battle.enemyName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = InkBlack,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "HP: ${state.enemyHp}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (state.enemyHp > 10) InkBlack else DragonRed
                        )
                    }
                }
            }
        }

        // Log de batalha
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkParchment),
            shape = RoundedCornerShape(8.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(state.battle.battleLog) { logEntry ->
                    Text(
                        text = logEntry,
                        style = MaterialTheme.typography.bodySmall,
                        color = InkBlack,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão parar
        Button(
            onClick = onStopBattle,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DragonRed)
        ) {
            Text(
                text = "Parar Batalha",
                color = InkBlack,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BattleCompletedScreen(
    state: BattleState.Completed,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val isVictory = state.battle.status == BattleStatus.VICTORY

        Text(
            text = if (isVictory) "VITÓRIA!" else "DERROTA!",
            style = MaterialTheme.typography.displayMedium,
            color = if (isVictory) DragonGold else DragonRed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkParchment),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Batalha finalizada em ${state.battle.totalRounds} rodadas",
                    style = MaterialTheme.typography.bodyLarge,
                    color = InkBlack,
                    textAlign = TextAlign.Center
                )

                if (isVictory && state.battle.xpGained > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "XP Ganho: ${state.battle.xpGained}",
                        style = MaterialTheme.typography.titleMedium,
                        color = DragonGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DragonGold)
        ) {
            Text(
                text = "Voltar",
                color = InkBlack,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Erro",
            style = MaterialTheme.typography.displaySmall,
            color = DragonRed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = InkBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateBack,
            colors = ButtonDefaults.buttonColors(containerColor = DragonGold)
        ) {
            Text(text = "Voltar", color = InkBlack)
        }
    }
}
