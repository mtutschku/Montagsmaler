package components.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import components.handler.Data;
import components.handler.Handler;
import components.neuralnetwork.Matrix;
import components.neuralnetwork.Network;
import components.neuralnetwork.NetworkStats;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Grafisches Benutzerinterface für die Zeichnung, welche vom neuronalen Netzwerk erkannt werden soll.
 * 
 * Beinhaltet eine Implementation für ein Fenster, in welchem gezeichnet werden kann.
 * Weiterhin sind Buttons für das Umschalten zwischen Zeichnen und Radieren, 
 * sowie zum Rückgängigmachen der letzten Aktion und zum Überspringen des aktuell geforderten Objekts enthalten.
 * Außerdem wird in dieser Klasse eine graphische Darstellung des Lernprozesses für das neuronale Netzwerkes implementiert.
 * Dabei wird die Genauigkeit des Netzwerkes in Prozent über die Anzahl der Lernepochen aufgetragen.
 * 
 * @version 01. Juli 2021
 * @author Pascal Uhlendorff, Jakob Hiestermann, Moritz Klose
 */
public class GUI extends Application {

    //Felder initialisieren, Variablen setzen
    private String mode = "Paint";

    private String guessLabelText = "None";
    private Meta toDrawList = new Meta();
    private String ToDrawNow = toDrawList.getRandomNext(true);

    private int counter = 1;
    private int maxTurns = toDrawList.getMeta().size() + 1;

    private static Handler handler;
    private static Network network;

    //Variablen fuer Timer
    private static final int TIMERSTART = 30;
    private Integer time = TIMERSTART;

    //setter für GuessLabel
    public void setGuessLabelText (String text) {
        guessLabelText = text;
    }

    //Setter für Handler
    public static void setHandler (Handler h) {
        handler = h;
    }

