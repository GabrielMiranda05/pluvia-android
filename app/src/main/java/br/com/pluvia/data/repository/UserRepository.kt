package br.com.pluvia.data.repository

import br.com.pluvia.data.model.UpdateProfileRequest
import br.com.pluvia.data.model.User
import br.com.pluvia.data.remote.RetrofitClient
import br.com.pluvia.util.Resource

class UserRepository {

    private val api = RetrofitClient.instance

    suspend fun getProfile(): Resource<User> {
        return try {
            val response = api.getProfile()
            if (response.isSuccessful && response.body() != null) Resource.Success(response.body()!!)
            else Resource.Error(if (response.code() == 401) "Sessão expirada. Faça login novamente" else "Erro ao carregar perfil")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }

    suspend fun updateProfile(name: String, email: String): Resource<User> {
        return try {
            val response = api.updateProfile(UpdateProfileRequest(name = name, email = email))
            if (response.isSuccessful && response.body() != null) Resource.Success(response.body()!!.user)
            else Resource.Error(if (response.code() == 409) "E-mail já utilizado por outro usuário" else "Erro ao atualizar perfil")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }
}
