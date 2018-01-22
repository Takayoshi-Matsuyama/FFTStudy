package tech.tkys.fft.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import tech.tkys.fft.test.FFTTestService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PrimaryRootPaneController implements javafx.fxml.Initializable {

    ArrayList<Double> timeSeriesData = null;
    ArrayList<Double> fftData = null;

    @FXML
    private ChoiceBox functionChoiceBox;

    @FXML
    private LineChart<Double, Double> timeSeriesLineChart;

    @FXML
    private LineChart<Double, Double> frequencyLineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.functionChoiceBox.getItems().addAll(
                "sin(t)",
                "sin(2t)",
                "sin(3t)",
                "6 * cos(6 * PI * t) +  4 * sin(18 * PI * t)"
        );
        this.functionChoiceBox.getSelectionModel().selectFirst();
    }

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
            // TODO: 周波数軸の値
            frequency += 1.0;
        }

        this.frequencyLineChart.getData().add(xyChartSeries);
    }

}
