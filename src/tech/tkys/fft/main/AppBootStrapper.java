package tech.tkys.fft.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tech.tkys.fft.test.FFTTestService;

import java.io.IOException;

/**
 * アプリケーションを起動する（初期化とメインウィンドウの表示）
 */
public class AppBootStrapper {
    public void startApplication(Stage primaryStage) {
        ServiceContainer.registerService("FFTTestService", new FFTTestService());

        this.showPrimaryStage(primaryStage);
    }

    private void showPrimaryStage(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("PrimaryRootPane.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
