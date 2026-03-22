# 🎬 Notflix 

> **Disclaimer**: This project is strictly for educational purposes and is not intended for commercial use. Notflix is a clone created specifically to practice modern Android UI implementation and RESTful API integration.

## 📱 About the Project

Notflix is a mobile streaming platform inspired by Netflix. The application allows users to browse movies and TV shows, view detailed information, and manage a personalized watchlist. It replicates the core features of a modern streaming app using publicly available movie data while adding unique, original features to enhance user engagement.

## ✨ Features

### Core Features
* **User Login and Signup**: Secure account creation and authentication using email and password.
* **Profile Selection**: Allows users to create and choose from multiple profiles under a single account, each with personalized watchlists and viewing preferences.
* **Movie Browsing**: Browse movies and TV shows displayed in categorized sections.
* **Movie Details**: Displays comprehensive information including title, description, genre, rating, release date, and episodes.
* **User Profile**: Shows profile-specific data such as watchlist, watched/unfinished shows, downloads, and notifications.

### Bonus Original Features
* **🎟️ Mystery Ticket**: Discover content in a fun, unbiased way. Users select a preferred genre, content type, and duration range, and the app generates a randomly selected title from the database. This encourages users to explore new content without judging a movie by its cover.
* **📊 Notflix Wrapped (Viewing Stats)**: Displays summarized viewing insights based on the user's watch history. It presents four key metrics:
  * Total watch time
  * Favorite genre
  * Binge factor (number of episodes watched consecutively in a single session)
  * Prime viewing time (e.g., early bird, night owl, lunchtime viewer)

## 🛠️ Tech Stack

* **Frontend**: Java, XML 
* **Backend & Database**: Firebase Authentication, Firebase Firestore
* **API**: TMDB (The Movie Database) API
* **IDE/Tools**: Android Studio, Android Emulator

## 📂 Package Structure
*(Note: This represents the initial architecture plan. The structure grew and evolved as development progressed to accommodate new features).*

```
com.ladajules.notflix
│
├── data
│   ├── model
│   │   ├── Movie.java
│   │   ├── Genre.java
│   │   └── UserProfile.java
│   ├── remote
│   │   ├── MovieApi.java
│   │   └── ApiClient.java
│   └── repository
│       └── MovieRepository.java
│
└── ui
    ├── auth
    │   ├── LoginActivity.java
    │   └── SignupActivity.java
    ├── profile_selection
    │   ├── ProfileSelectionActivity.java
    │   └── ProfileAdapter.java
    ├── home
    │   ├── HomeActivity.java
    │   └── MovieAdapter.java
    ├── movie_details
    │   └── MovieDetailsActivity.java
    ├── user_profile
    │   └── UserProfileActivity.java
    ├── viewmodel
    │   ├── AuthViewModel.java
    │   └── MovieViewModel.java
    └── utils
        ├── Constants.java
        └── MainActivity.java
