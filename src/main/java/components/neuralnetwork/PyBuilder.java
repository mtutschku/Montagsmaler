package components.neuralnetwork;

import com.sun.glass.events.KeyEvent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import javax.swing.*;
import java.io.*;

/** Diese Klasse dient der Erstellung eines Python-Skripts, welches für die Beschaffung der Trainingsbilder des Netzwerks
 * verantwortlich ist.
 * 
 * Hierfür wird eine GUI bereitgestellt, die es dem Benutzer erlaubt, einfach Kategorien und Parameter
 * für die Trainingssets auszuwählen.
 * Die GUI generiert zum Schluss die Python-Datei. Diese muss nur ausgeführt werden.
 * 
 * ACHTUNG: Viele Zeilen dieses Codes entstehen durch das Layout der GUI, welches durch einen Builder erstellt wurde.
 * Die eigentliche Logik dahinter ist ziemlich prägnant.
 * 
 * @author Morris Tutschku
 * @version 24. Juni 2021
 */
@SuppressWarnings("all") // Wer braucht schon Warnungen
public class PyBuilder extends JDialog {

    /** Konstruktor
     * 
     * @param parent "Ursprung" des JDialogs. Hier ein pseudo-JFrame (siehe main-Methode).
     * @param modal GUI blockiert Benutzereingaben bei anderen modaler Fenster 
     */
    public PyBuilder(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
     
    /** Generierter Code zur Erstellung des GUI-Layouts. */
    private void initComponents() {

        catField = new JTextField();
        catLabel = new JLabel();
        matchesLabel = new JLabel();
        selectButton = new JButton();
        deselectButton = new JButton();
        matchesScrollPane = new JScrollPane();
        matchesList = new JList();
        selectedScrollPane = new JScrollPane();
        selectedList = new JList();
        selectedLabel = new JLabel();
        countSlider = new JSlider();
        imgCountLabel = new JLabel();
        separator = new JSeparator();
        scriptNameLabel = new JLabel();
        scriptNameField = new JTextField();
        generateButton = new JButton();
        pyInstalledCheckBox = new JCheckBox();
        qdpyInstalledCheckBox = new JCheckBox();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Montagsmaler - PyBuilder");

        catField.setFont(new Font("Consolas", 0, 18));
        catField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                catFieldKeyPressed(evt);
            }
        });

        catLabel.setFont(new java.awt.Font("Arial", 1, 12));
        catLabel.setText("Kategorie suchen");

        matchesLabel.setFont(new java.awt.Font("Arial", 1, 12));
        matchesLabel.setText("Verfügbar (" + getCategories().length + ")");

        selectButton.setFont(new java.awt.Font("Dialog", 1, 18));
        selectButton.setText("↓");
        selectButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectButtonMouseClicked(evt);
            }
        });

        deselectButton.setFont(new Font("Dialog", 1, 18));
        deselectButton.setText("↑");
        deselectButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deselectButtonMouseClicked(evt);
            }
        });

        matchesList.setBackground(new Color(220, 220, 220));
        matchesList.setFont(new Font("Consolas", 0, 18));
        matchesScrollPane.setViewportView(matchesList);
        matchesList.setModel(getDLM());

        selectedList.setBackground(new Color(220, 220, 220));
        selectedList.setFont(new Font("Consolas", 0, 18));
        selectedList.setMaximumSize(new Dimension(46, 88));
        selectedScrollPane.setViewportView(selectedList);
        matchesList.setModel(getDLM());

        selectedLabel.setFont(new Font("Arial", 1, 12));
        selectedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectedLabel.setText("Ausgewählt (0)");

        countSlider.setMajorTickSpacing(1000);
        countSlider.setMaximum(5000);
        countSlider.setMinimum(1000);
        countSlider.setPaintLabels(true);
        countSlider.setPaintTicks(true);
        countSlider.setSnapToTicks(true);
        countSlider.setValue(1000);

        imgCountLabel.setFont(new Font("Arial", 1, 12));
        imgCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgCountLabel.setText("Anzahl Bilder pro Kategorie");

        scriptNameLabel.setFont(new Font("Arial", 1, 12));
        scriptNameLabel.setText("Name:");

        scriptNameField.setFont(new Font("Consolas", 0, 12));
        scriptNameField.setText("script.py");

        generateButton.setFont(new Font("Arial", 1, 18));
        generateButton.setText("GENERIEREN");
        generateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generateButtonMouseClicked(evt);
            }
        });

        pyInstalledCheckBox.setSelected(true);
        pyInstalledCheckBox.setText("Python installiert?");

        qdpyInstalledCheckBox.setSelected(true);
        qdpyInstalledCheckBox.setText("quickdraw_python installiert?");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(selectedLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(matchesScrollPane)
                    .addComponent(catField)
                    .addComponent(selectedScrollPane, GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(selectButton, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                        .addComponent(deselectButton, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
                    .addComponent(countSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(imgCountLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(separator, GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(catLabel)
                            .addComponent(matchesLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scriptNameLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scriptNameField, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
                            .addComponent(pyInstalledCheckBox)
                            .addComponent(qdpyInstalledCheckBox))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(generateButton, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(catLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(matchesLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(matchesScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(selectButton)
                    .addComponent(deselectButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectedLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectedScrollPane, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(imgCountLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(countSlider, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(separator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(scriptNameLabel)
                            .addComponent(scriptNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pyInstalledCheckBox)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(qdpyInstalledCheckBox))
                    .addComponent(generateButton, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        pack();
        setLocationRelativeTo(null);
    }                      

    /** Methode zum Suchen der Kategorien. */
    private void catFieldKeyPressed(java.awt.event.KeyEvent evt) {                                    
        String[] cats = getCategories();
        String search = (catField.getText() + evt.getKeyChar()).trim();
        
        ArrayList<String> matches = new ArrayList<>();
        DefaultListModel dlm = new DefaultListModel();
        
        if(evt.isControlDown() || evt.isShiftDown() || evt.isAltDown()) return;
        if(evt.getKeyCode() == KeyEvent.VK_BACKSPACE){
            if(catField.getSelectedText() != null && catField.getSelectedText().length() == catField.getText().length()){
                matchesLabel.setText("Verfügbar (" + getCategories().length + ")");
                matchesList.setModel(getDLM());
                return;
            }
            try{
                search = search.substring(0, search.length()-1);
            } catch (StringIndexOutOfBoundsException e){
                return;
            }
        }
        for(String cat : cats){
            if(cat.toLowerCase().contains(search)){
                matches.add(cat);
                dlm.addElement(cat);
            }
        }
        
        matchesLabel.setText("Verfügbar (" + matches.size() + ")");
        matchesList.setModel(dlm);
    }                                   

    /** Hinzufügen einer ausgewählten Kategorie. */
    private void selectButtonMouseClicked(java.awt.event.MouseEvent evt) {                                          
        int[] selection = matchesList.getSelectedIndices();
        if(selection.length == 0) return;
        
        DefaultListModel dlm = new DefaultListModel();
        for(int i = 0; i < selectedList.getModel().getSize(); i++){
            dlm.addElement(selectedList.getModel().getElementAt(i));
        }
        
        for(int cat : selection){
            boolean ignore = false;
            for(int i = 0; i < selectedList.getModel().getSize(); i++){
                if(matchesList.getModel().getElementAt(cat).equals(selectedList.getModel().getElementAt(i))){
                    ignore = true;
                }
            }
            if(!ignore){
                dlm.addElement(matchesList.getModel().getElementAt(cat));
            }
        }
        selectedList.setModel(dlm);
        selectedLabel.setText("Ausgewählt (" + selectedList.getModel().getSize() + ")");
    }                                         

    /** Entfernen einer ausgewählten Kategorie. */
    private void deselectButtonMouseClicked(java.awt.event.MouseEvent evt) {                                            
        int[] selection = selectedList.getSelectedIndices();
        if(selection.length == 0) return;
        
        DefaultListModel dlm = new DefaultListModel();
        for(int i = 0; i < selectedList.getModel().getSize(); i++){
            dlm.addElement(selectedList.getModel().getElementAt(i));
        }
        
        for(int deselect : selection){
            dlm.setElementAt("", deselect); // geht gerade nicht besser :(
        }
        
        for(int i = 0; i < selectedList.getModel().getSize(); i++){
            dlm.removeElement("");
        }
        
        selectedList.setModel(dlm);
        selectedLabel.setText("Ausgewählt (" + selectedList.getModel().getSize() + ")");
        
    }                                           

    /** Skript generieren. */
    private void generateButtonMouseClicked(java.awt.event.MouseEvent evt) {                                            
        ArrayList<String> selected = new ArrayList<>();
        for(int i = 0; i < selectedList.getModel().getSize(); i++){
            selected.add(selectedList.getModel().getElementAt(i).toString());
        }
        if(selected.size() == 0){
            JOptionPane.showMessageDialog(null, "Es wurden keine Kategorien ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE, null);
            return;
        } else if(selected.size() > 20){
            JOptionPane.showMessageDialog(null, "Es wurden viele Kategorien ausgewählt.\nEs können größere Datenmengen anfallen.", "Achtung!", JOptionPane.WARNING_MESSAGE, null);
        }
        
        int imgCount = countSlider.getValue();
        String scriptName = scriptNameField.getText().trim();
        boolean pyInstalled = pyInstalledCheckBox.isSelected();
        boolean qdpyInstalled = qdpyInstalledCheckBox.isSelected();
        
        if(!pyInstalled){
            JOptionPane.showMessageDialog(null, "Zum Ausführen des Skripts wird Python benötigt.", "Python installieren", JOptionPane.WARNING_MESSAGE);
        }
        
        if(!qdpyInstalled){
            Object[] options = {"Link kopieren", "OK"};
            String url = "https://github.com/martinohanlon/quickdraw_python/";
            int clicked = JOptionPane.showOptionDialog(null, "Das Python-Modul \"quickdraw\" muss installiert sein.\n\nsiehe " + url, "quickdraw_python", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
            if(clicked == 0){
                StringSelection sel = new StringSelection(url);
                Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                cb.setContents(sel, sel);
                JOptionPane.showMessageDialog(null, "Link wurde in die Zwischenablage kopiert.", ":)", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        // ins Skript schreiben
        Writer w = null;
        try{
            w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(scriptName), "utf-8"));
            w.write("# Dieses Skript muss sich im quickdraw_python Ordner befinden.\n");
            w.write("# Dort muss auch ein leerer Ordner \"img\" erstellt werden, in dem die Bilder gespeichert werden.\n\n");
            w.write("from quickdraw import QuickDrawData\n\n");
            w.write("qd = QuickDrawData()\n\n");
            
            for(int i = 0; i < selected.size(); i++){
                w.write("c" + i + " = \"" + selected.get(i) + "\"\n");
            }
            
            w.write("\ncounter = 0\n");
            w.write("totalCount = " + imgCount + "\n\n");
            
            for(int i = 0; i < selected.size(); i++){
                w.write("while counter < totalCount:\n");
                w.write("   drawing = qd.get_drawing(c" + i + ")\n\n");
                w.write("   if(drawing.recognized):\n");
                w.write("       drawing.image.resize((28,28)).save(\"img/\" + c" + i + " + str(counter) + \".png\")\n");
                w.write("       counter += 1\n");
                w.write("   else:\n");
                w.write("       print(c" + i + " + str(counter) + \" wurde nicht erkannt.\")\n");
                w.write("counter = 0\n\n");
            }
            
            w.close();
            JOptionPane.showMessageDialog(null, "Skript erfolgreich generiert.\n\n(" + System.getProperty("user.dir") + "\\" + scriptName + ")", "Fertig!", JOptionPane.INFORMATION_MESSAGE);

        } catch (UnsupportedEncodingException ex) {
            JOptionPane.showMessageDialog(null, "UTF-8 wird nicht unterstützt. Schade aber auch.", ":(", JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Datei wurde nicht gefunden. Dateiname überprüfen.", ":(", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Datei konnte nicht beschrieben werden. IOException.", ":(", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                w.close();
            } catch (IOException ex) { }
        }
        
    }                                           
    
    /** Hauptmethode zum Anzeigen der GUI.
     * 
     * @param args Kommandozeilenargumente (hier nicht benötigt)
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PyBuilder dialog = new PyBuilder(new JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
                   
    public JTextField catField;
    public JLabel catLabel;
    public JSlider countSlider;
    public JButton deselectButton;
    public JButton generateButton;
    public JLabel imgCountLabel;
    public JLabel matchesLabel;
    public JList matchesList;
    public JScrollPane matchesScrollPane;
    public JCheckBox pyInstalledCheckBox;
    public JCheckBox qdpyInstalledCheckBox;
    public JTextField scriptNameField;
    public JLabel scriptNameLabel;
    public JButton selectButton;
    public JLabel selectedLabel;
    public JList selectedList;
    public JScrollPane selectedScrollPane;
    public JSeparator separator;               
    
    /** Standardmäßige Liste mit allen Kategorien zurückgeben.
     * 
     * @return komplette Liste
     */
    public static DefaultListModel getDLM(){
        DefaultListModel dlm = new DefaultListModel();
        String[] cats = getCategories();
        for(String cat : cats){
            dlm.addElement(cat);
        }
        return dlm;
    }
    
    /** Alle Kategorien als gesammelten String zurückgeben.
     * 
     * @return alle Kategorien als String
     */
    public static String getCategoriesAsString(){
        String[] cats = getCategories();
        String out = "";
        
        for(String cat : cats){
            out += cat + "\n";
        }
        
        return out;
    }
    
    /** Alle Kategorien in geordneter String-Array zurückgeben.
     * 
     * @return String-Array mit Kategorien
     */
    public static String[] getCategories() {
        String[] cat = {
            "aircraft carrier",
            "airplane",
            "alarm clock",
            "ambulance",
            "angel",
            "animal migration",
            "ant",
            "anvil",
            "apple",
            "arm",
            "asparagus",
            "axe",
            "backpack",
            "banana",
            "bandage",
            "barn",
            "baseball bat",
            "baseball",
            "basket",
            "basketball",
            "bat",
            "bathtub",
            "beach",
            "bear",
            "beard",
            "bed",
            "bee",
            "belt",
            "bench",
            "bicycle",
            "binoculars",
            "bird",
            "birthday cake",
            "blackberry",
            "blueberry",
            "book",
            "boomerang",
            "bottlecap",
            "bowtie",
            "bracelet",
            "brain",
            "bread",
            "bridge",
            "broccoli",
            "broom",
            "bucket",
            "bulldozer",
            "bus",
            "bush",
            "butterfly",
            "cactus",
            "cake",
            "calculator",
            "calendar",
            "camel",
            "camera",
            "camouflage",
            "campfire",
            "candle",
            "cannon",
            "canoe",
            "car",
            "carrot",
            "castle",
            "cat",
            "ceiling fan",
            "cell phone",
            "cello",
            "chair",
            "chandelier",
            "church",
            "circle",
            "clarinet",
            "clock",
            "cloud",
            "coffee cup",
            "compass",
            "computer",
            "cookie",
            "cooler",
            "couch",
            "cow",
            "crab",
            "crayon",
            "crocodile",
            "crown",
            "cruise ship",
            "cup",
            "diamond",
            "dishwasher",
            "diving board",
            "dog",
            "dolphin",
            "donut",
            "door",
            "dragon",
            "dresser",
            "drill",
            "drums",
            "duck",
            "dumbbell",
            "ear",
            "elbow",
            "elephant",
            "envelope",
            "eraser",
            "eye",
            "eyeglasses",
            "face",
            "fan",
            "feather",
            "fence",
            "finger",
            "fire hydrant",
            "fireplace",
            "firetruck",
            "fish",
            "flamingo",
            "flashlight",
            "flip flops",
            "floor lamp",
            "flower",
            "flying saucer",
            "foot",
            "fork",
            "frog",
            "frying pan",
            "garden hose",
            "garden",
            "giraffe",
            "goatee",
            "golf club",
            "grapes",
            "grass",
            "guitar",
            "hamburger",
            "hammer",
            "hand",
            "harp",
            "hat",
            "headphones",
            "hedgehog",
            "helicopter",
            "helmet",
            "hexagon",
            "hockey puck",
            "hockey stick",
            "horse",
            "hospital",
            "hot air balloon",
            "hot dog",
            "hot tub",
            "hourglass",
            "house plant",
            "house",
            "hurricane",
            "ice cream",
            "jacket",
            "jail",
            "kangaroo",
            "key",
            "keyboard",
            "knee",
            "knife",
            "ladder",
            "lantern",
            "laptop",
            "leaf",
            "leg",
            "light bulb",
            "lighter",
            "lighthouse",
            "lightning",
            "line",
            "lion",
            "lipstick",
            "lobster",
            "lollipop",
            "mailbox",
            "map",
            "marker",
            "matches",
            "megaphone",
            "mermaid",
            "microphone",
            "microwave",
            "monkey",
            "moon",
            "mosquito",
            "motorbike",
            "mountain",
            "mouse",
            "moustache",
            "mouth",
            "mug",
            "mushroom",
            "nail",
            "necklace",
            "nose",
            "ocean",
            "octagon",
            "octopus",
            "onion",
            "oven",
            "owl",
            "paint can",
            "paintbrush",
            "palm tree",
            "panda",
            "pants",
            "paper clip",
            "parachute",
            "parrot",
            "passport",
            "peanut",
            "pear",
            "peas",
            "pencil",
            "penguin",
            "piano",
            "pickup truck",
            "picture frame",
            "pig",
            "pillow",
            "pineapple",
            "pizza",
            "pliers",
            "police car",
            "pond",
            "pool",
            "popsicle",
            "postcard",
            "potato",
            "power outlet",
            "purse",
            "rabbit",
            "raccoon",
            "radio",
            "rain",
            "rainbow",
            "rake",
            "remote control",
            "rhinoceros",
            "rifle",
            "river",
            "roller coaster",
            "rollerskates",
            "sailboat",
            "sandwich",
            "saw",
            "saxophone",
            "school bus",
            "scissors",
            "scorpion",
            "screwdriver",
            "sea turtle",
            "see saw",
            "shark",
            "sheep",
            "shoe",
            "shorts",
            "shovel",
            "sink",
            "skateboard",
            "skull",
            "skyscraper",
            "sleeping bag",
            "smiley face",
            "snail",
            "snake",
            "snorkel",
            "snowflake",
            "snowman",
            "soccer ball",
            "sock",
            "speedboat",
            "spider",
            "spoon",
            "spreadsheet",
            "square",
            "squiggle",
            "squirrel",
            "stairs",
            "star",
            "steak",
            "stereo",
            "stethoscope",
            "stitches",
            "stop sign",
            "stove",
            "strawberry",
            "streetlight",
            "string bean",
            "submarine",
            "suitcase",
            "sun",
            "swan",
            "sweater",
            "swing set",
            "sword",
            "syringe",
            "t-shirt",
            "table",
            "teapot",
            "teddy-bear",
            "telephone",
            "television",
            "tennis racquet",
            "tent",
            "The Eiffel Tower",
            "The Great Wall of China",
            "The Mona Lisa",
            "tiger",
            "toaster",
            "toe",
            "toilet",
            "tooth",
            "toothbrush",
            "toothpaste",
            "tornado",
            "tractor",
            "traffic light",
            "train",
            "tree",
            "triangle",
            "trombone",
            "truck",
            "trumpet",
            "umbrella",
            "underwear",
            "van",
            "vase",
            "violin",
            "washing machine",
            "watermelon",
            "waterslide",
            "whale",
            "wheel",
            "windmill",
            "wine bottle",
            "wine glass",
            "wristwatch",
            "yoga",
            "zebra",
            "zigzag"
            };

        return cat;
    }

}

