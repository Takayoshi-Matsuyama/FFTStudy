package tech.tkys.fft.main;

import javafx.application.Application;
import javafx.stage.Stage;
import tech.tkys.fft.test.FFTTestService;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);

        System.out.println("FFT Utility");
        FFTTestService.executeFFTTest();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppBootStrapper appBootStrapper = new AppBootStrapper();
        appBootStrapper.startApplication(primaryStage);
    }
}
