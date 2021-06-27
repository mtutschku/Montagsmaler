package components.neuralnetwork;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import java.util.ArrayList;

/**
 * Diese Klasse realisiert einen Plot der Netzwerk Statistiken mittel JavaFX
 * 
 * @version 27.06.2021
 * @author Moritz Klose 
 */
public class NetworkPlot extends Application{

    /** Lernprozess in Prozent */
    ArrayList<Double> percentData = NetworkStats.percentData;

    /** Anzahl der Epochen bezogen auf den Lernprozess */
    ArrayList<Integer> epochData = NetworkStats.epochData;

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
    }

    /**
     * Diese Methode initialisiert ein Fenster welches einen Graphen darstellt
     * dieser Graph bildet sich aus den Daten der NetworkStats.java Klasse
     * 
     * @param primaryStage platform of the application
     */
    private void init(Stage primaryStage) {
        HBox root = new HBox();
        Scene scene = new Scene(root, 550, 550);

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Epochs");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Accuracy in Percent(%)");

        LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Accuracy of the Learning Process");

        XYChart.Series<Number, Number> data = new XYChart.Series<>();
        data.setName("Network Accuracy");

        for(int i = 0; i < epochData.size(); i++) {
            data.getData().add(new XYChart.Data<Number, Number>(epochData.get(i), percentData.get(i)));
        }

        lineChart.getData().add(data);
        root.getChildren().add(lineChart);

        primaryStage.setTitle("Accuracy of the Learning Process");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
