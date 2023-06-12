package com.example.personalinfoapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;

public class PersonalInfoApp extends Application {
    private ObservableList<UserProfile> userProfiles;
    private ListView<UserProfile> listView;
    private TextField fullNameField;
    private TextField hobbyField;
    private TextField futurePlanField;
    private TextField favoriteMusicField;
    private TextField favoriteFilmField;
    private TextField skillsField;

    private static final String FILE_PATH = "user_profiles.txt";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/personal_info";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize userProfiles list and listView
        userProfiles = FXCollections.observableArrayList();
        listView = new ListView<>(userProfiles);

        // Create buttons and set their actions
        Button addButton = new Button("New user");
        addButton.setOnAction(e -> showAddDialog());

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> showUpdateDialog());

        Button removeButton = new Button("Delete user");
        removeButton.setOnAction(e -> removeUserProfile());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveUserProfiles());

        // Create button box and set its contents
        HBox buttonBox = new HBox(12, addButton, updateButton, removeButton, saveButton);

        // Create main layout VBox and set its contents and padding
        VBox vbox = new VBox(20, listView, buttonBox);
        vbox.setPadding(new Insets(20));

        // Create the scene with the layout
        Scene scene = new Scene(createAppLayout(vbox), 600, 400);

        // Apply CSS styles by adding the stylesheet to the scene
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Set the scene to the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Information Manager");
        primaryStage.show();

        // Load user profiles from the database
        loadUserProfilesFromDatabase();
    }

    // Creates the application layout with a menu bar and content area
    private BorderPane createAppLayout(VBox content) {
        Text info = new Text("Advanced Java Assignment 2015 E.C MiT.");
        Text madeBy = new Text("Leul Webshet, Kaleb Asnake, Bereket Hailay, Getachew Degie, Selam Mebratu, Dessi Mulatie");
        madeBy.setStyle("-fx-font-weight: bold");
        VBox footer = new VBox(10, madeBy, info);
        Text about = new Text("Manage your users with ease in just a few clicks");
        BorderPane layout = new BorderPane();
        layout.setTop(about);
        layout.setCenter(content);
        footer.setPadding(new Insets(8));
        layout.setBottom(footer);

        return layout;
    }

    // Shows a dialog for adding a new user profile
    private void showAddDialog() {
        Dialog<UserProfile> dialog = new Dialog<>();
        dialog.setTitle("Register new user");

        // Set the button types
        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        // Create the grid layout for the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        fullNameField = new TextField();
        hobbyField = new TextField();
        futurePlanField = new TextField();
        favoriteMusicField = new TextField();
        favoriteFilmField = new TextField();
        skillsField = new TextField();

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(fullNameField, 1, 0);
        grid.add(new Label("Hobby:"), 0, 1);
        grid.add(hobbyField, 1, 1);
        grid.add(new Label("Future Plan:"), 0, 2);
        grid.add(futurePlanField, 1, 2);
        grid.add(new Label("Favorite Music:"), 0, 3);
        grid.add(favoriteMusicField, 1, 3);
        grid.add(new Label("Favorite Film:"), 0, 4);
        grid.add(favoriteFilmField, 1, 4);
        grid.add(new Label("Skills:"), 0, 5);
        grid.add(skillsField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Convert the result of the dialog to a UserProfile object when the Register button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                return new UserProfile(
                        fullNameField.getText(),
                        hobbyField.getText(),
                        futurePlanField.getText(),
                        favoriteMusicField.getText(),
                        favoriteFilmField.getText(),
                        skillsField.getText()
                );
            }
            return null;
        });

        // Show the dialog and handle the result
        dialog.showAndWait().ifPresent(userProfile -> {
            addUserProfile(userProfile);
        });
    }

    // Adds a user profile to the list and database
    private void addUserProfile(UserProfile userProfile) {
        userProfiles.add(userProfile);
        saveUserProfileToDatabase(userProfile);
    }

    // Shows a dialog for updating an existing user profile
    private void showUpdateDialog() {
        UserProfile selectedUserProfile = listView.getSelectionModel().getSelectedItem();
        if (selectedUserProfile != null) {
            Dialog<UserProfile> dialog = new Dialog<>();
            dialog.setTitle("Update user");

            // Set the button types
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Create the grid layout for the dialog
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            fullNameField = new TextField(selectedUserProfile.getFullName());
            hobbyField = new TextField(selectedUserProfile.getHobby());
            futurePlanField = new TextField(selectedUserProfile.getFuturePlan());
            favoriteMusicField = new TextField(selectedUserProfile.getFavoriteMusic());
            favoriteFilmField = new TextField(selectedUserProfile.getFavoriteFilm());
            skillsField = new TextField(selectedUserProfile.getSkills());

            grid.add(new Label("Full Name:"), 0, 0);
            grid.add(fullNameField, 1, 0);
            grid.add(new Label("Hobby:"), 0, 1);
            grid.add(hobbyField, 1, 1);
            grid.add(new Label("Future Plan:"), 0, 2);
            grid.add(futurePlanField, 1, 2);
            grid.add(new Label("Favorite Music:"), 0, 3);
            grid.add(favoriteMusicField, 1, 3);
            grid.add(new Label("Favorite Film:"), 0, 4);
            grid.add(favoriteFilmField, 1, 4);
            grid.add(new Label("Skills:"), 0, 5);
            grid.add(skillsField, 1, 5);

            dialog.getDialogPane().setContent(grid);

            // Convert the result of the dialog to a UserProfile object when the Update button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    return new UserProfile(
                            fullNameField.getText(),
                            hobbyField.getText(),
                            futurePlanField.getText(),
                            favoriteMusicField.getText(),
                            favoriteFilmField.getText(),
                            skillsField.getText()
                    );
                }
                return null;
            });

            // Show the dialog and handle the result
            dialog.showAndWait().ifPresent(updatedUserProfile -> {
                updateUserProfile(selectedUserProfile, updatedUserProfile);
            });
        }
    }

    // Updates a user profile in the list and database
    private void updateUserProfile(UserProfile oldUserProfile, UserProfile updatedUserProfile) {
        userProfiles.remove(oldUserProfile);
        userProfiles.add(updatedUserProfile);
        updateUserProfileInDatabase(oldUserProfile, updatedUserProfile);
    }

    // Removes a user profile from the list and database
    private void removeUserProfile() {
        UserProfile selectedUserProfile = listView.getSelectionModel().getSelectedItem();
        if (selectedUserProfile != null) {
            userProfiles.remove(selectedUserProfile);
            removeUserProfileFromDatabase(selectedUserProfile);
        }
    }

    // Saves the user profiles to a file
    private void saveUserProfiles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (UserProfile userProfile : userProfiles) {
                writer.println(userProfile.toFileString());
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads the user profiles from the file
    private void loadUserProfilesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                UserProfile userProfile = UserProfile.fromFileString(line);
                if (userProfile != null) {
                    userProfiles.add(userProfile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads the user profiles from the database
    private void loadUserProfilesFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM user_profiles")) {
            while (resultSet.next()) {
                UserProfile userProfile = new UserProfile(
                        resultSet.getString("full_name"),
                        resultSet.getString("hobby"),
                        resultSet.getString("future_plan"),
                        resultSet.getString("favorite_music"),
                        resultSet.getString("favorite_film"),
                        resultSet.getString("skills")
                );
                userProfiles.add(userProfile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Saves a user profile to the database
    private void saveUserProfileToDatabase(UserProfile userProfile) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO user_profiles (full_name, hobby, future_plan, favorite_music, favorite_film, skills) " +
                             "VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, userProfile.getFullName());
            statement.setString(2, userProfile.getHobby());
            statement.setString(3, userProfile.getFuturePlan());
            statement.setString(4, userProfile.getFavoriteMusic());
            statement.setString(5, userProfile.getFavoriteFilm());
            statement.setString(6, userProfile.getSkills());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updates a user profile in the database
    private void updateUserProfileInDatabase(UserProfile oldUserProfile, UserProfile updatedUserProfile) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE user_profiles SET full_name=?, hobby=?, future_plan=?, favorite_music=?, favorite_film=?, skills=? " +
                             "WHERE full_name=? AND hobby=? AND future_plan=? AND favorite_music=? AND favorite_film=? AND skills=?")) {
            statement.setString(1, updatedUserProfile.getFullName());
            statement.setString(2, updatedUserProfile.getHobby());
            statement.setString(3, updatedUserProfile.getFuturePlan());
            statement.setString(4, updatedUserProfile.getFavoriteMusic());
            statement.setString(5, updatedUserProfile.getFavoriteFilm());
            statement.setString(6, updatedUserProfile.getSkills());
            statement.setString(7, oldUserProfile.getFullName());
            statement.setString(8, oldUserProfile.getHobby());
            statement.setString(9, oldUserProfile.getFuturePlan());
            statement.setString(10, oldUserProfile.getFavoriteMusic());
            statement.setString(11, oldUserProfile.getFavoriteFilm());
            statement.setString(12, oldUserProfile.getSkills());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Removes a user profile from the database
    private void removeUserProfileFromDatabase(UserProfile userProfile) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM user_profiles WHERE full_name=? AND hobby=? AND future_plan=? AND favorite_music=? AND favorite_film=? AND skills=?")) {
            statement.setString(1, userProfile.getFullName());
            statement.setString(2, userProfile.getHobby());
            statement.setString(3, userProfile.getFuturePlan());
            statement.setString(4, userProfile.getFavoriteMusic());
            statement.setString(5, userProfile.getFavoriteFilm());
            statement.setString(6, userProfile.getSkills());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
