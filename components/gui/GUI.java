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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;

/** Grafisches Benutzerinterface für die Zeichnung, welche vom neuronalen Netzwerk erkannt werden soll.
 * 
 * Beinhaltet eine Implementation für ein Fenster, in welchem gezeichnet werden kann.
 * Weiterhin sind Buttons für das Umschalten zwischen Zeichnen und Radieren, 
 * sowie zum Rückgängigmachen der letzten Aktion und zum Überspringen des aktuell geforderten Objekts enthalten.
 * 
 * @version 8. Juni 2021
 * @author Pascal Uhlendorff
 */
public class GUI extends Application {

    String mode = "Paint";

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
                    } else if(mode.equals("Eraser")) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.clearRect(event.getX(), event.getY(), 6, 6);
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
                    } else if(mode.equals("Eraser")) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.clearRect(event.getX(), event.getY(), 6, 6);
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
                    } else if(mode.equals("Eraser")) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.clearRect(event.getX(), event.getY(), 6, 6);
                        graphicsContext.closePath();
                    }
                }
            });  

        //initialize buttons
        Button buttonNew = new Button("New");
        Button buttonPaint = new Button("Paint");
        Button buttonEraser = new Button("Eraser");
        Button buttonUndo = new Button("Undo");
        Button buttonNextWord = new Button("Next");
    
        //horizontal allignment of all buttons
        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getChildren().addAll(buttonNew, buttonPaint, buttonEraser, buttonUndo, buttonNextWord);

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

        buttonEraser.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                mode = "Eraser";
            }
        });

        buttonUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
        
        buttonNextWord.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });

        Label thingToDraw = new Label("TEST");

        //vertical representation of info
        VBox info = new VBox();
        info.getChildren().addAll(thingToDraw);

        //sets the scene/stage and shows it
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(buttons);
        borderPane.setCenter(canvas);
        borderPane.setRight(info);

        Scene scene = new Scene(borderPane);

        stage.setTitle("Paint It");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}