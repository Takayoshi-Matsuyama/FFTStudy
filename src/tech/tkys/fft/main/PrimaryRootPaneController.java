package tech.tkys.fft.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import tech.tkys.fft.test.FFTTestService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Double.parseDouble;

public class PrimaryRootPaneController implements javafx.fxml.Initializable {

    double samplingFrequency = 1024.0;
    int samplingNumber = 1024;
    ArrayList<Double> timeSeriesData = null;
    ArrayList<Double> fftData = null;

    @FXML
    private TextField samplingFrequencyTextField;

    @FXML
    private TextField samplingNumberTextField;

    @FXML
    private ChoiceBox functionChoiceBox;

    @FXML
    private LineChart<Double, Double> timeSeriesLineChart;

    @FXML
    private LineChart<Double, Double> frequencyLineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.samplingFrequencyTextField.setText("1024");
        this.samplingNumberTextField.setText("1024");
        this.functionChoiceBox.getItems().addAll(
                "0.1*sin(2*PI*100t)+0.2*sin(2*PI*200t)+0.3*sin(2*PI*300t)",
                "sin(t/3)",
                "sin(t/2)",
                "sin(t)",
                "sin(2t)",
                "sin(3t)",
                "6 * cos(6 * PI * t) +  4 * sin(18 * PI * t)"
        );
        this.functionChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void onGenerateTimeSeriesButtionClicked(ActionEvent event) {

        this.samplingFrequency = 1024.0;
        this.samplingNumber = 1024;

        try {
            this.samplingFrequency = Double.parseDouble(this.samplingFrequencyTextField.getText());
            this.samplingNumber = Integer.parseInt(this.samplingNumberTextField.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.samplingFrequency = 1024.0;
            this.samplingNumber = 1024;
        }

        FFTTestService fftTestService = null;
        Object service = ServiceContainer.getService("FFTTestService");
        if ((service instanceof FFTTestService) == false) {
            return;
        } else {
            fftTestService = (FFTTestService)service;
        }

        String selectedFunction = (String)this.functionChoiceBox.getSelectionModel().getSelectedItem();
        this.timeSeriesData = fftTestService.generateTimeSeriesData(this.samplingFrequency, this.samplingNumber, selectedFunction);

        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName("Time Series Data");
        Double dt = 1.0 / this.samplingFrequency;
        Double time = 0.0;
        int pointCount = 0;
        for (Double tsDataElement : this.timeSeriesData) {
            xyChartSeries.getData().add(new XYChart.Data<>(time, tsDataElement));
            time += dt;
            pointCount++;
            if (pointCount > 99) {
                break;
            }
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
        Double df = this.samplingFrequency / this.samplingNumber;
        Double frequency = 0.0;
        for (Double fftDataElement : this.fftData) {
            xyChartSeries.getData().add(new XYChart.Data<>(frequency, fftDataElement));
            frequency += df;
        }

        this.frequencyLineChart.getData().add(xyChartSeries);
    }

    @FXML
    public void onClearButtonClicked(ActionEvent event) {
        if (this.timeSeriesData != null) {
            this.timeSeriesData.clear();
        }

        if (this.fftData != null) {
            this.fftData.clear();
        }

        this.timeSeriesLineChart.getData().clear();
        this.frequencyLineChart.getData().clear();
    }

}
