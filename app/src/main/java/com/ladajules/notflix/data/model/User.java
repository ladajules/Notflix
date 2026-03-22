package com.ladajules.notflix.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {
    private String id;
    private String name;
    private String email;
    private boolean hasCompletedOnboarding;
    private long createdAt;
    private long lastLoginAt;

    public User() {
    }

    public User(String id, String name, String email, boolean hasCompletedOnboarding, long createdAt, long lastLoginAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.hasCompletedOnboarding = hasCompletedOnboarding;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        hasCompletedOnboarding = in.readByte() != 0;
        createdAt = in.readLong();
        lastLoginAt = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isHasCompletedOnboarding() { return hasCompletedOnboarding; }
    public void setHasCompletedOnboarding(boolean hasCompletedOnboarding) { this.hasCompletedOnboarding = hasCompletedOnboarding; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(long lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("email", email);
        map.put("hasCompletedOnboarding", hasCompletedOnboarding);
        map.put("createdAt", createdAt);
        map.put("lastLoginAt", lastLoginAt);
        return map;
    }

    public static User fromMap(Map<String, Object> map) {
        if (map == null) return null;
        
        User user = new User();
        user.setId((String) map.getOrDefault("id", ""));
        user.setName((String) map.getOrDefault("name", ""));
        user.setEmail((String) map.getOrDefault("email", ""));
        
        Object onboarding = map.get("hasCompletedOnboarding");
        user.setHasCompletedOnboarding(onboarding instanceof Boolean ? (Boolean) onboarding : false);
        
        Object created = map.get("createdAt");
        user.setCreatedAt(created instanceof Long ? (Long) created : System.currentTimeMillis());
        
        Object lastLogin = map.get("lastLoginAt");
        user.setLastLoginAt(lastLogin instanceof Long ? (Long) lastLogin : System.currentTimeMillis());
        
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeByte((byte) (hasCompletedOnboarding ? 1 : 0));
        dest.writeLong(createdAt);
        dest.writeLong(lastLoginAt);
    }
}
