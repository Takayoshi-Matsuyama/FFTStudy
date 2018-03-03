package tech.tkys.fft.main;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Provides the entry point of this application.
 */
public class Main extends Application {

    /**
     * The entry point of this application.
     * @param args command arguments
     */
    public static void main(String[] args) {

        FFTService.executeFFTTest();

        launch(args);
    }

    /**
     * The entry point of JavaFX.
     * @param primaryStage The primary stage.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        AppBootStrapper appBootStrapper = new AppBootStrapper();
        appBootStrapper.startApplication(primaryStage);
    }
}
