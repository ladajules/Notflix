package com.ladajules.notflix.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val hasCompletedOnboarding: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
) : Parcelable {

    // Convert User to Map for Firebase
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "hasCompletedOnboarding" to hasCompletedOnboarding,
            "createdAt" to createdAt,
            "lastLoginAt" to lastLoginAt
        )
    }

    companion object {
        // Create User from Firebase document
        fun fromMap(map: Map<String, Any>): User {
            return User(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                email = map["email"] as? String ?: "",
                hasCompletedOnboarding = map["hasCompletedOnboarding"] as? Boolean ?: false,
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
                lastLoginAt = map["lastLoginAt"] as? Long ?: System.currentTimeMillis()
            )
        }
    }

}

