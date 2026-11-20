package br.com.pluvia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pluvia.data.model.AddTerrainRequest
import br.com.pluvia.data.model.Crop
import br.com.pluvia.data.model.Terrain
import br.com.pluvia.data.model.UpdateTerrainRequest
import br.com.pluvia.data.repository.TerrainRepository
import br.com.pluvia.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TerrainViewModel : ViewModel() {

    private val repository = TerrainRepository()

    private val _terrainsState = MutableStateFlow<Resource<List<Terrain>>>(Resource.Loading)
    val terrainsState: StateFlow<Resource<List<Terrain>>> = _terrainsState

    private val _addTerrainState = MutableStateFlow<Resource<Terrain>?>(null)
    val addTerrainState: StateFlow<Resource<Terrain>?> = _addTerrainState

    private val _editTerrainState = MutableStateFlow<Resource<Terrain>?>(null)
    val editTerrainState: StateFlow<Resource<Terrain>?> = _editTerrainState

    private val _editingTerrainId = MutableStateFlow<Int?>(null)

    private val _terrainName = MutableStateFlow("")
    val terrainName: StateFlow<String> = _terrainName

    private val _hectares = MutableStateFlow("")
    val hectares: StateFlow<String> = _hectares

    private val _cropType = MutableStateFlow("Soja")
    val cropType: StateFlow<String> = _cropType

    private val _state = MutableStateFlow("")
    val state: StateFlow<String> = _state

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _neighborhood = MutableStateFlow("")
    val neighborhood: StateFlow<String> = _neighborhood

    private val _street = MutableStateFlow("")
    val street: StateFlow<String> = _street

    private val _number = MutableStateFlow("")
    val number: StateFlow<String> = _number

    private val _latitude = MutableStateFlow("")
    val latitude: StateFlow<String> = _latitude

    private val _longitude = MutableStateFlow("")
    val longitude: StateFlow<String> = _longitude

    private val _crops = MutableStateFlow<List<Crop>>(emptyList())
    val crops: StateFlow<List<Crop>> = _crops

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError

    private val _hectaresError = MutableStateFlow<String?>(null)
    val hectaresError: StateFlow<String?> = _hectaresError

    private val _stateError = MutableStateFlow<String?>(null)
    val stateError: StateFlow<String?> = _stateError

    private val _cityError = MutableStateFlow<String?>(null)
    val cityError: StateFlow<String?> = _cityError

    private val _latitudeError = MutableStateFlow<String?>(null)
    val latitudeError: StateFlow<String?> = _latitudeError

    private val _longitudeError = MutableStateFlow<String?>(null)
    val longitudeError: StateFlow<String?> = _longitudeError

    val cropTypes get() = _crops.value.map { it.type }

    fun onTerrainNameChange(value: String) {
        if (value.length <= 60) _terrainName.value = value
        _nameError.value = null
    }

    fun onHectaresChange(value: String) {
        if (value.length <= 8 && value.matches(Regex("^\\d{0,6}(\\.\\d{0,2})?$"))) {
            _hectares.value = value
        }
        _hectaresError.value = null
    }

    fun onCropTypeChange(value: String) { _cropType.value = value }

    fun onStateChange(value: String) {
        val filtered = value.filter { it.isLetter() || it == ' ' }
        if (filtered.length <= 2) _state.value = filtered.uppercase()
        _stateError.value = null
    }

    fun onCityChange(value: String) {
        val filtered = value.filter { it.isLetter() || it == ' ' || it == '-' }
        if (filtered.length <= 50) _city.value = filtered
        _cityError.value = null
    }

    fun onNeighborhoodChange(value: String) {
        val filtered = value.filter { it.isLetter() || it == ' ' || it == '-' }
        if (filtered.length <= 60) _neighborhood.value = filtered
    }

    fun onStreetChange(value: String) {
        if (value.length <= 80) _street.value = value
    }

    fun onNumberChange(value: String) {
        val filtered = value.filter { it.isLetterOrDigit() || it == '-' || it == '/' }
        if (filtered.length <= 10) _number.value = filtered.uppercase()
    }

    fun onLatitudeChange(value: String) {
        if (value.length <= 10 && value.matches(Regex("^-?\\d{0,2}(\\.\\d{0,6})?$|^-$"))) {
            _latitude.value = value
        }
        _latitudeError.value = null
    }

    fun onLongitudeChange(value: String) {
        if (value.length <= 11 && value.matches(Regex("^-?\\d{0,3}(\\.\\d{0,6})?$|^-$"))) {
            _longitude.value = value
        }
        _longitudeError.value = null
    }

    private fun validateAddForm(): Boolean {
        _nameError.value = if (_terrainName.value.isBlank()) "Informe o nome do terreno" else null
        _hectaresError.value = if ((_hectares.value.toDoubleOrNull() ?: 0.0) <= 0.0) {
            "Informe um valor de hectares maior que zero"
        } else null
        _stateError.value = if (_state.value.isBlank()) "Informe o estado (UF)" else null
        _cityError.value = if (_city.value.isBlank()) "Informe a cidade" else null
        _latitudeError.value = if (_latitude.value.toDoubleOrNull() == null) "Informe uma latitude válida" else null
        _longitudeError.value = if (_longitude.value.toDoubleOrNull() == null) "Informe uma longitude válida" else null

        return listOf(
            _nameError.value,
            _hectaresError.value,
            _stateError.value,
            _cityError.value,
            _latitudeError.value,
            _longitudeError.value
        ).all { it == null }
    }

    private fun validateEditForm(): Boolean {
        _nameError.value = if (_terrainName.value.isBlank()) "Informe o nome do terreno" else null
        _hectaresError.value = if ((_hectares.value.toDoubleOrNull() ?: 0.0) <= 0.0) {
            "Informe um valor de hectares maior que zero"
        } else null

        return _nameError.value == null && _hectaresError.value == null
    }

    private fun clearErrors() {
        _nameError.value = null
        _hectaresError.value = null
        _stateError.value = null
        _cityError.value = null
        _latitudeError.value = null
        _longitudeError.value = null
    }

    fun loadTerrains() {
        viewModelScope.launch {
            _terrainsState.value = Resource.Loading
            _terrainsState.value = repository.getTerrains()
        }
    }

    fun loadCrops() {
        viewModelScope.launch {
            val result = repository.getCrops()
            if (result is Resource.Success) {
                _crops.value = result.data
                if (_crops.value.isNotEmpty() && _crops.value.none { it.type == _cropType.value }) {
                    _cropType.value = _crops.value.first().type
                }
            }
        }
    }

    fun loadTerrainForEdit(terrainId: Int) {
        _editingTerrainId.value = terrainId
        clearErrors()
        viewModelScope.launch {
            val result = repository.getTerrain(terrainId)
            if (result is Resource.Success) {
                val terrain = result.data
                _terrainName.value = terrain.name
                _hectares.value = terrain.hectares.toString()
                _cropType.value = terrain.cropType
                _state.value = terrain.state
                _city.value = terrain.city
                _neighborhood.value = terrain.neighborhood
                _street.value = terrain.street
                _number.value = terrain.number
                _latitude.value = terrain.latitude.toString()
                _longitude.value = terrain.longitude.toString()
            }
        }
    }

    fun addTerrain() {
        if (!validateAddForm()) {
            _addTerrainState.value = Resource.Error("Corrija os campos destacados antes de salvar")
            return
        }
        viewModelScope.launch {
            _addTerrainState.value = Resource.Loading
            _addTerrainState.value = repository.addTerrain(buildRequest())
        }
    }

    fun updateTerrain() {
        val id = _editingTerrainId.value ?: return
        if (!validateEditForm()) {
            _editTerrainState.value = Resource.Error("Corrija os campos destacados antes de salvar")
            return
        }
        viewModelScope.launch {
            _editTerrainState.value = Resource.Loading
            val request = UpdateTerrainRequest(
                name = _terrainName.value.takeIf { it.isNotBlank() },
                hectares = _hectares.value.toDoubleOrNull()
            )
            _editTerrainState.value = repository.updateTerrain(id, request)
        }
    }

    private fun buildRequest(): AddTerrainRequest {
        val cropId = _crops.value.firstOrNull { it.type == _cropType.value }?.id ?: 1
        return AddTerrainRequest(
            cropId = cropId,
            name = _terrainName.value,
            hectares = _hectares.value.toDoubleOrNull() ?: 0.0,
            state = _state.value,
            city = _city.value,
            neighborhood = _neighborhood.value.takeIf { it.isNotBlank() },
            street = _street.value.takeIf { it.isNotBlank() },
            number = _number.value.takeIf { it.isNotBlank() },
            latitude = _latitude.value.toDoubleOrNull() ?: 0.0,
            longitude = _longitude.value.toDoubleOrNull() ?: 0.0
        )
    }

    fun resetAddTerrainState() { _addTerrainState.value = null }
    fun resetEditTerrainState() { _editTerrainState.value = null }

    fun clearForm() {
        _editingTerrainId.value = null
        _terrainName.value = ""
        _hectares.value = ""
        _cropType.value = "Soja"
        _state.value = ""
        _city.value = ""
        _neighborhood.value = ""
        _street.value = ""
        _number.value = ""
        _latitude.value = ""
        _longitude.value = ""
        clearErrors()
    }
}
