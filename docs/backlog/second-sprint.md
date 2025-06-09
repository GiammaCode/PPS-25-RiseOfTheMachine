---
layout: default
title: 2° sprint
nav_order: 10
---

# Second Sprint Planning – 30/04/2025

Il team si è incontrato in loco per rivedere le specifiche del progetto e
definire i task del **secondo sprint**.  
**Durata:** 2 ore

---

## Obiettivi Sprint
Rilasciare una versione del gioco funzionante,
dando al committente la possibilità di provare le core feature del videogioco(successione di turni di gioco dove
si possono svolgere azioni con conseguenze sulla mappa)
permettendo al team di sviluppo in futuro di non dipendere in maniera stringente gli uni dagli altri fornendo la
possibilità di runnare un applicazione funzionante e ottenendo i relativi feedback.
Tutto questo tralasciando piccoli dettagli che verranno trattati nelle prossime sprint


---

## Planning & Comunicazione
- Incontri frequenti, sia in presenza che online
- Collaborazione diretta su codice e decisioni progettuali
- Comunicazione continua e confronto immediato

**Deadline sprint:** 12/05/2025
---

## Divisione del Lavoro


### Gianmaria Casamenti
- Aggiornamento e rifattorizzazione del codice inerente all'entità human player
- Sviluppo della view funzionante Mappa e azioni con turni
- Aggiornamento e rifattorizzazione del codice inerente allo stato di gioco

### Lorenzo Leoni
- Gestione dell'effetto delle azioni da parte del PlayerAi.
- Creazione delle abilità sbloccabili dal giocatore-
- Aggiunta dei controlli all'interno dell'inputHandler per trasformare input in azioni valide e gestire tutte le azioni.

### Luca Pasini

- Sviluppo del controller con relativo turno che effettivamente svolge e azioni di AI e Human
- Inserimento di worldState nel controller, utilizzandolo per modificare lo Stato del gioco
- Aggiornamento degli algoritmi di crezione della mappa con relativi given e tests

---

## Sprint Review – 08/05/2025

Si è deciso di chiudere prima la sprint perché il risultato della sprint era stato raggiunto, grazie a un aumento delle 
ore di lavoro del team 

### Risultati ottenuti:
Il team ha raggiunto tutti gli obiettivi previsti.
Il sistema attualmente simula un serie di turni di gioco che modificano lo stato del gioco e quindi la mappa, rendondolo
il core del gioco funzionante e usabile.


