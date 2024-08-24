package service

interface IAuthService {
    suspend fun verifyCredential(credential: String)
}