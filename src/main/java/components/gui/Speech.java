package components.gui;

import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JOptionPane;

/**
 * Diese Methode erweitert die GUI in Form einer Sprachausgabe
 * Dabei wird ein mp3 Player erzeugt, welcher auf gespeicherter mp3-Dateien in dem Resource Folder zugreift
 * 
 * @version 09. Juli 2021
 * @author Moritz Klose
 */
public class Speech {


    /**
     * Diese Methode überprüft welches Wort erraten wurden und übergibt dieses dann an die playText Methode
     * Dabei wird eine Switch-Case Anweisung zur Umsetzung verwendet
     * 
     * @param word String, der das Wort beschreibt, welches das neurale Netzwerk erkannt hat.
     */
    public void text(String word){
        switch (word){
            case "Eye":
                playText("Eye.mp3");
                break;
            case "Eiffel Tower":
                playText("EiffelTower.mp3");
                break;
            case "Giraffe":
                playText("Giraffe.mp3");
                break;
            case "Hourglass":
                playText("Hourglass.mp3");
                break;
            default:
                playText("Eye.mp3");
        }

    }

    /**
     * Diese Methode erzeugt eine Sprachausgabe mithilfe eines Players, welcher zur Sprachausgabe vorgesehen ist
     * 
     * @param word String, der das Wort beschreibt, welches das neurale Netzwerk erkannt hat.
     */
    public void playText(String word){
        try{
            FileInputStream fs = new FileInputStream(new File("src/main/resources/" + word));
            Player player = new Player(fs);
            player.play();
      
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error");
            e.printStackTrace();
        }
    }
}
