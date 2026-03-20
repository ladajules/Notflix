package com.ladajules.notflix.data.repository;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ladajules.notflix.data.model.Profile;
import com.ladajules.notflix.data.remote.FirebaseManager;
import com.ladajules.notflix.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProfileRepository {

    private final FirebaseFirestore firestore;
    private static final String TAG = "ProfileRepository";

    public ProfileRepository() {
        this.firestore = FirebaseManager.getFirestore();
    }

    public interface ProfileCallback<T> {
        void onResult(boolean success, T data, Exception e);
    }

    public void createProfile(Profile profile, ProfileCallback<String> callback) {
        Log.d(TAG, "Creating profile for user: " + profile.getUserId());

        String profileId = profile.getId();
        if (profileId == null || profileId.isEmpty()) {
            profileId = firestore.collection(Constants.PROFILES_COLLECTION).document().getId();
            profile.setId(profileId);
        }

        firestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .set(profile.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Profile created successfully with ID: " + profile.getId());
                        callback.onResult(true, profile.getId(), null);
                    } else {
                        Log.e(TAG, "Failed to create profile", task.getException());
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void getProfilesForUser(String userId, ProfileCallback<List<Profile>> callback) {
        Log.d(TAG, "Getting profiles for user: " + userId);

        firestore.collection(Constants.PROFILES_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Profile> profiles = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            Map<String, Object> data = document.getData();
                            if (data != null) {
                                // Add ID to the map if it's not already there
                                data.put("id", document.getId());
                                Profile profile = Profile.fromMap(data);
                                if (profile != null) {
                                    profiles.add(profile);
                                }
                            }
                        }
                        // Sort by createdAt
                        Collections.sort(profiles, (p1, p2) -> Long.compare(p1.getCreatedAt(), p2.getCreatedAt()));
                        
                        Log.d(TAG, "Found " + profiles.size() + " profiles");
                        callback.onResult(true, profiles, null);
                    } else {
                        Log.e(TAG, "Failed to get profiles", task.getException());
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void getProfile(String profileId, ProfileCallback<Profile> callback) {
        Log.d(TAG, "Getting profile: " + profileId);

        firestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            if (data != null) {
                                data.put("id", document.getId());
                                Profile profile = Profile.fromMap(data);
                                callback.onResult(true, profile, null);
                            } else {
                                callback.onResult(true, null, null);
                            }
                        } else {
                            callback.onResult(true, null, null);
                        }
                    } else {
                        Log.e(TAG, "Failed to get profile", task.getException());
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void updateProfile(String profileId, Map<String, Object> updates, ProfileCallback<Void> callback) {
        Log.d(TAG, "Updating profile: " + profileId);

        firestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .update(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Profile updated successfully");
                        callback.onResult(true, null, null);
                    } else {
                        Log.e(TAG, "Failed to update profile", task.getException());
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void deleteProfile(String profileId, ProfileCallback<Void> callback) {
        Log.d(TAG, "Deleting profile: " + profileId);

        firestore.collection(Constants.PROFILES_COLLECTION)
                .document(profileId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Profile deleted successfully");
                        callback.onResult(true, null, null);
                    } else {
                        Log.e(TAG, "Failed to delete profile", task.getException());
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void getProfileCount(String userId, ProfileCallback<Integer> callback) {
        firestore.collection(Constants.PROFILES_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        callback.onResult(true, task.getResult().size(), null);
                    } else {
                        callback.onResult(false, 0, task.getException());
                    }
                });
    }
}
