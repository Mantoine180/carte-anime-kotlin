class AuthRepository(private val apiService: ApiService) {
    fun login(username: String, password: String): Call<LoginResponse> {
        return apiService.login(LoginRequest(username, password))
    }
}
// Path: repository/AuthRepository.kt