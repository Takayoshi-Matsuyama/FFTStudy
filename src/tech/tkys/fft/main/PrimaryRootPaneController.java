package tech.tkys.fft.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for JavaFX GUI.
 */
public class PrimaryRootPaneController implements javafx.fxml.Initializable {

    double samplingFrequency = 1024.0;
    int samplingNumber = 1024;
    TimeSeriesDataSet timeSeries;
    FrequencyDataSet fftData;

    @FXML
    private TextField samplingFrequencyTextField;

    @FXML
    private TextField sampleNumberTextField;

    @FXML
    private ChoiceBox functionChoiceBox;

    @FXML
    private LineChart<Double, Double> timeSeriesLineChart;

    @FXML
    private LineChart<Double, Double> frequencyLineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.samplingFrequencyTextField.setText("1024");
        this.sampleNumberTextField.setText("1024");
        this.functionChoiceBox.getItems().addAll(
                "0.1*sin(2*PI*100t)+0.2*sin(2*PI*200t)+0.3*sin(2*PI*300t)",
                "sin(2*PI*10.5t)");
        this.functionChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void onGenerateTimeSeriesButtionClicked(ActionEvent event) {

        this.samplingFrequency = 1024.0;
        this.samplingNumber = 1024;

        try {
            this.samplingFrequency = Double.parseDouble(this.samplingFrequencyTextField.getText());
            this.samplingNumber = Integer.parseInt(this.sampleNumberTextField.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.samplingFrequency = 1024.0;
            this.samplingNumber = 1024;
        }

        FFTService fftTestService = null;
        Object service = ServiceContainer.getService("FFTService");
        if ((service instanceof FFTService) == false) {
            return;
        } else {
            fftTestService = (FFTService)service;
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

            // グラフの表示範囲を絞る
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
        FFTService fftTestService = null;
        Object service = ServiceContainer.getService("FFTService");
        if ((service instanceof FFTService) == false) {
            return;
        } else {
            fftTestService = (FFTService)service;
        }

        if (this.timeSeries == null) {
            return;
        }

        this.fftData = fftTestService.executeFFT(
                this.samplingFrequency,
                this.samplingNumber,
                this.timeSeries.getTimeSeries());

        XYChart.Series<Double, Double> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName("X(f)");

        for (int i = 0; i < fftData.getSize(); i++) {
            xyChartSeries.getData().add(new XYChart.Data<>(
                    this.fftData.getFrequencies().get(i),
                    this.fftData.getFourierCoefficients().get(i)));

            // グラフの表示範囲を絞る
//            if (i > 40) {
//                break;
//            }
        }

        this.frequencyLineChart.getData().add(xyChartSeries);
    }

    @FXML
    public void onDumpFFTButtonClicked(ActionEvent event) {
        if (this.fftData == null) {
            return;
        }

        System.out.println("f\tX(f)");
        for (int i = 0; i < this.fftData.getSize(); i++) {
            System.out.printf(
                    "%f\t%f%n",
                    this.fftData.getFrequencies().get(i),
                    this.fftData.getFourierCoefficients().get(i));
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
