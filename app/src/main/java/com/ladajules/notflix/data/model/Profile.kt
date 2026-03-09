package com.ladajules.notflix.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable {

    // Convert Profile to Map for Firebase
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "name" to name,
            "avatarUrl" to avatarUrl,
            "createdAt" to createdAt
        )
    }

    companion object {
        // Create Profile from Firebase document
        fun fromMap(map: Map<String, Any>): Profile {
            return Profile(
                id = map["id"] as? String ?: "",
                userId = map["userId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                avatarUrl = map["avatarUrl"] as? String ?: "",
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis()
            )
        }

        // Default Netflix-style avatar options
        fun getDefaultAvatars(): List<String> {
            return listOf(
                "avatar_1", // Red
                "avatar_2", // Blue
                "avatar_3", // Yellow
                "avatar_4", // Green
                "avatar_5"  // Purple
            )
        }
    }
}