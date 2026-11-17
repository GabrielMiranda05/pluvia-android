package br.com.pluvia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.pluvia.ui.component.BottomNavBar
import br.com.pluvia.ui.component.TerrainCard
import br.com.pluvia.ui.navigation.NavRoutes
import br.com.pluvia.ui.theme.BlueBackground
import br.com.pluvia.ui.theme.BluePrimary
import br.com.pluvia.ui.theme.BlueSurface
import br.com.pluvia.ui.theme.CyanGradientEnd
import br.com.pluvia.ui.theme.CyanGradientStart
import br.com.pluvia.ui.theme.GrayBorder
import br.com.pluvia.ui.theme.GrayText
import br.com.pluvia.ui.theme.WhiteSurface
import br.com.pluvia.ui.viewmodel.TerrainViewModel
import br.com.pluvia.util.Resource

@Composable
fun HomeScreen(
    viewModel: TerrainViewModel,
    onNavigateToAddTerrain: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToEditTerrain: (Int) -> Unit
) {
    val terrainsState by viewModel.terrainsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTerrains()
    }

    Scaffold(
        containerColor = BlueBackground,
        topBar = { HomeTopBar() },
        bottomBar = {
            BottomNavBar(
                currentRoute = NavRoutes.HOME,
                onHomeClick = {},
                onTerrainClick = onNavigateToAddTerrain,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        when (val state = terrainsState) {
            is Resource.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator(color = BluePrimary)
                }
            }
            is Resource.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    items(state.data) { terrain ->
                        TerrainCard(
                            terrain = terrain,
                            onEditClick = { onNavigateToEditTerrain(terrain.id) }
                        )
                    }
                    item {
                        AddTerrainButton(onClick = onNavigateToAddTerrain)
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
            is Resource.Error -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(
                        text = state.message,
                        color = GrayText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(BlueBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(CyanGradientStart, CyanGradientEnd)
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Outlined.WaterDrop,
                contentDescription = null,
                tint = WhiteSurface,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = "Pluvia",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BluePrimary
        )
    }
}

@Composable
private fun AddTerrainButton(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = GrayBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .background(BlueSurface)
            .clickable(onClick = onClick)
            .padding(vertical = 18.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = null,
            tint = BluePrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = "Cadastrar novo terreno",
            style = MaterialTheme.typography.bodyMedium,
            color = BluePrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}
