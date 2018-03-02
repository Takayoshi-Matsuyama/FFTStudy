package tech.tkys.fft.main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        FFTTestService.executeFFTTest();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppBootStrapper appBootStrapper = new AppBootStrapper();
        appBootStrapper.startApplication(primaryStage);
    }
}
