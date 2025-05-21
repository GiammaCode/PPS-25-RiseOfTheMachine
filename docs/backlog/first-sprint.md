# First Sprint Planning – 16/04/2025

Il team si è incontrato in loco per rivedere le specifiche del progetto e 
definire i task del **primo sprint**.  
**Durata:** 2 ore

---

## Scelte Tecnologiche Iniziali

- Il team ha deciso di **non concentrarsi inizialmente sull'interfaccia grafica**.
- La prima versione sarà **eseguita da terminale (CLI)**, stampando la griglia della partita e ricevendo input da tastiera per le mosse.
- L'intero sviluppo verrà realizzato in modo da **poter facilmente aggiungere una GUI in futuro**.

---

## Obiettivi Sprint

- Setup del progetto con tecnologie selezionate: GitHub, GitHub Actions, SBT, IntelliJ IDEA
- Creazione del **model basilare** per:
    - Mappa di gioco
    - Entità: **AI Player** e **Human Player**
    - Stato della partita
- Implementazione del **controller/game loop** basilare
- Prima versione della **view da CLI**

---

## Planning & Comunicazione

- Incontri frequenti, sia in presenza che online
- Collaborazione diretta su codice e decisioni progettuali
- Comunicazione continua e confronto immediato

**Deadline sprint:** 30/04/2025

---

## Divisione del Lavoro

### Gianmaria Casamenti
- Sviluppo a partire dai contratti definiti da L. Leoni per l’entità `PlayerHuman`
- Implementazione azioni eseguibili dall’utente
- Sviluppo della view da terminale
- Prima versione dello stato di gioco

### Lorenzo Leoni
- Sviluppo delle **interfacce comuni**
- Implementazioni per l’**AI Player**
- Sviluppo iniziale di un **input handler**

### Luca Pasini
- Sviluppo della **mappa di gioco**
- Definizione di una **struttura di città**
- Studio degli algoritmi per la generazione della mappa

**Nota:** Per ogni entità sviluppata devono essere implementati i **test automatizzati**, coprendo più casi possibili.

---

## Sprint Review – 29/04/2025

### Risultati ottenuti:

- Setup del progetto completato
- Model basilare della mappa di gioco creato
- Model basilare delle principali entità di gioco completato
- Prima versione della view da CLI realizzata

Abbiamo deciso di concludere il primo sprint con una **release compilabile** per avere una base stabile su cui iterare.
