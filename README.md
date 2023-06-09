# Software-Challenge-Client
## des Teams Gym Eckhorst Q2

Der Client benutzt Gradle als Build-System und funktioniert mit den Serverversionen 23.x.x.
Mittlerweile wurde dieser durch einen in Rust geschriebenen Client mit identischer Logik und bis auf die Programmiersprachensyntax nahezu identischem Code sowie leicht verbesserten Tests abgelöst, welcher durch die bessere Performance bessere Züge berechnen kann.

## Übersicht

### Testspiele

Neben den Unit Tests lässt sich der Client auch gut testen, indem eine neuere Version mit einer älteren verglichen wird. Hierzu ändert man meistens die Funktion Logic.createNewCombinedRater, welche man dann gegen die bisherige Rating-Funktion testen kann. Dazu wird die main-Methode in ComparisonProgram ausgeführt.

### MoveGetter

Der MoveGetter ist ein Interface, welches dazu dient, Zugsuchalgoritmen oder Varianten dieser austauschbar und folglich gegeneinander ausspielbar zu machen. Ein Zugsuchalgoritmus wird dann unabhängig von der Heuristik implementiert. So ließen sich beispielsweise Principal Variation Search mit und ohne Aspiration-Optimierung leicht gegeneinander testen.

### Rater

Der Rater ist ein Interface für Bewertungsfunktionen von GameStates. Die Strategie zum Berechnen von Zügen wird hauptsächlich mit Ratern realisiert. Durch den WeightedRater und den CompositeRater können mehrere Rater mit einander kombiniert werden.

### PossibleMoveGenerator

Im Gegensatz zu einem normalen Minimax-Algoritmus führt die Principal Variation Search nicht eine, sondern zwei Suchen pro Zug aus: eine ungenaue, aber schnelle Nullfenstersuche und wenn das Ergebnis dieser im normalen Suchfenster liegt, dann eine genaue Suche. Das bedeutet, dass die Suche schneller läuft, wenn das Suchfenster schnell verkleinert wird. Um dies zu erreichen, sollten möglichst gute Züge zuerst ausprobiert werden. Um dies zu erreichen, wurden verschiedene Ansätze erprobt. Um diese leicht auswechselbar zu machen wurde das Interface PossibleMoveGenerator eingeführt.

