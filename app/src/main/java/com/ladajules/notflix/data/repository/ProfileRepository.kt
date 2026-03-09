package com.ladajules.notflix.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ladajules.notflix.data.model.Profile
import com.ladajules.notflix.data.remote.FirebaseManager
import com.ladajules.notflix.utils.Constants
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val firestore: FirebaseFirestore = FirebaseManager.firestore
    private val TAG = "ProfileRepository"

    suspend fun createProfile(profile: Profile): Result<String> {
        return try {
            Log.d(TAG, "Creating profile for user: ${profile.userId}")
            
            // Generate ID if not provided
            val profileId = profile.id.ifEmpty { 
                firestore.collection(Constants.PROFILES_COLLECTION).document().id 
            }
            
            val profileWithId = profile.copy(id = profileId)
            
            firestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .set(profileWithId.toMap())
                .await()
            
            Log.d(TAG, "Profile created successfully with ID: $profileId")
            Result.success(profileId)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create profile", e)
            Result.failure(e)
        }
    }

    suspend fun getProfilesForUser(userId: String): Result<List<Profile>> {
        return try {
            Log.d(TAG, "Getting profiles for user: $userId")
            
            val querySnapshot = firestore.collection(Constants.PROFILES_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            val profiles = querySnapshot.documents.mapNotNull { document ->
                document.data?.let { data ->
                    Profile.fromMap(data + ("id" to document.id))
                }
            }.sortedBy { it.createdAt }
            
            Log.d(TAG, "Found ${profiles.size} profiles")
            Result.success(profiles)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get profiles", e)
            Result.failure(e)
        }
    }

    suspend fun getProfile(profileId: String): Result<Profile?> {
        return try {
            Log.d(TAG, "Getting profile: $profileId")
            
            val document = firestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .get()
                .await()
            
            val profile = if (document.exists()) {
                document.data?.let { data ->
                    Profile.fromMap(data + ("id" to document.id))
                }
            } else null
            
            Log.d(TAG, "Profile found: ${profile != null}")
            Result.success(profile)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get profile", e)
            Result.failure(e)
        }
    }

    suspend fun updateProfile(profileId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            Log.d(TAG, "Updating profile: $profileId")
            
            firestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .update(updates)
                .await()
            
            Log.d(TAG, "Profile updated successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update profile", e)
            Result.failure(e)
        }
    }

    suspend fun deleteProfile(profileId: String): Result<Unit> {
        return try {
            Log.d(TAG, "Deleting profile: $profileId")
            
            firestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .delete()
                .await()
            
            Log.d(TAG, "Profile deleted successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete profile", e)
            Result.failure(e)
        }
    }

    suspend fun getProfileCount(userId: String): Result<Int> {
        return try {
            val querySnapshot = firestore.collection(Constants.PROFILES_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            Result.success(querySnapshot.size())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}