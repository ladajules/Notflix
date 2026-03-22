package com.ladajules.notflix.data.repository;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.data.remote.FirebaseManager;
import com.ladajules.notflix.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListRepository {
    private final FirebaseFirestore firestore;

    public UserListRepository() {
        this.firestore = FirebaseManager.getFirestore();
    }

    public interface UserListCallback<T> {
        void onResult(boolean success, T data, Exception e);
    }

    public void addToMyList(String profileId, Movie movie, UserListCallback<Void> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("profileId", profileId);
        data.put("movieId", movie.getId());
        data.put("title", movie.getTitle());
        data.put("posterPath", movie.getPosterPath());
        data.put("backdropPath", movie.getBackdropPath());
        data.put("timestamp", System.currentTimeMillis());

        firestore.collection(Constants.USERLISTS_COLLECTION)
                .document(profileId + "_" + movie.getId())
                .set(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResult(true, null, null);
                    } else {
                        callback.onResult(false, null, task.getException());
                    }
                });
    }

    public void getMyList(String profileId, UserListCallback<List<Movie>> callback) {
        firestore.collection(Constants.USERLISTS_COLLECTION)
                .whereEqualTo("profileId", profileId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Movie> movies = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            Movie movie = new Movie();
                            movie.setId(((Long) doc.get("movieId")).intValue());
                            movie.setTitle((String) doc.get("title"));
                            movie.setPosterPath((String) doc.get("posterPath"));
                            movie.setBackdropPath((String) doc.get("backdropPath"));
                            movies.add(movie);
                        }
                        callback.onResult(true, movies, null);
                    } else {
                        callback.onResult(false, null, task.getException());
                    }
                });
    }
}
