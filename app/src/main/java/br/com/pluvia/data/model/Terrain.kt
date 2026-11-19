package br.com.pluvia.data.model

import com.google.gson.annotations.SerializedName

data class Terrain(
    val id: Int,
    val name: String,
    val city: String,
    val state: String,
    val neighborhood: String = "",
    val street: String = "",
    val number: String = "",
    val hectares: Double,
    val cropType: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val humidity: Int,
    val pressure: Int,
    val fireRiskPercent: Int,
    val fireRiskLevel: RiskLevel,
    val irrigationStatus: IrrigationStatus,
    val lastIrrigationDate: String?
)

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}

enum class IrrigationStatus {
    ACTIVE, INACTIVE
}

data class AddTerrainRequest(
    @SerializedName("crop_id") val cropId: Int,
    val name: String,
    val hectares: Double,
    val state: String,
    val city: String,
    val neighborhood: String?,
    val street: String?,
    val number: String?,
    val latitude: Double,
    val longitude: Double
)

data class UpdateTerrainRequest(
    val name: String? = null,
    val hectares: Double? = null
)

data class Crop(
    val id: Int,
    val type: String
)

data class ForecastHistory(
    val id: Int,
    val date: String,
    val airTemperature: Double,
    val airPressure: Int,
    val airHumidity: Int,
    val regionFireRiskPercent: Double
)

data class IrrigationHistory(
    val id: Int,
    val date: String,
    val status: String
)

data class LandResponse(
    val id: Int,
    val name: String,
    val hectares: Double,
    val city: String,
    val state: String,
    val crop: Crop,
    val latitude: Double,
    val longitude: Double,
    val forecastHistories: List<ForecastHistory> = emptyList(),
    val irrigationHistories: List<IrrigationHistory> = emptyList()
)
