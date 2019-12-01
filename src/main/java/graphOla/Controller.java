package graphOla;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label labelE;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnAnimation;

    @FXML
    private Button btnClear;
    @FXML
    private Button BtnGenerate;

    @FXML
    private Label LabelA;

    @FXML
    private TextField textFieldGetE;

    @FXML
    private TextField textFieldGetA;

    @FXML
    private ScatterChart<Number, Number> graph;

    @FXML
    private ChoiceBox<?> choiceBoxGetMethod;

    @FXML
    private TextField textFieldGetEpsi;
    private TrajectoryXY xy;
    private Timeline timeline;

    @FXML
    void generateOnClick(ActionEvent event) {
        graph.setAnimated(true);
        generateAndAddSeries("-fx-background-color: green;");


    }

    private void generateAndAddSeries(String firstSeriesColor) {
        if (!((textFieldGetA.getText().isEmpty() || textFieldGetEpsi.getText().isEmpty()) || textFieldGetA.getText().isEmpty())) {
            XYChart.Series series = new XYChart.Series();

            try {
                xy = new TrajectoryXY(100, Double.parseDouble(textFieldGetE.getText()), Double.parseDouble(textFieldGetA.getText()), Double.parseDouble(textFieldGetEpsi.getText()), (String) choiceBoxGetMethod.getValue());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nieprawidłowa wartość");
                alert.setContentText("Podaj wartość liczbową");

                alert.show();
            } catch (IllegalArgumentException e1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nieprawidłowa wartość");
                alert.setContentText("Podaj wartość e od 0 do 1");
                alert.show();
            }
            List<Double> x = xy.getX();
            List<Double> y = xy.getY();
            series.setName("e= " + textFieldGetE.getText() + " a= " + textFieldGetA.getText() + " epsilon= " + textFieldGetEpsi.getText() + " metoda: " + choiceBoxGetMethod.getValue());
            IntStream.rangeClosed(0, 99).forEach(z -> series.getData().add(new XYChart.Data(x.get(z), y.get(z))));
            graph.getData().add(series);
            ArrayList<Node> seriesNodes = new ArrayList<Node>();
            Set<Node> nodes = graph.lookupAll(".series0");
            int flag = 0;
            for (Node n : nodes) {
                n.setStyle(firstSeriesColor);
                if (flag == 0) {
                    seriesNodes.add(n);
                }
                flag++;
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pole jest puste");
            alert.setContentText("Wpisz wartości do pola");
            alert.show();
        }
    }


    @FXML
    void clearOnClick(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }

        graph.getData().clear();
    }

    @FXML
    void saveOnClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        Node node = (Node) event.getSource();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(node.getScene().getWindow());
        StringBuilder sb = new StringBuilder();
        for (XYChart.Series<Number, Number> numberNumberSeries : graph.getData()) {
            ObservableList<XYChart.Data<Number, Number>> data = numberNumberSeries.getData();
            List<Number> xValues = data.stream().map(x -> x.getXValue()).collect(Collectors.toList());
            List<Number> yValues = data.stream().map(x -> x.getYValue()).collect(Collectors.toList());
            sb.append(numberNumberSeries.getName());
            sb.append("\n");
            sb.append(xValues.toString());
            sb.append("\n");
            sb.append(yValues.toString());
        }
        SaveFile(sb.toString(), file);

    }

    private void SaveFile(String content, File file) {
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file, true);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
        }

    }

    @FXML
    void animationOnClick(ActionEvent event) {
        graph.getData().clear();
        if (timeline != null) {
            timeline.stop();
        }

        generateAndAddSeries("-fx-background-color: transparent;");
        graph.setLegendVisible(false);
        graph.setAnimated(false);


        AtomicLong start = new AtomicLong(System.currentTimeMillis());
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.5),
                        event1 -> {
                            double diff = 2 * ((System.currentTimeMillis()) - start.get()) / 1000;
                            int cycle = (int) (diff - 1);
                            if (cycle == 98) {
                                start.set(System.currentTimeMillis());
                            }
                            XYChart.Series series1 = new XYChart.Series();
                            series1.getData().add(new XYChart.Data(xy.getX().get(cycle), xy.getY().get(cycle)));
                            graph.getData().add(series1);
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    @FXML
    void initialize() {
        assert labelE != null : "fx:id=\"labelE\" was not injected: check your FXML file 'graph.fxml'.";
        assert xAxis != null : "fx:id=\"xAxis\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnAnimation != null : "fx:id=\"btnAnimation\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'graph.fxml'.";
        assert graph != null : "fx:id=\"graph\" was not injected: check your FXML file 'graph.fxml'.";
        assert textFieldGetEpsi != null : "fx:id=\"textFieldGetEpsi\" was not injected: check your FXML file 'graph.fxml'.";
        assert yAxis != null : "fx:id=\"yAxis\" was not injected: check your FXML file 'graph.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'graph.fxml'.";
        assert BtnGenerate != null : "fx:id=\"BtnGenerate\" was not injected: check your FXML file 'graph.fxml'.";
        assert LabelA != null : "fx:id=\"LabelA\" was not injected: check your FXML file 'graph.fxml'.";
        assert textFieldGetE != null : "fx:id=\"textFieldGetE\" was not injected: check your FXML file 'graph.fxml'.";
        assert textFieldGetA != null : "fx:id=\"textFieldGetA\" was not injected: check your FXML file 'graph.fxml'.";
        assert choiceBoxGetMethod != null : "fx:id=\"choiceBoxGetMethod\" was not injected: check your FXML file 'graph.fxml'.";
    }
}
