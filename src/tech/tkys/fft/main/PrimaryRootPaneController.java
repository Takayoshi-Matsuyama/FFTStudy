package tech.tkys.fft.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import tech.tkys.fft.test.FFTTestService;

import java.util.ArrayList;

public class PrimaryRootPaneController {

    @FXML
    private LineChart<Double, Double> timeSeriesLineChart;

    @FXML
    public void onSelectButtionClicked(ActionEvent event) {
        System.out.println("onSelectButtionClicked");

        FFTTestService fftTestService = null;
        Object service = ServiceContainer.getService("FFTTestService");
        if ((service instanceof FFTTestService) == false) {
            return;
        } else {
            fftTestService = (FFTTestService)service;
        }

        ArrayList<Double> timeSeriesData = fftTestService.generateTimeSeriesData();
    }

}
