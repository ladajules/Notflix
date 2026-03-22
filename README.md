# 🎬 Notflix 

> **Disclaimer**: This project is strictly for educational purposes and is not intended for commercial use. Notflix is a clone created specifically to practice modern Android UI implementation and RESTful API integration.

## 📱 About the Project

[cite_start]Notflix is a mobile streaming platform inspired by Netflix[cite: 8]. [cite_start]The application allows users to browse movies and TV shows, view detailed information, and manage a personalized watchlist[cite: 8]. [cite_start]It replicates the core features of a modern streaming app using publicly available movie data while adding unique, original features to enhance user engagement[cite: 9].

## ✨ Features

### Core Features
* [cite_start]**User Login and Signup**: Secure account creation and authentication using email and password[cite: 11].
* [cite_start]**Profile Selection**: Allows users to create and choose from multiple profiles under a single account, each with personalized watchlists and viewing preferences[cite: 31, 32].
* [cite_start]**Movie Browsing**: Browse movies and TV shows displayed in categorized sections[cite: 39].
* [cite_start]**Movie Details**: Displays comprehensive information including title, description, genre, rating, release date, and episodes[cite: 59].
* [cite_start]**User Profile**: Shows profile-specific data such as watchlist, watched/unfinished shows, downloads, and notifications[cite: 94, 95].

### Bonus Original Features
* [cite_start]**🎟️ Mystery Ticket**: Discover content in a fun, unbiased way[cite: 129]. [cite_start]Users select a preferred genre, content type, and duration range, and the app generates a randomly selected title from the database[cite: 130]. [cite_start]This encourages users to explore new content without judging a movie by its cover[cite: 131, 205].
* [cite_start]**📊 Notflix Wrapped (Viewing Stats)**: Displays summarized viewing insights based on the user's watch history[cite: 126]. It presents four key metrics:
  * [cite_start]Total watch time [cite: 127, 208]
  * [cite_start]Favorite genre [cite: 127, 208]
  * [cite_start]Binge factor (number of episodes watched consecutively in a single session) [cite: 127, 208]
  * [cite_start]Prime viewing time (e.g., early bird, night owl, lunchtime viewer) [cite: 127, 208]

## 🛠️ Tech Stack

* [cite_start]**Frontend**: Java [cite: 152][cite_start], XML [cite: 153] 
* [cite_start]**Backend & Database**: Firebase Authentication [cite: 139][cite_start], Firebase Firestore [cite: 138, 141]
* [cite_start]**API**: TMDB (The Movie Database) API [cite: 137, 156]
* [cite_start]**IDE/Tools**: Android Studio [cite: 143][cite_start], Android Emulator [cite: 145]

## 📂 Package Structure
*(Note: This represents the initial architecture plan. The structure grew and evolved as development progressed to accommodate new features).*

```text
[cite_start]com.ladajules.notflix [cite: 168]
│
[cite_start]├── data [cite: 169]
[cite_start]│   ├── model [cite: 170]
│   │   ├── Movie.java
│   │   ├── Genre.java
│   │   └── UserProfile.java
[cite_start]│   ├── remote [cite: 175]
│   │   ├── MovieApi.java
│   │   └── ApiClient.java
[cite_start]│   └── repository [cite: 178]
│       └── MovieRepository.java
│
[cite_start]└── ui [cite: 174]
    [cite_start]├── auth [cite: 180]
    │   ├── LoginActivity.java
    │   └── SignupActivity.java
    ├── profile_selection [cite: 183]
    │   ├── ProfileSelectionActivity.java
    │   └── ProfileAdapter.java
    ├── home [cite: 186]
    │   ├── HomeActivity.java
    │   └── MovieAdapter.java
    ├── movie_details [cite: 189]
    │   └── MovieDetailsActivity.java
    ├── user_profile [cite: 191]
    │   └── UserProfileActivity.java
    ├── viewmodel [cite: 194]
    │   ├── AuthViewModel.java
    │   └── MovieViewModel.java
    └── utils [cite: 197]
        ├── Constants.java
        └── MainActivity.java
