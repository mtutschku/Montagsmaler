package components.gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import components.handler.*;
import components.neuralnetwork.Matrix;
import components.neuralnetwork.Network;
import components.neuralnetwork.NetworkStats;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Grafisches Benutzerinterface fuer die Zeichnung, welche vom neuronalen Netzwerk erkannt werden soll.
 * 
 * Beinhaltet eine Implementation fuer ein Fenster, in welchem gezeichnet werden kann.
 * Weiterhin sind Buttons fuer das Umschalten zwischen Zeichnen und Radieren, 
 * sowie zum Ueberspringen des aktuell geforderten Objekts enthalten.
 * Außerdem wird in dieser Klasse eine graphische Darstellung des Lernprozesses für das neuronale Netzwerkes implementiert.
 * Dabei wird die Genauigkeit des Netzwerkes in Prozent ueber die Anzahl der Lernepochen aufgetragen.
 * 
 * @version 12. Juli 2021
 * @author Pascal Uhlendorff, Jakob Hiestermann, Moritz Klose
 */
public class GUI extends Application {

    /** Felder initialisieren, globale Variablen setzen */
    private String mode = "Paint"; /** Modus fuer das Umschalten zwischen Zeichnen und Radieren */

    private String guessLabelText = "None";
    private Meta toDrawList = new Meta();
    private String ToDrawNow = toDrawList.getRandomNext(true);
    
    /** Zaehlvariablen fuer die Statistik bezueglich Vermutungen/Erratenem */
    private int succesfulTries = 0;
    private int numberOfGuesses = 0;

    private int counter = 1; /** Zaehlvariable fuer die Versuche */
    private int maxTurns = toDrawList.getMeta().size() + 1; /** Maximale Anzahl an Versuchen */

    private static Translator translator;
    private static Network network;

    boolean ignore = false; /** Flagge fuer das Blockieren der Nutzereingabe */
    boolean onlyonce = true; /** Flagge damit das Statistikfenster nur einmal geoeffnet wird*/

    /** Variablen fuer Timer */
    private static final int TIMERSTART = 30;
    private Integer time = TIMERSTART;

    /** setter fuer GuessLabel */
    public void setGuessLabelText (String text) {
        guessLabelText = text;
    }

    /** Setter fuer Translator */
    public static void setTranslator (Translator h) {
        translator = h;
    }

    /** Setter fuer Network */
    public static void setNetwork (Network n) {
        network = n;
    }

    /** */
    @Override
    public void start(Stage primaryStage) throws Exception {

        /** Ab hier wird die GUI implementiert */
        final int SIZE = 784; 
        Canvas canvas = new Canvas(SIZE, SIZE);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        /** Rahmen um das Canvas */
        graphicsContext.setStroke(Color.GREY);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokeRect(0, 0, SIZE, SIZE);
        graphicsContext.setStroke(Color.BLACK);

        /** Label deklariert */ 
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

        Label finalStatistic = new Label();
        finalStatistic.setFont(new Font("Arial", 24));

        /** Buttons initialisieren */ 
        final int ICONSIZE = 15;

        Image iconClear = new Image(getClass().getClassLoader().getResourceAsStream("bin.png"));
        Button buttonClear = new Button();
        ImageView iconClearView = new ImageView(iconClear);
        iconClearView.setFitHeight(ICONSIZE);
        iconClearView.setFitWidth(ICONSIZE);
        buttonClear.setGraphic(iconClearView);

        Image iconPen = new Image(getClass().getClassLoader().getResourceAsStream("pen.png"));
        Button buttonPaint = new Button();
        ImageView iconPenView = new ImageView(iconPen);
        iconPenView.setFitHeight(ICONSIZE);
        iconPenView.setFitWidth(ICONSIZE);
        buttonPaint.setGraphic(iconPenView);

        Image iconEraser = new Image(getClass().getClassLoader().getResourceAsStream("eraser.png"));
        Button buttonErase = new Button();
        ImageView iconEraserView = new ImageView(iconEraser);
        iconEraserView.setFitHeight(ICONSIZE);
        iconEraserView.setFitWidth(ICONSIZE);
        buttonErase.setGraphic(iconEraserView);

        Image iconNext = new Image(getClass().getClassLoader().getResourceAsStream("next.png"));
        Button buttonNextWord = new Button();
        ImageView iconNextView = new ImageView(iconNext);
        iconNextView.setFitHeight(ICONSIZE);
        iconNextView.setFitWidth(ICONSIZE);
        buttonNextWord.setGraphic(iconNextView);

        /** Gridpane mit allen Buttons und Labels verknuepft */ 
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.add(buttonClear, 0, 0, 1, 1);
        gridPane.add(buttonPaint, 1, 0, 1, 1);
        gridPane.add(buttonErase, 2, 0, 1, 1);
        gridPane.add(buttonNextWord, 3, 0, 1, 1);
        gridPane.add(thingToDraw, 10, 0, 1, 1);
        gridPane.add(count, 15, 0, 1, 1);
        gridPane.add(counterMax, 16, 0, 1, 1);
        gridPane.add(guess, 20, 0, 1, 1);
        gridPane.add(timerLabel, 25, 0, 1, 1);

        /** Horizontale Ausrichtung aller Buttons/Label */
        HBox topBar = new HBox(gridPane);

        /** Timeline fuer den Timer der waehrend des Spiels ablaeuft */
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

        /** Popup fuer den richtigen Tipp erstellen */
        Popup rightGuess = new Popup();
        rightGuess.setX(primaryStage.getWidth());
        rightGuess.setY(primaryStage.getHeight());

        /** Handler fuer Input von der Maus. Erste Phase beim Druecken der linken Maustaste. */
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(ignore) { /** Flagge fuer das Ingorieren von Nutzereingaben */
                        return;
                    }      
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
        
        /** Handler fuer das Ziehen des Mauszeigers */
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
            new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(ignore) { /** Flagge fuers Ignorieren von Nutzereingaben */
                        return;
                    }  
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

        /** Handler fuer das Loslassen der linken Maustaste. Enthaelt das Raten des Netzwerks */
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
             new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(ignore) {
                        return;
                    }
                    if(mode.equals("Paint")) { 
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                    } else if(mode.equals("Erase")) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.clearRect(event.getX(), event.getY(), 10, 10);
                        graphicsContext.closePath();
                    }


