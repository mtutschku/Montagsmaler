# 19

Tutor Niklas Friedrich

Morris Tutschku:
	W1: In der ersten Woche wurde direkt ein nicht-lineares Netzwerk implementiert, das mittels supervised learning trainiert wird. Die Gewichte des Netzwerks werden durch back-propagation angepasst. Gemeinsam mit Moritz Klose wurden die dazu nötigen Methoden entwickelt und verfeinert. Eine Data-Klasse wurde erschaffen, mit deren Inhalte das supervised learning vereinheitlicht wurde.

	W2: Die Beschaffung der Trainingssets für das Netzwerk wurde angefangen. Eine Verbindung zwischen Googles "quickdraw" und unserem Projekt wurde durch python ermöglicht. Statistiken des Netzwerks (Genauigkeit, Schnelligkeit etc.) wurden implementiert und dienten als Kennzahlen, wie effizient das Netzwerk ist.

	W3: Mit der PyBuilder-Klasse konnte man nach Belieben Kategorien und gewisse Parameter auswählen, mit denen das endgültige Netzwerk trainiert werden soll. Erste Testläufe des Netzwerks wurden durchgeführt. Infolgedessen wurde(n) kleinere Bugs behoben, Code optimiert und Feintuning betrieben. Die ersten Trainingssets wurden vorm Trainieren noch zusätzlich bearbeitet, damit das Netzwerk bessere Ergebnisse erzielt (u. a. algorithmisch zentriert).

	W4: Fehlerbehebungen an der Schnittstelle (Translator), da das Canvas nicht sauber in eine Matrix umgewandelt wurde. Arbeiten an der Training-Klasse, die weitere relevante Methoden für das Netzwerk enthält. Die Klasse enthält gemeinsam mit der PreTrained-Klasse die für das letztendlich verwendete Netzwerk nötigen Matrizen. Erste Integration eines hardgecodeten Netzwerks ins Programm wurde durchgeführt und getestet.

	W5: Verschiedene Netzwerke mit unterschiedlichen Kategorien wurden ausprobiert. Es wurden einige Kategorien ausgewählt, die das Netzwerk zuverlässig erkennen kann. Kleine Bugfixes und Codeoptimierungen. Vorbereitungen zur Präsentation des Programms wurden angefangen.

	W6: Netzwerk trainiert, verschiedene Werte für NW-Parameter ausprobiert. Präsentation vorbereitet.

Moritz Klose:
	W1: Um die vollständige Funktion des neuronalen Netzwerkes zu erhalten waren Vorarbeiten nötig. Dabei war besonders die mathematische Grundlage von großer Bedeutung.
	Den wichtigsten Punkt stellen hierbei Matrix Rechenoperationen dar um die Gewichte des Netzwerks zu bestimmten. Diese wurden dann in den Aufbau des Netzwerks eingebettet um ein nicht-lineares Netzwerk zu erhalten. 

	W2: In dem Projekt kommt für die GUI das Package JavaFX zum Einsatz. Damit das Spiel auch für außenstehende spielbar ist, wurde das gesamte Projekt umstrukturiert und in die Form eines Gradle-Projekts gebracht. Das Build File von Gradle ermöglicht es externe Dependencies (wie JavaFX, später auch Javazoom) automatisch bei der Ausführung des Programms herunterzuladen. Dies macht das Programm wesentlich anwenderfreundlich, da keine Packages per Hand zugefügt werden müssen.

	W3: Damit der Lernfortschritt des Netzwerkes verfolgt werden kann wurde eine graphische Darstellung über JavaFX erstellt. Hierbei wurde die Genauigkeit des Netzwerk über die Epochen dargestellt. Die Grafik zeigt ein nicht-lineares Netzwerk. In der finalen Form des Projekts wird diese Grafik nicht mehr dargestellt werden. Sie diente lediglich zur Überprüfung und zur Visualisierung des Lernfortschritts.
	
	W4: Die graphische Darstellung des Netzwerks wurde in die GUI eingebettet, damit bei Aufruf der GUI das zweite Fenster für die Grafik zeitgleich geöffnet wird. Es wurden zwei sogenannte Stages für die GUI und die Grafik implementiert. Dies verhindert Fehler in der Ausführung der beiden Fenster.

	W5: Als Erweiterung der GUI wurde in dieser Woche eine Sprachausgabe implementiert. Diese gibt das Wort aus, wenn das Netzwerk das dazugehörige Bild richtig erkannt hat. Die Ausgabe wurde mithilfe des JavaZoom Packages realisiert. Damit auch hier keine externen Packete per Hand in das Projekt eingefügt werden müssen, kommt auch hier das Build-File von Gradle zum Einsatz und lädt bei Ausführung des Programms die benötigten Packages automatisch herunter.

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

	W6: Es wurden letzte Optimierungen und Anpassungen des GUI Codes vorgenommen. Des Weiteren wurde die Präsentation des Projekts vorbereitet und der Abschluss eben dessen besprochen.


Jakob Hiestermann:
	W1: Die Grundidee des Translators wird implementiert, es soll von der GUI ein Canvas übergeben werden, welches ausgelesen und in eine Matrix übersetzt werden. Außerdem wird in Meta.java eine Arrayliste erstellt, die der GUI die Rückübersetzung von Matrix in Begriff liefert.

	W2: Arbeit an der Mainfunktion; Netzwerk und Translator werden zu diesem Zeitpunkt initailisert und es wird versucht die Bilddaten aus der GUI zu extrahieren. Dieser Ansatz wird später verworfen (-> W4).

	W3: Es sind weitere Anpassungen in der Main notwendig, das Auslesen eines Bildes aus einer Datei funktioniert nicht wie gewünscht. Der Translator wird für die Verarbeitung eines BufferedImage umgeschrieben. Die Matrixklasse wird erweitert mit Funktionen, mit denen die höchsten Werte und deren Position angefordert werden können. 

	W4: Main entsprechend neuem Ansatz umgeschrieben, die gelaunchte GUI bekommt einen Translator und Netzwerk übergeben und das Programm arbeitet ab dann innerhalb der start-Methode der GUI. Die Hauptaufgabe des Translators konnte nun erfolgreich getestet werden. Icon zur GUI hinzugefügt.

	W5: Da das Netzwerk Schwierigkeiten hat, kleine und nicht zentrierte Zeichnungen zu erkennen, muss der Translator noch das von der GUI übergebene BufferedImage nach der Zeichnung absuchen. Dies wird in der Methode subtractEmpty + Hilfsmethoden umgesetzt.

	W6:  Polishing und Bugfixing. Die von mir auf englisch verfasste Javadoc wird zu deutsch übersetzt. Außerdem verfasse ich die Programmanleitung und schließe den Bericht mit fehlenden Infos zu allgemeiner Vorgehensweise und Grundgedanken ab (beides in dieser README).
