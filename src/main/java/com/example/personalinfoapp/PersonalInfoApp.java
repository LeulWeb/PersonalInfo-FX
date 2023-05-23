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
import java.util.Optional;

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Initialize userProfiles list and listView
        userProfiles = FXCollections.observableArrayList();
        listView = new ListView<>(userProfiles);

        // Create buttons and set their actions
        Button addButton = new Button("New Student");
        addButton.setOnAction(e -> showAddDialog());

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> showUpdateDialog());

        Button removeButton = new Button("Delete Student");
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
        primaryStage.setTitle("Student Information Manager");

        primaryStage.show();

        // Load user profiles from file
        loadUserProfiles();
    }

    // Creates the application layout with a menu bar and content area
    private BorderPane createAppLayout(VBox content) {
        Text info = new Text("Advanced Java Assignment 2015 E.C MiT.");
        Text madeBy = new Text("Leul Webshet, Kaleb Asnake, Bereket Hailay, Getachew Degie, Selam Mebratu, Dessi Mulatie");
        madeBy.setStyle("-fx-font-weight: bold");
        VBox footer = new VBox(10, madeBy, info);
        Text about = new Text("Manage your students with ease in just few click: ");
        BorderPane layout = new BorderPane();
//        layout.setTop(createMenuBar());
        layout.setTop(about);
        layout.setCenter(content);
        footer.setPadding(new Insets(8));
        layout.setBottom(footer);

        return layout;
    }

    // Creates the menu bar with a file menu
