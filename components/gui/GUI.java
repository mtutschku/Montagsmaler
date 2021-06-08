package components.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
    Button button;

    @Override
    public void start(Stage stage) throws Exception {
        
        Canvas canvas = new Canvas(840, 840);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        //event handler for mouse input
        if(mode.equals("Paint")) {
            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {            
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
            });

            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                    }
            });

            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                }
            });  
        } else if(mode.equals("Eraser")) {
            graphicsContext.setFill(Color.WHITE);
            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {            
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
            });

            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                    }
            });

            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                }
            });  
        }

        //initialize buttons
        Button buttonNew = new Button("New");
        Button buttonPaint = new Button("Paint");
        Button buttonEraser = new Button("Eraser");
        Button buttonUndo = new Button("Undo");
        Button buttonNextWord = new Button("Next");
    
        //horizontal allignment of all buttons
        HBox buttons = new HBox();
        buttons.getChildren().addAll(buttonNew, buttonPaint, buttonEraser, buttonUndo, buttonNextWord);
        
        //event handler for buttons
        buttonNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphicsContext.clearRect(0, 0, 840, 840);
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

        //sets the scene/stage and shows it
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(buttons);
        borderPane.setCenter(canvas);

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