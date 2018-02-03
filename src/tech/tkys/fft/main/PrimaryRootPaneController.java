package tech.tkys.fft.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import tech.tkys.fft.test.FFTTestService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PrimaryRootPaneController implements javafx.fxml.Initializable {

    double samplingFrequency = 1024.0;
    int samplingNumber = 1024;
    TimeSeriesDataSet timeSeries;
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
                "0.1*sin(2*PI*100t)+0.2*sin(2*PI*200t)+0.3*sin(2*PI*300t)");
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
        this.timeSeries = fftTestService.generateTimeSeries(
                this.samplingFrequency,
                this.samplingNumber,
                selectedFunction);

        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName("x(t)");
        for (int i = 0; i < this.timeSeries.getSize(); i++) {
            xyChartSeries.getData().add(new XYChart.Data<>(
                    this.timeSeries.getTimeStamps().get(i),
                    this.timeSeries.getTimeSeries().get(i)));

            if (i > 98) {
                break;
            }
        }

        this.timeSeriesLineChart.getData().add(xyChartSeries);
    }

    @FXML
    public void onDumpTimeSeriesButtonClicked(ActionEvent event) {
        if (this.timeSeries == null) {
            return;
        }

        System.out.println("t\tx(t)");
        for (int i = 0; i < this.timeSeries.getSize(); i++) {
            System.out.printf(
                    "%f\t%f%n",
                    this.timeSeries.getTimeStamps().get(i),
                    this.timeSeries.getTimeSeries().get(i));
        }
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

        if (this.timeSeries == null) {
            return;
        }

        this.fftData = fftTestService.executeFFT(this.timeSeries.getTimeSeries());

        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName("X(f)");
        Double df = this.samplingFrequency / this.samplingNumber;
        Double frequency = 0.0;
        for (Double fftDataElement : this.fftData) {
            xyChartSeries.getData().add(new XYChart.Data<>(frequency, fftDataElement));
            frequency += df;
        }

        this.frequencyLineChart.getData().add(xyChartSeries);
    }

    @FXML
    public void onDumpFFTButtonClicked(ActionEvent event) {
        if (this.fftData == null) {
            return;
        }

        System.out.println("f\tX(f)");
        for (Double fftDataElement : this.fftData) {
            System.out.println(fftDataElement);
        }
    }

    @FXML
    public void onClearButtonClicked(ActionEvent event) {
        if (this.timeSeries != null) {
            this.timeSeries.clear();
        }

        if (this.fftData != null) {
            this.fftData.clear();
        }

        this.timeSeriesLineChart.getData().clear();
        this.frequencyLineChart.getData().clear();
    }

}
