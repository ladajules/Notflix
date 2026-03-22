package com.ladajules.notflix.data.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Download {
    private String id;
    private String profileId;
    private int movieId;
    private String title;
    private String posterPath;
    private String backdropPath;
    private String status;
    private String size;
    private long timestamp;

    public Download() {}

    public Download(String profileId, Movie movie) {
        this.profileId = profileId;
        this.movieId = movie.getId();
        this.title = movie.getTitle();
        this.posterPath = movie.getPosterPath();
        this.backdropPath = movie.getBackdropPath();
        this.status = "Downloaded";
        int randomSize = new Random().nextInt((1500 - 500) + 1) + 500;
        this.size = randomSize + " MB";
        
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProfileId() { return profileId; }
    public void setProfileId(String profileId) { this.profileId = profileId; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getFullPosterPath() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public String getFullBackdropPath() {
        return "https://image.tmdb.org/t/p/w300" + backdropPath;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("profileId", profileId);
        map.put("movieId", movieId);
        map.put("title", title);
        map.put("posterPath", posterPath);
        map.put("backdropPath", backdropPath);
        map.put("status", status);
        map.put("size", size);
        map.put("timestamp", timestamp);
        return map;
    }

    public static Download fromMap(String id, Map<String, Object> map) {
        Download download = new Download();
        download.setId(id);
        download.setProfileId((String) map.get("profileId"));
        
        Object movieIdObj = map.get("movieId");
        if (movieIdObj instanceof Long) {
            download.setMovieId(((Long) movieIdObj).intValue());
        } else if (movieIdObj instanceof Integer) {
            download.setMovieId((Integer) movieIdObj);
        }
        
        download.setTitle((String) map.get("title"));
        download.setPosterPath((String) map.get("posterPath"));
        download.setBackdropPath((String) map.get("backdropPath"));
        download.setStatus((String) map.get("status"));
        download.setSize((String) map.get("size"));
        
        Object timestampObj = map.get("timestamp");
        if (timestampObj instanceof Long) {
            download.setTimestamp((Long) timestampObj);
        }
        
        return download;
    }
}
