package tech.tkys.fft.main;

import javafx.application.Application;
import javafx.stage.Stage;
import tech.tkys.fft.test.FFTTestService;

public class Main extends Application {

    public static void main(String[] args) {

        FFTTestService.executeFFTTest2();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppBootStrapper appBootStrapper = new AppBootStrapper();
        appBootStrapper.startApplication(primaryStage);
    }
}
