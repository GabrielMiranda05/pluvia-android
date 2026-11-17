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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.pluvia.data.model.User
import br.com.pluvia.ui.component.AppButton
import br.com.pluvia.ui.component.BottomNavBar
import br.com.pluvia.ui.navigation.NavRoutes
import br.com.pluvia.ui.theme.BlueBackground
import br.com.pluvia.ui.theme.BluePrimary
import br.com.pluvia.ui.theme.BlueSurface
import br.com.pluvia.ui.theme.CyanGradientEnd
import br.com.pluvia.ui.theme.CyanGradientStart
import br.com.pluvia.ui.theme.GrayText
import br.com.pluvia.ui.theme.RedLogout
import br.com.pluvia.ui.theme.WhiteSurface
import br.com.pluvia.ui.viewmodel.ProfileViewModel
import br.com.pluvia.util.Resource

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToTerrain: () -> Unit,
    onNavigateToEditProfile: () -> Unit
) {
    val profileState by viewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        containerColor = BlueBackground,
        topBar = { ProfileTopBar() },
        bottomBar = {
            BottomNavBar(
                currentRoute = NavRoutes.PROFILE,
                onHomeClick = onNavigateToHome,
                onTerrainClick = onNavigateToTerrain,
                onProfileClick = {}
            )
        }
    ) { paddingValues ->
        when (val state = profileState) {
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
                ProfileContent(
                    user = state.data,
                    onLogout = onLogout,
                    onEditProfile = {
                        viewModel.prepareEdit(state.data)
                        onNavigateToEditProfile()
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is Resource.Error -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(text = state.message, color = GrayText)
                }
            }
        }
    }
}

@Composable
private fun ProfileTopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(BlueBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = "Meu perfil",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BluePrimary
        )
    }
}

@Composable
private fun ProfileContent(
    user: User,
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(CyanGradientStart, CyanGradientEnd)
                    )
                )
        ) {
            Text(
                text = user.initials,
                style = MaterialTheme.typography.headlineMedium,
                color = WhiteSurface,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = GrayText
        )

        Spacer(modifier = Modifier.height(32.dp))

        ProfileInfoCard(icon = Icons.Outlined.Person, label = "Nome", value = user.name)

        Spacer(modifier = Modifier.height(12.dp))

        ProfileInfoCard(icon = Icons.Outlined.Email, label = "E-mail", value = user.email)

        Spacer(modifier = Modifier.height(20.dp))

        // Botão editar perfil
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, BluePrimary.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .background(BlueSurface)
                .clickable(onClick = onEditProfile)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = BluePrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Editar perfil",
                style = MaterialTheme.typography.labelLarge,
                color = BluePrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Sair da conta",
            onClick = onLogout,
            containerColor = RedLogout
        )
    }
}

@Composable
private fun ProfileInfoCard(
    icon: ImageVector,
    label: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(BlueSurface)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