    //Setter für Network
    public static void setNetwork (Network n) {
        network = n;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        /** Ab hier wird die GUI implementiert */
        final int SIZE = 784;
        Canvas canvas = new Canvas(SIZE, SIZE);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.setStroke(Color.GREY);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokeRect(0, 0, SIZE, SIZE);
        graphicsContext.setStroke(Color.BLACK);

        
        //Label declaration
        Label thingToDraw = new Label(ToDrawNow);
        thingToDraw.setFont(new Font("Arial", 24));
        
        Label counterMax = new Label("/" + Integer.toString(maxTurns));
        counterMax.setFont(new Font("Arial", 24));

        Label count = new Label("Word: " + Integer.toString(counter));
        count.setFont(new Font("Arial", 24));

        Label guess = new Label("Guess: " + guessLabelText); 
        guess.setFont(new Font("Arial", 24));

        Label timerLabel = new Label("Time: " + time.toString());
        timerLabel.setFont(new Font("Arial", 24));

        //initialize buttons
        Button buttonNew = new Button("New");
        buttonNew.setFont(new Font("Arial", 12));

        Button buttonPaint = new Button("Paint");
        buttonPaint.setFont(new Font("Arial", 12));

        Button buttonErase = new Button("Erase");
        buttonErase.setFont(new Font("Arial", 12));

        Button buttonNextWord = new Button("Next");
        buttonNextWord.setFont(new Font("Arial", 12));

        //setup gridpane with all buttons and labels
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.add(buttonNew, 0, 0, 1, 1);
        gridPane.add(buttonPaint, 1, 0, 1, 1);
        gridPane.add(buttonErase, 2, 0, 1, 1);
        gridPane.add(buttonNextWord, 3, 0, 1, 1);
        gridPane.add(thingToDraw, 10, 0, 1, 1);
        gridPane.add(count, 15, 0, 1, 1);
        gridPane.add(counterMax, 16, 0, 1, 1);
        gridPane.add(guess, 20, 0, 1, 1);
        gridPane.add(timerLabel, 25, 0, 1, 1);

        //horizontal allignment of all buttons and info
        HBox topBar = new HBox(gridPane);

        //timeline for timer on gui
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(time > 0){
                time--;
                timerLabel.setText("Time: " + time.toString());
                } else {
                    buttonNextWord.fire();
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //creating a popup for the right guess
        Popup rightGuess = new Popup();
        rightGuess.setX(primaryStage.getWidth());
        rightGuess.setY(primaryStage.getHeight());

        //event handler for mouse input
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {      
                    if(mode.equals("Paint")) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    } else if(mode.equals("Erase")) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.clearRect(event.getX(), event.getY(), 10, 10);
                    }
                }
            });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
            new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(mode.equals("Paint")) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                    } else if(mode.equals("Erase")) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.clearRect(event.getX(), event.getY(), 10, 10);
                        graphicsContext.closePath();
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                    }
                }
            });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
             new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(mode.equals("Paint")) { 
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                    } else if(mode.equals("Erase")) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.clearRect(event.getX(), event.getY(), 10, 10);
                        graphicsContext.closePath();
                    }


                    //this part translates the canvas into a matrix that is forwarded to the network which returns a guess as String
                    WritableImage writableImage = canvas.snapshot(null, null);

                    final int IM_DIMENSION = 28 * 28;
                    BufferedImage bufferedImage = new BufferedImage(IM_DIMENSION, IM_DIMENSION, BufferedImage.TYPE_INT_ARGB);
                    SwingFXUtils.fromFXImage(writableImage, bufferedImage);

                    Data translatedInput;
                    translatedInput = handler.translateImage(bufferedImage);

                    Matrix networkGuessM;
                    String networkGuess;

                    networkGuessM = network.feedForward(translatedInput.getInputs());
                    networkGuess = Meta.getCertainMETA(networkGuessM.getHighestValueRow());

                    setGuessLabelText(networkGuess);
                    guess.setText("Guess: " + guessLabelText);
                    
                    if(guessLabelText.equals(ToDrawNow)) {
                        Text currentGuess = new Text("It is a " + guessLabelText);
                        currentGuess.setFont(new Font("Arial", 30));
                        rightGuess.getContent().add(currentGuess); 
                        rightGuess.show(primaryStage);
                        
                        PauseTransition delay = new PauseTransition(Duration.seconds(2));
                        
                        delay.setOnFinished(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event) {
                                rightGuess.hide();
                                buttonNextWord.fire();
                                rightGuess.getContent().remove(currentGuess);
                            }
                        });
                        delay.play();
                    }

                    /*
                    Task<Void> popupTask = new Task<Void>() {
                      
                        @Override 
                        protected Void call() throws Exception{
                            if(guessLabelText.equals(ToDrawNow)) {
                                Text currentGuess = new Text(guessLabelText);
                                currentGuess.setFont(new Font("Arial", 30));
                                rightGuess.getContent().add(currentGuess); 

                                rightGuess.show(primaryStage);
                                Thread.sleep(5000);
                                rightGuess.hide();
                                buttonNextWord.fire();
                            } 
                            return null;
                        }
                    };

                    Thread popupThread = new Thread(popupTask);
                    popupThread.setDaemon(true);
                    popupThread.start();
                    */
                    
                    
                }
   
            });  
        


        //event handler for buttons
        buttonNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphicsContext.clearRect(0, 0, SIZE, SIZE);
                graphicsContext.setStroke(Color.GREY);
                graphicsContext.setLineWidth(2);
                graphicsContext.strokeRect(0, 0, SIZE, SIZE);
                graphicsContext.setStroke(Color.BLACK);
            }
        });

        buttonPaint.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                mode = "Paint";
            }
        });

        buttonErase.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                mode = "Erase";
            }
        });
        
        buttonNextWord.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!toDrawList.getMeta().isEmpty()) {
                (ToDrawNow) = toDrawList.getRandomNext(true);
                    thingToDraw.setText(ToDrawNow);
                    count.setText("Try: " + Integer.toString(++counter));
                    if (counter == maxTurns) {
                        buttonNextWord.setText("Exit");
                    }
                    graphicsContext.clearRect(0, 0, SIZE, SIZE);
                    graphicsContext.setStroke(Color.GREY);
                    graphicsContext.setLineWidth(2);
                    graphicsContext.strokeRect(0, 0, SIZE, SIZE);
                    graphicsContext.setStroke(Color.BLACK);

                    setGuessLabelText("None");
                    guess.setText("Guess: " + guessLabelText);

                    time = TIMERSTART + 1;
                } else {
                    primaryStage.close();
                }
                
            }
        });

        //sets the scene/primaryStage and shows it
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBar);
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane);

        primaryStage.setTitle("Paint It");
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("brushicon.jpg")));
        primaryStage.setWidth(1000);
        primaryStage.setHeight(1000);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


        /** Ab hier wird die graphische Darstellung des Netzwerks implementiert */
        
        Stage stage = new Stage();
        /** Lernprozess in Prozent */
        ArrayList<Double> percentData = NetworkStats.percentData;

        /** Anzahl der Epochen bezogen auf den Lernprozess */
        ArrayList<Integer> epochData = NetworkStats.epochData;
        
        HBox root = new HBox();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Epochs");
        xAxis.setTickLabelFont(new Font("Arial", 16));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Accuracy in Percent(%)");
        yAxis.setTickLabelFont(new Font("Arial", 16));

        LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Accuracy of the Learning Process");
        //lineChart.setFont(new Font("Arial", 16));

        XYChart.Series<Number, Number> data = new XYChart.Series<>();
        data.setName("Network Accuracy");

        for(int i = 0; i < epochData.size(); i++) {
            data.getData().add(new XYChart.Data<Number, Number>(epochData.get(i), percentData.get(i)));
        }

        lineChart.getData().add(data);
        root.getChildren().add(lineChart);

        stage.setTitle("Accuracy of the Learning Process");
        //stage.setFont("Arial", 16);
        stage.setScene(new Scene(root, 550, 550));
        stage.show();
    }
    
}