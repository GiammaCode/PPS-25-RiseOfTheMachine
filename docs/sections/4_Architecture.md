# Architettura

## Pattern Architetturale
L'architettura adottata per il nostro gioco a turni è il pattern **MVC (Model-View-Controller)**, uno dei più diffusi per separare la logica di business, la gestione dell'interfaccia utente e il controllo dell'interazione utente.

- **Model**: rappresenta la logica del gioco, comprese le regole, lo stato dei turni, i giocatori e l’evoluzione del gioco.
- **View**: si occupa della visualizzazione delle informazioni al giocatore. Attualmente implementata tramite CLI.
- **Controller**: gestisce l'interazione dell'utente, interpreta gli input e invoca i metodi appropriati sul modello.

Questa separazione consente un'elevata manutenibilità, testabilità e la possibilità di cambiare la vista senza toccare la logica del gioco.

## Architettura Complessiva


### Vantaggi

- Separazione chiara delle responsabilità.
- Possibilità di testare ogni componente in modo isolato.
- Flessibilità nell'aggiunta di nuove viste o modalità di input.