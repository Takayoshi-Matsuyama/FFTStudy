package tech.tkys.fft.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import tech.tkys.fft.test.FFTTestService;

import java.util.ArrayList;

public class PrimaryRootPaneController {

    ArrayList<Double> timeSeriesData = null;
    ArrayList<Double> fftData = null;

    @FXML
    private LineChart<Double, Double> timeSeriesLineChart;

    @FXML
    private LineChart<Double, Double> frequencyLineChart;

    @FXML
    public void onGenerateTimeSeriesButtionClicked(ActionEvent event) {
        System.out.println("onSelectButtionClicked");

        FFTTestService fftTestService = null;
        Object service = ServiceContainer.getService("FFTTestService");
        if ((service instanceof FFTTestService) == false) {
            return;
        } else {
            fftTestService = (FFTTestService)service;
        }

        this.timeSeriesData = fftTestService.generateTimeSeriesData();

        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName("Time Series Data");
        Double time = 0.0;
        for (Double tsDataElement : this.timeSeriesData) {
            xyChartSeries.getData().add(new XYChart.Data<>(time, tsDataElement));
            time += 1.0;
        }

        this.timeSeriesLineChart.getData().add(xyChartSeries);
    }

    @FXML
    public void onExecuteFFTButtonClicked(ActionEvent event) {
        FFTTestService fftTestService = null;
        Object service = ServiceContainer.getService("FFTTestService");
        if ((service instanceof FFTTestService) == false) {
            return;
        } else {
            fftTestService = (FFTTestService)service;
        }

        if (this.timeSeriesData == null) {
            return;
        }

        this.fftData = fftTestService.executeFFT(this.timeSeriesData);

        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName("FFT Data");
        Double frequency = 0.0;
        for (Double fftDataElement : this.fftData) {
            xyChartSeries.getData().add(new XYChart.Data<>(frequency, fftDataElement));
            frequency += 1.0;
        }

        this.frequencyLineChart.getData().add(xyChartSeries);
    }

}
