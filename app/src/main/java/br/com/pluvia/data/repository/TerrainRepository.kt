package br.com.pluvia.data.repository

import br.com.pluvia.data.model.AddTerrainRequest
import br.com.pluvia.data.model.Crop
import br.com.pluvia.data.model.IrrigationStatus
import br.com.pluvia.data.model.LandResponse
import br.com.pluvia.data.model.RiskLevel
import br.com.pluvia.data.model.Terrain
import br.com.pluvia.data.model.UpdateTerrainRequest
import br.com.pluvia.data.remote.RetrofitClient
import br.com.pluvia.util.Resource

class TerrainRepository {

    private val api = RetrofitClient.instance

    suspend fun getCrops(): Resource<List<Crop>> = try {
        val response = api.getCrops()
        if (response.isSuccessful) Resource.Success(response.body().orEmpty())
        else Resource.Error("Erro ao carregar tipos de plantação")
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Erro desconhecido")
    }

    suspend fun getTerrains(): Resource<List<Terrain>> = try {
        val response = api.getTerrains()
        if (response.isSuccessful) Resource.Success(response.body().orEmpty().map(::toTerrain))
        else Resource.Error(errorMessage(response.code(), "Erro ao carregar terrenos"))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Erro desconhecido")
    }

    suspend fun getTerrain(id: Int): Resource<Terrain> = try {
        val response = api.getTerrain(id)
        if (response.isSuccessful && response.body() != null) Resource.Success(toTerrain(response.body()!!))
        else Resource.Error(errorMessage(response.code(), "Terreno não encontrado"))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Erro desconhecido")
    }

    suspend fun addTerrain(request: AddTerrainRequest): Resource<Terrain> = try {
        val response = api.addTerrain(request)
        if (response.isSuccessful && response.body() != null) Resource.Success(toTerrain(response.body()!!))
        else Resource.Error(errorMessage(response.code(), "Erro ao cadastrar terreno"))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Erro desconhecido")
    }

    suspend fun updateTerrain(id: Int, request: UpdateTerrainRequest): Resource<Terrain> = try {
        val response = api.updateTerrain(id, request)
        if (response.isSuccessful && response.body() != null) Resource.Success(toTerrain(response.body()!!))
        else Resource.Error(errorMessage(response.code(), "Erro ao atualizar terreno"))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Erro desconhecido")
    }

    suspend fun deleteTerrain(id: Int): Resource<Unit> = try {
        val response = api.deleteTerrain(id)
        if (response.isSuccessful) Resource.Success(Unit)
        else Resource.Error(errorMessage(response.code(), "Erro ao excluir terreno"))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Erro desconhecido")
    }

    private fun toTerrain(land: LandResponse): Terrain {
        val forecast = land.forecastHistories.lastOrNull()
        val irrigation = land.irrigationHistories.lastOrNull()
        val risk = forecast?.regionFireRiskPercent?.toInt() ?: 0
        val level = when {
            risk >= 66 -> RiskLevel.HIGH
            risk >= 33 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
        val irrigationStatus = if (irrigation?.status?.lowercase() == "ativado") IrrigationStatus.ACTIVE else IrrigationStatus.INACTIVE

        return Terrain(
            id = land.id,
            name = land.name,
            city = land.city,
            state = land.state,
            hectares = land.hectares,
            cropType = land.crop.type,
            latitude = land.latitude,
            longitude = land.longitude,
            temperature = forecast?.airTemperature ?: 0.0,
            humidity = forecast?.airHumidity ?: 0,
            pressure = forecast?.airPressure ?: 0,
            fireRiskPercent = risk,
            fireRiskLevel = level,
            irrigationStatus = irrigationStatus,
            lastIrrigationDate = irrigation?.date
        )
    }

    private fun errorMessage(code: Int, fallback: String): String = when (code) {
        401 -> "Sessão expirada. Faça login novamente"
        404 -> "Terreno não encontrado"
        else -> fallback
    }
}
