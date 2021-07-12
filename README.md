# 19

Tutor Niklas Friedrich

Morris Tutschku:
	W1: Grundgerüst neuronales Netzwerk, Matrix-Stuff, Data-Klasse
	W2: Training.java: Trainieren des Netzwerks mit Trainingssets von quickdraw
	W3: PyBuilder.java: Automatisches Python-Skript erstellen mit bestimmten Trainingssets
	W4: Fehlerbehebung Schnittstelle, Training.java erweitert, PreTrained.java erstellt: Trainiertes Netzwerk kann integriert werden
	W5: Verschiedene Netzwerke mit unterschiedlichen Kategorien trainiert. Letztendliches Netzwerk steht noch aus. 

Moritz Klose:
	W1: Arbeit an der Network- und der Matrix-Klasse 
	W2: Einbindung von Gradle 
	W3: Visualisierung des Neuronalen Netzes 
	W4: Netzworkplot in die GUI integriert. Erstellung von zwei Stages für GUI und Plot.
	W5: Speech.java: Methode zur Sprachausgabe des, von dem Netzwerk, erratenen Wortes.

Pascal Uhlendorff: 
	W1: Die GUI in ihrer Basisform per Hand mit JavaFX implementiert. Das Grundgerüst samt Buttons und Labels aufgestellt. 
	Zeichnen und radieren sind bereits möglich. Außerdem kann die Zeichenfläche zurückgesetzt werden und 
	es kann die zu zeichnende Sache per Knopfdruck weitergeschaltet werden, sollte der Anwender die aktuell geforderte Sache nicht zeichnen können/wollen.
						
	W2: GUI weiter überarbeitet und zusätzliche Funtkionen hinzugefügt. Die GUI beinhaltet nun einen maximale Zeit die zum Zeichnen pro Sache 
	zur Verfügung steht und schaltet bei Ablauf dieser Zeit weiter zur nächsten Sache. Hinzu kommen die Implementierung einer Anzeige für die Anzahl der Versuche 
	und die Anzahl der maximalen Versuche. 
						
	W3: Kleinere GUI-Anpassungen und Bug-Fixes. Einarbeitung in Scene Builder und Vorbereitung des Outputs für den Handler. Das Raten als Placeholder implementiert.

	W4: Hinzufügen der korrekten Rate-Funktionalität in der GUI. Dafür wurde die GUI mit dem Handler und dem Netzwerk verbunden und der Event Handler für das Loslassen der
	Maustaste erweitert. Außerdem wurde ein Popup erstellt, welches die richtige Vermutung anzeigt und einen Prozess zum Weiterschalten/Zurücksetzen anstößt.

	W5: Den Code in der GUI-Klasse optimiert und mit zusätzlichen Kommentaren versehen. Weitere Bugs behoben, die zu doppeltem Raten und Überspringen von Wörtern führten. 
	Eine kleine Ergänzung, um am Ende anzuzeigen, wie genau das Netzwerk geraten hat und wie viele Sachen vom Netzwerk erkannt wurden.


Jakob Hiestermann:
	W1: Die Grundidee des Translators wird implementiert, es soll von der GUI ein Canvas übergeben werden, welches ausgelesen und in eine Matrix übersetzt werden. Außerdem wird in Meta.java eine Arrayliste erstellt, die der GUI die Rückübersetzung von Matrix in Begriff liefert.

	W2: Arbeit an der Mainfunktion; Netzwerk und Translator werden zu diesem Zeitpunkt initailisert und es wird versucht die Bilddaten aus der GUI zu extrahieren. Dieser Ansatz wird später verworfen (-> W4).

	W3: Es sind weitere Anpassungen in der Main notwendig, das Auslesen eines Bildes aus einer Datei funktioniert nicht wie gewünscht. Der Translator wird für die Verarbeitung eines BufferedImage umgeschrieben. Die Matrixklasse wird erweitert mit Funktionen, mit denen die höchsten Werte und deren Position angefordert werden können. 

	W4: Main entsprechend neuem Ansatz umgeschrieben, die gelaunchte GUI bekommt einen Translator und Netzwerk übergeben und das Programm arbeitet ab dann innerhalb der start-Methode der GUI. Die Hauptaufgabe des Translators konnte nun erfolgreich getestet werden. Icon zur GUI hinzugefügt.

	W5: Da das Netzwerk Schwierigkeiten hat, kleine und nicht zentrierte Zeichnungen zu erkennen, muss der Translator noch das von der GUI übergebene BufferedImage nach der Zeichnung absuchen. Dies wird in der Methode subtractEmpty + Hilfsmethoden umgesetzt.

	W6: 
