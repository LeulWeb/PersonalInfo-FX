package com.example.personalinfoapp;

public class UserProfile {
    private String fullName;
    private String hobby;
    private String futurePlan;
    private String favoriteMusic;
    private String favoriteFilm;
    private String skills;

    public UserProfile(String fullName, String hobby, String futurePlan, String favoriteMusic, String favoriteFilm, String skills) {
        this.fullName = fullName;
        this.hobby = hobby;
        this.futurePlan = futurePlan;
        this.favoriteMusic = favoriteMusic;
        this.favoriteFilm = favoriteFilm;
        this.skills = skills;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getFuturePlan() {
        return futurePlan;
    }

    public void setFuturePlan(String futurePlan) {
        this.futurePlan = futurePlan;
    }

    public String getFavoriteMusic() {
        return favoriteMusic;
    }

    public void setFavoriteMusic(String favoriteMusic) {
        this.favoriteMusic = favoriteMusic;
    }

    public String getFavoriteFilm() {
        return favoriteFilm;
    }

    public void setFavoriteFilm(String favoriteFilm) {
        this.favoriteFilm = favoriteFilm;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
