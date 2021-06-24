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


public class NetworkPlot extends Application{
    ArrayList<Double> percentData = NetworkStats.percentData;
    ArrayList<Integer> epochData = NetworkStats.epochData;

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
    }

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
