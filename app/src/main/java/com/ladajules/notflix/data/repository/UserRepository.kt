package com.ladajules.notflix.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ladajules.notflix.data.model.User
import com.ladajules.notflix.data.remote.FirebaseManager
import com.ladajules.notflix.utils.Constants
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore: FirebaseFirestore = FirebaseManager.firestore

    suspend fun createUser(user: User): Result<Unit> {
        return try {
            firestore.collection(Constants.USERS_COLLECTION)
                .document(user.id)
                .set(user.toMap())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<User?> {
        return try {
            val document = firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val userData = document.data
                if (userData != null) {
                    val user = User.fromMap(userData)
                    Result.success(user)
                } else {
                    Result.success(null)
                }
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLastLogin(userId: String): Result<Unit> {
        return try {
            val updates = mapOf("lastLoginAt" to System.currentTimeMillis())
            firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .update(updates)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}