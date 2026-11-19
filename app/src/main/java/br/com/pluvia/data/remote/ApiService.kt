package br.com.pluvia.data.remote

import br.com.pluvia.data.model.AddTerrainRequest
import br.com.pluvia.data.model.AuthResponse
import br.com.pluvia.data.model.Crop
import br.com.pluvia.data.model.LandResponse
import br.com.pluvia.data.model.LoginRequest
import br.com.pluvia.data.model.RegisterRequest
import br.com.pluvia.data.model.UpdateProfileRequest
import br.com.pluvia.data.model.UpdateProfileResponse
import br.com.pluvia.data.model.UpdateTerrainRequest
import br.com.pluvia.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("api/crops")
    suspend fun getCrops(): Response<List<Crop>>

    @GET("api/users/me")
    suspend fun getProfile(): Response<User>

    @PUT("api/users/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UpdateProfileResponse>

    @GET("api/lands")
    suspend fun getTerrains(): Response<List<LandResponse>>

    @GET("api/lands/{id}")
    suspend fun getTerrain(@Path("id") id: Int): Response<LandResponse>

    @POST("api/lands")
    suspend fun addTerrain(@Body request: AddTerrainRequest): Response<LandResponse>

    @PUT("api/lands/{id}")
    suspend fun updateTerrain(
        @Path("id") id: Int,
        @Body request: UpdateTerrainRequest
    ): Response<LandResponse>

    @DELETE("api/lands/{id}")
    suspend fun deleteTerrain(@Path("id") id: Int): Response<Unit>
}
