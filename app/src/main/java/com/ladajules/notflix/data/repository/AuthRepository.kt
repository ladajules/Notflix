package com.ladajules.notflix.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.ladajules.notflix.data.remote.FirebaseManager
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseManager.auth

    sealed class AuthResult {
        data class Success(val userId: String) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid

            if (userId != null) {
                AuthResult.Success(userId)
            } else {
                AuthResult.Error("Failed to create account. Please try again.")
            }
        } catch (e: IllegalStateException) {
            AuthResult.Error("Firebase not initialized. Check google-services.json.")
        } catch (e: FirebaseAuthWeakPasswordException) {
            AuthResult.Error("Password is too weak. Please use a stronger password.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error("Invalid email format.")
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthResult.Error("An account with this email already exists.")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An unknown error occurred.")
        }
    }

    suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid

            if (userId != null) {
                AuthResult.Success(userId)
            } else {
                AuthResult.Error("Failed to sign in. Please try again.")
            }
        } catch (e: IllegalStateException) {
            AuthResult.Error("Firebase not initialized. Check google-services.json.")
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResult.Error("No account found with this email.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error("Incorrect password. Please try again.")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An unknown error occurred.")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}