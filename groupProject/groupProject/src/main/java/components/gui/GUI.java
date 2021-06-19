package components.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/** Grafisches Benutzerinterface für die Zeichnung, welche vom neuronalen Netzwerk erkannt werden soll.
 *
 * Beinhaltet eine Implementation für ein Fenster, in welchem gezeichnet werden kann.
 * Weiterhin sind Buttons für das Umschalten zwischen Zeichnen und Radieren,
 * sowie zum Rückgängigmachen der letzten Aktion und zum Überspringen des aktuell geforderten Objekts enthalten.
 *
 * @version 9. Juni 2021
 * @author Pascal Uhlendorff
 */
public class GUI extends Application {

    private String mode = "Paint";
    private int counter = 0;
    private int maxTurns = 6;
    private static final int TIMERSTART = 20;
    private Integer time = TIMERSTART;


    public void setMaxTurns(int turns) {
        this.maxTurns = turns;
    }

    public void counter() {
        counter++;
    }

    private Meta toDraw = new Meta();

    @Override
    public void start(Stage stage) throws Exception {

        final int SIZE = 840;
        Canvas canvas = new Canvas(SIZE, SIZE);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        graphicsContext.setStroke(Color.GREY);
        graphicsContext.setLineWidth(2);
        graphicsContext.strokeRect(0, 0, SIZE, SIZE);
        graphicsContext.setStroke(Color.BLACK);

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
                    }
                });


        //Label declaration
        Label thingToDraw = new Label(toDraw.getRandomNext(true));
        thingToDraw.setFont(new Font("Arial", 24));

        Label counterMax = new Label("/" + Integer.toString(maxTurns));
        counterMax.setFont(new Font("Arial", 24));

        Label count = new Label("Try: " + Integer.toString(counter));
        count.setFont(new Font("Arial", 24));

        Label guess = new Label("Guess: " + "Guess1");
        guess.setFont(new Font("Arial", 24));

        Label timer = new Label("Time: " + time.toString());
        timer.setFont(new Font("Arial", 24));

        //initialize buttons
        Button buttonNew = new Button("New");
        buttonNew.setFont(new Font("Arial", 12));

        Button buttonPaint = new Button("Paint");
        buttonPaint.setFont(new Font("Arial", 12));

        Button buttonErase = new Button("Erase");
        buttonErase.setFont(new Font("Arial", 12));

        Button buttonNextWord = new Button("Next");
        buttonNextWord.setFont(new Font("Arial", 12));

        Button buttonGuess = new Button("Guess");
        buttonGuess.setFont(new Font("Arial", 12));

        //setup gridpane with all buttons and labels
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.add(buttonNew, 0, 0, 1, 1);
        gridPane.add(buttonPaint, 1, 0, 1, 1);
        gridPane.add(buttonErase, 2, 0, 1, 1);
        gridPane.add(buttonNextWord, 3, 0, 1, 1);
        gridPane.add(thingToDraw, 10, 0, 1, 1);
        gridPane.add(buttonGuess, 11, 0, 1, 1);
        gridPane.add(count, 15, 0, 1, 1);
        gridPane.add(counterMax, 16, 0, 1, 1);
        gridPane.add(guess, 20, 0, 1, 1);
        gridPane.add(timer, 25, 0, 1, 1);

        //horizontal allignment of all buttons and info
        HBox topBar = new HBox(gridPane);

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
                if(counter <= maxTurns) {
                    thingToDraw.setText(toDraw.getRandomNext(true));
                    counter();
                    count.setText("Try: " + Integer.toString(counter));
                    graphicsContext.clearRect(0, 0, SIZE, SIZE);
                    graphicsContext.setStroke(Color.GREY);
                    graphicsContext.setLineWidth(2);
                    graphicsContext.strokeRect(0, 0, SIZE, SIZE);
                    graphicsContext.setStroke(Color.BLACK);
                } else {
                    stage.close();
                }

            }
        });

        buttonGuess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });


        //sets the scene/stage and shows it
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBar);
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane);

        stage.setTitle("Paint It");
        stage.setWidth(1000);
        stage.setHeight(1000);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}