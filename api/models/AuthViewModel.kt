class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    fun login(username: String, password: String) {
        // Pensez à utiliser LiveData et Coroutine pour gérer les requêtes asynchrones
        val response = repository.login(username, password)
        // Gérez la réponse
    }
}
// Path: api/models/AuthViewModel.kt