//    private MenuBar createMenuBar() {
//        MenuBar menuBar = new MenuBar();
//
//        Menu fileMenu = new Menu("File");
//
//
//        MenuItem saveMenuItem = new MenuItem("Save");
//        saveMenuItem.setOnAction(e -> saveUserProfiles());
//        MenuItem exitMenuItem = new MenuItem("Exit");
//        exitMenuItem.setOnAction(e -> System.exit(0));
//        fileMenu.getItems().addAll(saveMenuItem, new SeparatorMenuItem(), exitMenuItem);
//
//        menuBar.getMenus().add(fileMenu);
//
//        return menuBar;
//    }

    // Shows a dialog for adding a new user profile
    private void showAddDialog() {
        Dialog<UserProfile> dialog = new Dialog<>();

        dialog.setTitle("Register new Student");

        // Set the button types
        ButtonType addButton = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane gridPane = createGridPane();

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String fullName = fullNameField.getText();
                String hobby = hobbyField.getText();
                String futurePlan = futurePlanField.getText();
                String favoriteMusic = favoriteMusicField.getText();
                String favoriteFilm = favoriteFilmField.getText();
                String skills = skillsField.getText();

                UserProfile userProfile = new UserProfile(fullName, hobby, futurePlan, favoriteMusic, favoriteFilm, skills);
                userProfiles.add(userProfile);
                return userProfile;
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Shows a dialog for updating an existing user profile
    private void showUpdateDialog() {
        UserProfile selectedUserProfile = listView.getSelectionModel().getSelectedItem();
        if (selectedUserProfile == null) {
            showAlert(Alert.AlertType.WARNING, "Select student first", "Please select student first to update their information");
            return;
        }

        Dialog<UserProfile> dialog = new Dialog<>();
        dialog.setTitle("Update student Profile");

        // Set the button types
        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        GridPane gridPane = createGridPane();

        // Populate the input fields with the selected user profile's data
        fullNameField.setText(selectedUserProfile.getFullName());
        hobbyField.setText(selectedUserProfile.getHobby());
        futurePlanField.setText(selectedUserProfile.getFuturePlan());
        favoriteMusicField.setText(selectedUserProfile.getFavoriteMusic());
        favoriteFilmField.setText(selectedUserProfile.getFavoriteFilm());
        skillsField.setText(selectedUserProfile.getSkills());

        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButton) {
                // Update the selected user profile with the new data
                selectedUserProfile.setFullName(fullNameField.getText());
                selectedUserProfile.setHobby(hobbyField.getText());
                selectedUserProfile.setFuturePlan(futurePlanField.getText());
                selectedUserProfile.setFavoriteMusic(favoriteMusicField.getText());
                selectedUserProfile.setFavoriteFilm(favoriteFilmField.getText());
                selectedUserProfile.setSkills(skillsField.getText());
                return selectedUserProfile;
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Removes the selected user profile from the list
    private void removeUserProfile() {
        UserProfile selectedUserProfile = listView.getSelectionModel().getSelectedItem();
        if (selectedUserProfile == null) {
            showAlert(Alert.AlertType.WARNING, "No student is selected.", "Please select a student to remove.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Remove Student Profile");
        alert.setContentText("Are you sure you want to remove the selected user profile?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            userProfiles.remove(selectedUserProfile);
        }
    }

    // Saves the user profiles to a file
    private void saveUserProfiles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (UserProfile userProfile : userProfiles) {
                // Write each user profile's data to a new line in the file
                writer.println(userProfile.getFullName());
                writer.println(userProfile.getHobby());
                writer.println(userProfile.getFuturePlan());
                writer.println(userProfile.getFavoriteMusic());
                writer.println(userProfile.getFavoriteFilm());
                writer.println(userProfile.getSkills());
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save user profiles.");
        }
    }

    // Loads user profiles from a file
    private void loadUserProfiles() {
        File file = new File(FILE_PATH);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String fullName = line;
                String hobby = reader.readLine();
                String futurePlan = reader.readLine();
                String favoriteMusic = reader.readLine();
                String favoriteFilm = reader.readLine();
                String skills = reader.readLine();

                UserProfile userProfile = new UserProfile(fullName, hobby, futurePlan, favoriteMusic, favoriteFilm, skills);
                userProfiles.add(userProfile);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load user profiles.");
        }
    }

    // Creates a grid pane with input fields for user profile data
    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label fullNameLabel = new Label("Student full Name:");
        fullNameField = new TextField();
        fullNameField.setStyle("-fx-border-color: green;" +
                "-fx-border-width: 0 0 2 0;" +
                "-fx-border-radius: 0;" +
                "-fx-padding: 0;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;");

        Label hobbyLabel = new Label("Student Hobby:");
        hobbyField = new TextField();
        hobbyField.setStyle("-fx-border-color: green;" +
                "-fx-border-width: 0 0 2 0;" +
                "-fx-border-radius: 0;" +
                "-fx-padding: 0;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;");
        Label futurePlanLabel = new Label("Student Future Plan:");
        futurePlanField = new TextField();
        futurePlanField.setStyle("-fx-border-color: green;" +
                "-fx-border-width: 0 0 2 0;" +
                "-fx-border-radius: 0;" +
                "-fx-padding: 0;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;");
        Label favoriteMusicLabel = new Label("Student Favorite Music:");
        favoriteMusicField = new TextField();
        favoriteMusicField.setStyle("-fx-border-color: green;" +
                "-fx-border-width: 0 0 2 0;" +
                "-fx-border-radius: 0;" +
                "-fx-padding: 0;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;");
        Label favoriteFilmLabel = new Label("Student Favorite Film:");
        favoriteFilmField = new TextField();
        favoriteFilmField.setStyle("-fx-border-color: green;" +
                "-fx-border-width: 0 0 2 0;" +
                "-fx-border-radius: 0;" +
                "-fx-padding: 0;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;");
        Label skillsLabel = new Label("Skills:");

        skillsField = new TextField();
        skillsField.setStyle("-fx-border-color: green;" +
                "-fx-border-width: 0 0 2 0;" +
                "-fx-border-radius: 0;" +
                "-fx-padding: 0;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: black;");

        gridPane.add(fullNameLabel, 0, 0);
        gridPane.add(fullNameField, 1, 0);
        gridPane.add(hobbyLabel, 0, 1);
        gridPane.add(hobbyField, 1, 1);
        gridPane.add(futurePlanLabel, 0, 2);
        gridPane.add(futurePlanField, 1, 2);
        gridPane.add(favoriteMusicLabel, 0, 3);
        gridPane.add(favoriteMusicField, 1, 3);
        gridPane.add(favoriteFilmLabel, 0, 4);
        gridPane.add(favoriteFilmField, 1, 4);
        gridPane.add(skillsLabel, 0, 5);
        gridPane.add(skillsField, 1, 5);

        return gridPane;
    }

    // Shows an alert with the specified type, title, and message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
