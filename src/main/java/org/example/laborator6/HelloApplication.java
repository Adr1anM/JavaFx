package org.example.laborator6;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;

    private List<ImageView> images = new ArrayList<>();
    private Random random = new Random();
    private Pane rightPane;
    private boolean animationRunning = false; // Flag to track if animation is running

    @Override
    public void start(Stage primaryStage) {
        // Left pane content
        VBox leftPaneContent = new VBox();
        Label loadOneImageLabel = new Label("Load one image");
        Button loadImageButton = new Button("Load Image");
        loadImageButton.setOnAction(event -> loadImage());
        Label loadAllImagesLabel = new Label("Load all images from folder");
        Button loadAllImagesButton = new Button("Load All Images");
        loadAllImagesButton.setOnAction(event -> loadImagesFromDirectory());

        // Start/Stop buttons
        Button startButton = new Button("Start Animation");
        startButton.setOnAction(event -> startAnimation());
        Button stopButton = new Button("Stop Animation");
        stopButton.setOnAction(event -> stopAnimation());

        leftPaneContent.getChildren().addAll(loadOneImageLabel, loadImageButton, loadAllImagesLabel, loadAllImagesButton, startButton, stopButton);

        // Left pane
        StackPane leftPane = new StackPane(new Label("Left Pane"));
        leftPane.getChildren().add(leftPaneContent);

        // Right pane
        rightPane = new Pane();

        // Split pane
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.3); // Set divider position

        // Create scene and set it on the stage
        Scene scene = new Scene(splitPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Image Loader");
        primaryStage.show();
    }

    private void startAnimation() {
        animationRunning = true;
        for (ImageView imageView : images) {
            animateImage(imageView);
        }
    }

    private void stopAnimation() {
        animationRunning = false;
    }

    private void loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            addImageToScene(image);
        }
    }

    private void loadImagesFromDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory for All Images");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            try {
                File[] files = selectedDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && isImageFile(file)) {
                            Image image = new Image(file.toURI().toString());
                            addImageToScene(image);
                        }
                    }
                }
            } catch (Exception e) {
                // Show a message box indicating that images could not be loaded
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load images");
                alert.setContentText("An error occurred while loading images from the selected directory.");
                alert.showAndWait();
            }
        }
    }

    private boolean isImageFile(File file) {
        String name = file.getName();
        String extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        return extension.equals("png") || extension.equals("jpg") || extension.equals("gif") || extension.equals("bmp");
    }

    private void addImageToScene(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setFitHeight(IMAGE_HEIGHT);
        double startX = random.nextDouble() * (SCREEN_WIDTH - IMAGE_WIDTH);
        double startY = random.nextDouble() * (SCREEN_HEIGHT - IMAGE_HEIGHT);
        imageView.setX(startX);
        imageView.setY(startY);
        rightPane.getChildren().add(imageView);
        images.add(imageView); // Add the image view to the list

        // Do not animate the image initially
    }

    private void animateImage(ImageView imageView) {
        final double[] speedX = {random.nextDouble() * 2 - 1};
        final double[] speedY = {random.nextDouble() * 2 - 1};
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(animationRunning) {
                    imageView.setX(imageView.getX() + speedX[0]);
                    imageView.setY(imageView.getY() + speedY[0]);

                    // Check for collision with screen boundaries
                    if (imageView.getX() <= 0 || imageView.getX() >= SCREEN_WIDTH - IMAGE_WIDTH) {
                        speedX[0] = -speedX[0]; // Reverse direction on collision with horizontal boundaries
                    }
                    if (imageView.getY() <= 0 || imageView.getY() >= SCREEN_HEIGHT - IMAGE_HEIGHT) {
                        speedY[0] = -speedY[0]; // Reverse direction on collision with vertical boundaries
                    }
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }


}

