package br.com.pluvia.data.repository

import br.com.pluvia.data.model.AuthResponse
import br.com.pluvia.data.model.LoginRequest
import br.com.pluvia.data.model.RegisterRequest
import br.com.pluvia.data.remote.AuthTokenStore
import br.com.pluvia.data.remote.RetrofitClient
import br.com.pluvia.util.Resource
import com.google.gson.Gson
import com.google.gson.JsonObject

class AuthRepository {

    private val api = RetrofitClient.instance
    private val gson = Gson()

    suspend fun login(email: String, password: String): Resource<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val auth = response.body()!!
                auth.token?.let { AuthTokenStore.token = it }
                Resource.Success(auth)
            } else {
                Resource.Error(parseErrorMessage(response.errorBody()?.string(), "Credenciais inválidas"))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }

    suspend fun register(name: String, email: String, password: String): Resource<AuthResponse> {
        return try {
            val response = api.register(RegisterRequest(name, email, password, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResult = login(email, password)
                if (loginResult is Resource.Success) loginResult
                else Resource.Success(response.body()!!)
            } else {
                Resource.Error(parseErrorMessage(response.errorBody()?.string(), "Erro ao criar conta"))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }

    fun logout() {
        AuthTokenStore.clear()
    }

    private fun parseErrorMessage(errorBody: String?, fallback: String): String {
        return try {
            val json = gson.fromJson(errorBody, JsonObject::class.java)
            json.get("message")?.asString ?: json.get("error")?.asString ?: fallback
        } catch (_: Exception) {
            fallback
        }
    }
}
