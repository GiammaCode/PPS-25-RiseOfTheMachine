# Sistema di Visualizzazione - CLIView

## Descrizione
Il modulo `CLIView`, contenuto in `ViewModule.scala`, rappresenta la **vista testuale** del gioco *Rise of the Machine*.
Il sistema di visualizzazione del progetto è costruito per separare la logica della presentazione (formattazione 
dell’output) dalla logica dell’interfaccia utente. 

Questo approccio migliora modularità, estendibilità e testabilità del codice.
L'architettura segue il principio di Single Responsibility: ogni componente ha un ruolo ben definito. 
La view (CLIView) gestisce il cosa mostrare, mentre il formatter (CLIFormatter) si occupa del come.


---

## Aspetti Implementativi

- Utilizza il pattern **strategy-view**, separando la logica del gioco dalla presentazione.
- Mixin-based composition: tramite trait Formatter, le funzionalità sono aggiunte dinamicamente
alle view (CLIView).
- Delegation: CLIView implementa Formatter e delega le chiamate al modulo
CLIFormatter.
- Modular separation: il modulo di formattazione (CLIFormatter) è indipendente dalla view.

Interface abstraction: grazie al trait Formatter, è possibile creare più implementazioni alternative 
(TestFormatter, GUIFormatter, ecc.).

Esempio di una possibile implementazioni in caso di una GUI.
```scala
object GUIFormatter extends Formatter:
  override def printBoxedContent(...) = renderInSwing(...)
  //continue of code

object GUIView extends GameView with Formatter:
  // delega a GUIFormatter invece di CLIFormatter
```
---

## Funzioni principali

Funzionalità principali di ViewModule 

| Metodo                 | Descrizione                                                                   |
|------------------------|-------------------------------------------------------------------------------|
| `renderGameModeMenu()` | Mostra il menu principale e gestisce la selezione della modalità e difficoltà |
| `renderGameTurn()`     | Mostra la mappa, lo stato del gioco e chiede le azioni a IA e giocatore       |
| `renderEndGame()`      | Mostra la schermata di fine gioco con il conseguente vincitore                |


Funzionalità principali della util CLIFormatter

| Metodo                | Descrizione                                                               |
|-----------------------|---------------------------------------------------------------------------|
| `printBoxedMenu()`    | Formatta il contenuto in modalità boxed e numera le il body del contenuto |
| `printBoxedContent()` | Formatta il contenuto in modalità boxed                                   |
| `printMap()`          | Formatta una mappa di caratteri stampandoli a griglia                     |
| `printAsciiTitle`     | Formatta il testo in Ascii con font selezionato                           |


---

## Diagramma dei componenti
  ```mermaid
  classDiagram
    direction TB

    class Formatter {
        +printBoxedContent(title, body)
        +printBoxedMenu(title, options)
        +printAsciiTitle(text)
        +printMap(map, conquered)
    }

    class CLIFormatter {
        +printBoxedContent(title, body)
        +printBoxedMenu(title, options)
        +printAsciiTitle(text)
        +printMap(map, conquered)
    }

    class GameView {
        +renderGameModeMenu(): GameSettings
        +renderGameTurn(worldState): GameTurnInput
        +renderEndGame(winner): Unit
    }

    class CLIView {
        +renderGameModeMenu(): GameSettings
        +renderGameTurn(worldState): GameTurnInput
        +renderEndGame(winner): Unit
    }

    CLIFormatter --|> Formatter
    CLIView --> CLIFormatter: delega
    CLIView --|> GameView: estende
    CLIView --|> Formatter
```

