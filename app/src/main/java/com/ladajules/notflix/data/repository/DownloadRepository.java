package com.ladajules.notflix.data.repository;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.ladajules.notflix.data.model.Download;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.remote.FirebaseManager;
import com.ladajules.notflix.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadRepository {
    private final FirebaseFirestore firestore;
    private static final String TAG = "DownloadRepository";

    public DownloadRepository() {
        this.firestore = FirebaseManager.getFirestore();
    }

    public interface DownloadCallback<T> {
        void onResult(boolean success, T data, Exception e);
    }

    public void addDownload(String profileId, Movie movie, DownloadCallback<String> callback) {
        Download download = new Download(profileId, movie);
        firestore.collection(Constants.DOWNLOADS_COLLECTION)
                .add(download.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        callback.onResult(true, task.getResult().getId(), null);
                    } else {
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void getDownloadsForProfile(String profileId, DownloadCallback<List<Download>> callback) {
        firestore.collection(Constants.DOWNLOADS_COLLECTION)
                .whereEqualTo("profileId", profileId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Download> downloads = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            Map<String, Object> data = doc.getData();
                            if (data != null) {
                                downloads.add(Download.fromMap(doc.getId(), data));
                            }
                        }
                        Log.d(TAG, "Successfully retrieved " + downloads.size() + " downloads");
                        callback.onResult(true, downloads, null);
                    } else {
                        Log.e(TAG, "Error getting downloads", task.getException());
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void deleteDownload(String downloadId, DownloadCallback<Void> callback) {
        firestore.collection(Constants.DOWNLOADS_COLLECTION)
                .document(downloadId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResult(true, null, null);
                    } else {
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void deleteAllDownloads(String profileId, DownloadCallback<Void> callback) {
        firestore.collection(Constants.DOWNLOADS_COLLECTION)
                .whereEqualTo("profileId", profileId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        WriteBatch batch = firestore.batch();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            batch.delete(doc.getReference());
                        }
                        batch.commit().addOnCompleteListener(commitTask -> {
                            if (commitTask.isSuccessful()) {
                                callback.onResult(true, null, null);
                            } else {
                                callback.onResult(false, null, commitTask.getException());
                            }
                        });
                    } else {
                        callback.onResult(false, null, task.getException());
                    }
                });
    }
}
