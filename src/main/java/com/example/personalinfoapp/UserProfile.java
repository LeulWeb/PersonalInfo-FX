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

    public String getHobby() {
        return hobby;
    }

    public String getFuturePlan() {
        return futurePlan;
    }

    public String getFavoriteMusic() {
        return favoriteMusic;
    }

    public String getFavoriteFilm() {
        return favoriteFilm;
    }

    public String getSkills() {
        return skills;
    }

    public String toFileString() {
        return fullName + "," + hobby + "," + futurePlan + "," + favoriteMusic + "," + favoriteFilm + "," + skills;
    }

    public static UserProfile fromFileString(String fileString) {
        String[] fields = fileString.split(",");
        String fullName = fields[0];
        String hobby = fields[1];
        String futurePlan = fields[2];
        String favoriteMusic = fields[3];
        String favoriteFilm = fields[4];
        String skills = fields[5];

        return new UserProfile(fullName, hobby, futurePlan, favoriteMusic, favoriteFilm, skills);
    }

    @Override
    public String toString() {
        return fullName; // Or any other field you want to display as the string representation
    }
}
