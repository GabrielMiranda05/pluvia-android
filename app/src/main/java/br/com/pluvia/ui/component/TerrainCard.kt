package br.com.pluvia.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Agriculture
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.pluvia.data.model.IrrigationStatus
import br.com.pluvia.data.model.RiskLevel
import br.com.pluvia.data.model.Terrain
import br.com.pluvia.ui.theme.BlueAccent
import br.com.pluvia.ui.theme.BluePrimary
import br.com.pluvia.ui.theme.BlueSurface
import br.com.pluvia.ui.theme.GrayText
import br.com.pluvia.ui.theme.OrangeRisk
import br.com.pluvia.ui.theme.OrangeRiskLight
import br.com.pluvia.ui.theme.WhiteSurface

@Composable
fun TerrainCard(terrain: Terrain, onEditClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WhiteSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Cabeçalho — nome + badge risco + botão editar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = terrain.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = GrayText,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${terrain.city} / ${terrain.state}",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrayText
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                RiskBadge(riskLevel = terrain.fireRiskLevel)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(BlueSurface)
                        .clickable(onClick = onEditClick)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Editar terreno",
                        tint = BluePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Agriculture,
                    contentDescription = null,
                    tint = GrayText,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${terrain.cropType} · ${terrain.hectares.toInt()} ha",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Métricas climáticas
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                WeatherMetricChip(
                    icon = Icons.Outlined.Thermostat,
                    label = "TEMP.",
                    value = "${terrain.temperature.toInt()}°C",
                    modifier = Modifier.weight(1f)
                )
                WeatherMetricChip(
                    icon = Icons.Outlined.Opacity,
                    label = "UMIDADE",
                    value = "${terrain.humidity}%",
                    modifier = Modifier.weight(1f)
                )
                WeatherMetricChip(
                    icon = Icons.Outlined.Speed,
                    label = "PRESSÃO",
                    value = "${terrain.pressure} hPa",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = BlueSurface)
            Spacer(modifier = Modifier.height(12.dp))

            // Risco de incêndio
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocalFireDepartment,
                        contentDescription = null,
                        tint = GrayText,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Risco de incêndio",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayText
                    )
                }
                Text(
                    text = "${terrain.fireRiskPercent}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = riskColor(terrain.fireRiskLevel)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { terrain.fireRiskPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(50.dp)),
                color = riskColor(terrain.fireRiskLevel),
                trackColor = BlueSurface,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status de irrigação
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val dotColor = if (terrain.irrigationStatus == IrrigationStatus.ACTIVE)
                        BlueAccent else OrangeRisk
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val irrigationText = when {
                        terrain.irrigationStatus == IrrigationStatus.ACTIVE -> "Irrigação ativa"
                        terrain.lastIrrigationDate != null ->
                            "Última irrigação: ${terrain.lastIrrigationDate}"
                        else -> "Irrigação inativa"
                    }
                    Text(
                        text = irrigationText,
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayText
                    )
                }
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = GrayText,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun riskColor(riskLevel: RiskLevel): Color = when (riskLevel) {
    RiskLevel.LOW    -> BlueAccent
    RiskLevel.MEDIUM -> OrangeRisk
    RiskLevel.HIGH   -> Color(0xFFE53935)
}

@Composable
private fun RiskBadge(riskLevel: RiskLevel) {
    val (text, textColor, bgColor) = when (riskLevel) {
        RiskLevel.LOW    -> Triple("Baixo", BluePrimary, BlueSurface)
        RiskLevel.MEDIUM -> Triple("Médio", OrangeRisk, OrangeRiskLight)
        RiskLevel.HIGH   -> Triple("Alto", Color(0xFFE53935), Color(0xFFFFEBEE))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun WeatherMetricChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BlueSurface)
            .padding(vertical = 10.dp, horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BluePrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = GrayText,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = BluePrimary
        )
    }
}