                    /** Raten des Gemalten */
                    WritableImage writableImage = canvas.snapshot(null, null);

                    /** Groeße des Bildes entspricht der Groeße des Canvas */
                    BufferedImage bufferedImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
                    
                    /** Uebertragen des WriteableImage in ein BufferedImage */
                    SwingFXUtils.fromFXImage(writableImage, bufferedImage); 

                    /** Uebersetzen des BufferedImage in eine Matrix */
                    Data translatedInput; 
                    translatedInput = translator.translateImage(bufferedImage); 
                    
                    /** Netzwerk verarbeitet den Input und gibt eine Vermutung zurueck*/
                    Matrix networkGuessM;
                    String networkGuess;

                    networkGuessM = network.feedForward(translatedInput.getInputs()); 
                    networkGuess = Meta.getCertainMETA(networkGuessM.getHighestValueRow());

                    /** Anzeige auf Aktuelle Vermutung setzen */
                    setGuessLabelText(networkGuess);
                    guess.setText("Guess: " + guessLabelText);

                    /** Initalisierung der Sprachausgabe */
                    Speech speech = new Speech();
                    speech.text(guessLabelText);

                    /** Zaehlt wie oft das Netzwerk geraten hat */
                    numberOfGuesses++; 
                    
                    /** Anzeige des Popups, wenn die Vermutung richtig war */
                    if(guessLabelText.equals(ToDrawNow)) {
                        succesfulTries++;
                        ignore = true; /** Nutzereingabe deaktivieren */
                        timeline.stop(); /** Timer anhalten */
                        Text currentGuess = new Text("It is a/an " + guessLabelText);
                        currentGuess.setFont(new Font("Arial", 30));
                        currentGuess.setFill(Color.RED);
                        rightGuess.getContent().add(currentGuess); 
                        rightGuess.show(primaryStage);
                        
                        /** Popup wird mit Verzögerung ausgblendet */
                        PauseTransition delay = new PauseTransition(Duration.seconds(2));
                        
                        delay.setOnFinished(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event) {
                                rightGuess.hide();
                                buttonNextWord.fire(); /** Wechsel zur naechsten zu malenden Sache */
                                rightGuess.getContent().remove(currentGuess); /** Zuruecksetzen des Labels */
                                ignore = false; /** Nutzereingabe wieder aktivieren */
                                timeline.play(); /** Timer wieder aktivieren */
                            }
                        });
                        delay.play(); 
                    }
                }
   
            });  
        


        /** Handler fuer die Buttons */

        /** Handler fuer den Button zum Zuruecksetzen des Canvas */
        buttonClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphicsContext.clearRect(0, 0, SIZE, SIZE);
                graphicsContext.setStroke(Color.GREY);
                graphicsContext.setLineWidth(2);
                graphicsContext.strokeRect(0, 0, SIZE, SIZE);
                graphicsContext.setStroke(Color.BLACK);
                buttonPaint.fire(); /** nach dem Zuruecksetzen des Canvas Rueckkehr in den Zeichenmodus */
            }
        });

        /** Handler fuer den Button zum Zeichnen */
        buttonPaint.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                mode = "Paint";
            }
        });

        /** Handler fuer den Button zum Radieren */
        buttonErase.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                mode = "Erase";
            }
        });

        /** Handler fuer den Button zum Weiterschalten der zu malenden Sache */
        buttonNextWord.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!(toDrawList.getMeta().isEmpty()) || counter != maxTurns) { /** Check, ob das Spiel zu ende ist */
                (ToDrawNow) = toDrawList.getRandomNext(true);
                    thingToDraw.setText(ToDrawNow);
                    count.setText("Try: " + Integer.toString(++counter));
                    buttonClear.fire(); /** Zuruecksetzen des Canvas */

                    setGuessLabelText("None");
                    guess.setText("Guess: " + guessLabelText);

                    time = TIMERSTART + 1; /** Zuruecksetzen des Timers */
                } else if(onlyonce) {
                    onlyonce = false;
                    primaryStage.close();
                    double proportionGuessesToRight = (double) succesfulTries / numberOfGuesses * 100;
                    String percentRightGuesses = String.format("The network guessed right in %.2f%% of cases. It tried a total of %d times. You got %d out of %d.", proportionGuessesToRight, numberOfGuesses, succesfulTries, maxTurns);
                    finalStatistic.setText(percentRightGuesses);

                    StackPane statisticsLayout = new StackPane();
                    statisticsLayout.getChildren().add(finalStatistic);

                    Scene scene2 = new Scene(statisticsLayout, 1500, 150);

                    Stage statisticsWindow = new Stage();
                    statisticsWindow.setTitle("Statistics");
                    statisticsWindow.setScene(scene2);
                    statisticsWindow.setResizable(false);
                    statisticsWindow.show();
                }
                
            }
        });

        /** Zusammenfuegen von Zeichenflaeche und Kontrollleiste */
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBar);
        borderPane.setCenter(canvas);
        Scene scene = new Scene(borderPane);

        /** Fenster erstellen */
        primaryStage.setTitle("Paint It");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("brushicon.jpg")));
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