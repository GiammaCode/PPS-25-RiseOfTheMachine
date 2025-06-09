---
layout: default
title: 3° sprint
nav_order: 11
---

# third Sprint Planning – 08/05/2025

Il team si è incontrato in loco per rivedere i risultati del secondo sprint e pianificare le attività successive.  
Durante l'incontro è stato deciso il rilascio della seconda release: **V2.0.0**.

**Durata dell'incontro:** circa 2 ore
---

## Obiettivi dello Sprint

- Aggiunta della modalità multiplayer
- Implementazione della strategia per il giocatore Human in modalità singleplayer
- Introduzione della selezione delle modalità di gioco (singleplayer/multiplayer)
- Introduzione della selezione della difficoltà (easy, normal, hard)

---

## Planning e Comunicazione

- Incontri frequenti, sia in presenza che online
- Collaborazione diretta su codice e decisioni progettuali
- Comunicazione continua e confronto immediato

**Deadline sprint:** 22/05/2025

---

## Divisione del Lavoro

### Gianmaria Casamenti

- Sviluppo del sistema di strategia per il **giocatore Human** in modalità singleplayer  
  Il sistema adatta il comportamento in base alla difficoltà:
    - **Easy**: strategia passiva
    - **Normal**: strategia casuale
    - **Hard**: strategia proattiva
- Miglioramento della **view** con l'aggiunta della gestione multiplayer e del menù di gioco

### Lorenzo Leoni

- Aggiunta del supporto multiplayer all'**InputHandler** e creazione delle **turnAction** generiche
- Introduzione di `GameSettings` per la gestione della difficoltà e dei GameMode.
- Aggiunta del supporto a **ScalaTest** e generazione del **fat JAR**

### Luca Pasini

- Implementazione della logica multiplayer all'interno del **controller**
- Gestione della possibilità di fallimento delle azioni all'interno del controller
- Creazione di test tramite **ScalaTest** per i moduli `city`, `map` e `controller`

---

## Sprint Review – 21/05/2025

### Risultati ottenuti

Il team ha raggiunto tutti gli obiettivi previsti.  
Il sistema risulta attualmente **giocabile** e con le modifiche sostanziali apportate in 
questo sprint si procederà con il rilascio della nuova versione **3.0.0**.
