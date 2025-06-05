---
layout: default
title: Design
nav_order: 5
---

# Design di Dettaglio

```mermaid
graph TD
subgraph "MODEL"
GameState[Game State<br/>- Mappa<br/>- Giocatori<br/>- Difficoltà<br/>- Turno]
GameLogic[Game Logic<br/>- Regole di gioco<br/>- Azioni<br/>- Validazione]
Data[Data<br/>- Città<br/>- Configurazione<br/>- Stato immutabile]
end

    subgraph "VIEW"
        CLI[CLI Interface<br/>- Menu<br/>- Input utente<br/>- Output formattato]
        Display[Display<br/>- Rendering stato<br/>- Messaggi<br/>- ASCII art]
    end

    subgraph "CONTROLLER"
        GameController[Game Controller<br/>- Ciclo di gioco<br/>- Coordinamento turni<br/>- Gestione azioni]
        InputHandler[Input Handler<br/>- Parsing comandi<br/>- Validazione input<br/>- Gestione errori]
    end

    %% MVC Interactions
    CLI -->|Input utente| InputHandler
    Display -->|Richiesta dati| GameController
    
    GameController -->|Aggiorna stato| GameState
    GameController -->|Esegue logica| GameLogic
    GameController -->|Legge dati| Data
    
    GameController -->|Invia dati| Display
    InputHandler -->|Comandi validati| GameController

    %% Internal connections
    GameState -.-> GameLogic
    GameLogic -.-> Data

    %% Styling
    classDef model fill:#e8f5e8,stroke:#4caf50,stroke-width:2px
    classDef view fill:#e1f5fe,stroke:#2196f3,stroke-width:2px
    classDef controller fill:#f3e5f5,stroke:#9c27b0,stroke-width:2px

    class GameState,GameLogic,Data model
    class CLI,Display view
    class GameController,InputHandler controller
```

## Design del Model
Il model rappresenta la logica di gioco, le sue regole, lo stato e l'evoluzione complessiva. Il suo design è 
fortemente incentrato sui principi della programmazione funzionale e sull’immutabilità, garantendo prevedibilità e 
consistenza dello stato di gioco. Ogni modifica allo stato o una sua componente restituisce una nuova istanza 
preservando anche la lista degli stati precedenti.

1) Lo stato globale del gioco è gestito da un componente centrale che incapsula la mappa, i giocatori, 
la difficoltà e il turno. Per proteggere la sua struttura interna è stato implementato come opaque types e 
permette l’accesso solo tramite i metodi di estensione.
2) Le azioni sono fortemente tipate, in modo da essere sicuri che a compile-time si passi sempre l’azione giusta 
nel caso di AI o Human.


Tutte queste scelte contribuiscono a manutenibilità, estendibilità e testabilità.

## Design della View
La view si occupa di mostrare le informazioni di gioco al player e della raccolta dell’input. Il suo design è
stato pensato per essere disaccoppiato dalla logica di gioco, predispone i metodi al controller per la renderizzazione 
degli elementi di gioco.

1) Per garantire coerenza visiva e ridurre la duplicazione del codice, la view utilizza un modulo di Utils dedicato 
alla formattazione degli elementi stilizzati come menù, blocchi informativi e titoli in ASCII art.


Questo design supporta l’usabilità fornendo un’interfaccia chiara e interattiva.

## Design del Controller
Il GameController rappresenta il fulcro della logica applicativa, responsabile della gestione dell’intero ciclo di gioco.
Inizializza i componenti principali, coordina le interazioni tra model e view, e gestisce l’alternanza dei turni (prima
l’azione dell’IA, seguita da quella dell’utente).
L’intero flusso di gioco è modellato in modo funzionale e immutabile tramite la monade State, che permette di mantenere
e aggiornare lo stato globale del gioco senza effetti collaterali, favorendo così una maggiore prevedibilità e facilità 
di test.
Il componente InputHandler è incaricato di interpretare e validare gli input provenienti dall’interfaccia utente. 
Si occupa di trasformare tali input in azioni concrete, verificando la correttezza semantica delle scelte e gestendo 
eventuali errori di parsing o input non validi.
Il design modulare e funzionale del controller è fondamentale per garantire la robustezza, la manutenibilità e 
la testabilità del sistema.



