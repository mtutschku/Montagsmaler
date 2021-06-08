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
import javafx.scene.layout.Pane;
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

    Button button;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Paint It");
        stage.setResizable(false);

        //Buttons are not used yet
        /*
        Button buttonUndo = new Button();
        buttonUndo.setText("UNDO");

        Button buttonNextWord = new Button("NEXT");

        Button buttonEraser = new Button("ERASER");

        HBox buttons = new HBox();
        buttons.getChildren().addAll(buttonUndo, buttonEraser, buttonNextWord);
        */

        Canvas canvas = new Canvas(840, 840);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        //event handler for mouse input
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

        Pane pane = new Pane(canvas);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}

