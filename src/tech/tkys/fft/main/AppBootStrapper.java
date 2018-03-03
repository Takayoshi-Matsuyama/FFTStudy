package tech.tkys.fft.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Starts the application.
 */
public class AppBootStrapper {
    /**
     * Starts the application.
     * @param primaryStage JavaFX primary stage.
     */
    public void startApplication(Stage primaryStage) {
        ServiceContainer.registerService("FFTService", new FFTService());

        this.showPrimaryStage(primaryStage);
    }

    /**
     * Shows the primary stage.
     * @param primaryStage JavaFX primary stage.
     */
    private void showPrimaryStage(Stage primaryStage) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PrimaryRootPane.fxml"));
            root = loader.load();
            PrimaryRootPaneController controller = loader.getController();
            if (controller != null) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("FFT");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
