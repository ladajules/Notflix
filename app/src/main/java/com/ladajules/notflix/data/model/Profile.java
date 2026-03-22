package com.ladajules.notflix.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile implements Parcelable {
    private String id;
    private String userId;
    private String name;
    private String avatarUrl;
    private long createdAt;

    public Profile() {
        // firebase
    }

    public Profile(String id, String userId, String name, String avatarUrl, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }

    protected Profile(Parcel in) {
        id = in.readString();
        userId = in.readString();
        name = in.readString();
        avatarUrl = in.readString();
        createdAt = in.readLong();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", userId);
        map.put("name", name);
        map.put("avatarUrl", avatarUrl);
        map.put("createdAt", createdAt);
        return map;
    }

    public static Profile fromMap(Map<String, Object> map) {
        if (map == null) return null;
        
        Profile profile = new Profile();
        profile.setId((String) map.getOrDefault("id", ""));
        profile.setUserId((String) map.getOrDefault("userId", ""));
        profile.setName((String) map.getOrDefault("name", ""));
        profile.setAvatarUrl((String) map.getOrDefault("avatarUrl", ""));
        
        Object createdAtObj = map.get("createdAt");
        if (createdAtObj instanceof Long) {
            profile.setCreatedAt((Long) createdAtObj);
        } else {
            profile.setCreatedAt(System.currentTimeMillis());
        }
        
        return profile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(avatarUrl);
        dest.writeLong(createdAt);
    }
}
