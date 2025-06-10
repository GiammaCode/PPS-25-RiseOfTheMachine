---
layout: default
title: Requisiti
nav_order: 3
---
# **Requisiti**

## **Business**
**Requisiti di Business**: Il sistema dovrà consentire il gioco di “Rise of the Machine”.

Sono stati individuati i seguenti requisiti di business: 
- Il gioco dovrà avere un menù di inizio e fine partita.
- Sarà possibile giocare una partita intera a “Rise of the Machine”
- Sarà possibile visualizzare la mappa di gioco e interagire con esso.
- Fornire un'esperienza di gioco a turni bilanciata e strategia, in cui l'IA e l'umanità (NPC) si affrontano 
in un ciclo di azioni e difese, gestendo una mappa dinamica

## **Funzionali**

Il gioco "Rise of the Machine" si compone di un insieme di entità e regole ben definite. Di seguito sono elencati i principali requisiti funzionali:

- Le entità in gioco sono:
    * AI Player
    * Human Player
    * Città (normali o capitali)

- La mappa di gioco è strutturata come una griglia quadrata, composta da diverse città disposte spazialmente.

- Ogni turno è composto da due fasi:
  * Fase dell’AI Player: il giocatore seleziona un’azione da eseguire
  * Fase dell’Human Player: un NPC rappresenta l’umanità e difende le città o lavora allo sviluppo del KillSwitch

- Ogni città possiede un livello di difesa. Le città capitali hanno un peso strategico maggiore.

- Le azioni disponibili per l’AI Player sono:
  * Infect: tenta di conquistare una città
  * Sabotage: riduce la difesa di una città
  * Evolve: consente di sbloccare nuove abilità offensive

- Le azioni disponibili per l’Human Player sono:
    * CityDefense: aumenta la difesa di una città specifica
    * GlobalDefense: incrementa la difesa globale (meno potente ma applicabile ovunque)
    * DevelopKillSwitch: sviluppa una cura definitiva per fermare l’AI

- Ogni azione presenta una probabilità di successo, influenzata dal livello di difficoltà e dai parametri di gioco.

- L’AI può ottenere abilità speciali tramite l’azione Evolve.

- Il gioco termina quando:
  * L’AI conquista almeno il 90% delle città o tutte e 3 le capitali
  * L’umanità completa il KillSwitch al 100%

- A ogni turno il sistema deve visualizzare:
  * Lo stato attuale della mappa
  * La percentuale di città infette
  * Il progresso del KillSwitch
  * Le azioni eseguite nei turni precedenti

## **Utente**

L'utente deve poter:
- Avviare una partita.
- Selezionare modalità e difficoltà
- Eseguire azioni sulla mappa
- Vedere le statistiche a fine partita e riavviare il gioco.

## **Non Funzionali**

I requisiti non funzionali individuati per il progetto sono:
- Realizzazione di software estendibile e rivisitabile.

- Il gioco dovrà rispondere in meno di 1 secondo in un'architettura con processore intel core i7 8th generazione 1.80 GHz e ram da 8GB 

- Realizzazione di un'esperienza di gioco godibile (usabilità, bilanciamento). 


- Breve tutorial integrato che spieghi le regole, le azioni disponibili e le condizioni di vittoria.

## **Implementativi**

- **Tecnologie:** Sviluppo in **Scala**. Utilizzo di **GitHub** per il repository e le **GitHub Actions** per 
automazione, **SBT** per la build e **IntelliJ IDEA** come ambiente di sviluppo.


- **Testing:** Implementazione di **test automatizzati** per ogni entità sviluppata, coprendo più casi possibili. 
Utilizzo di **ScalaTest** e **JUnit**.

## **Opzionali**

Sono stati individuati dei requisiti non obbligatori del progetto ma che tuttavia accrescerebbe il valore del prodotto finale:
- Possibilità di giocare con un amico alla modalità multiplayer 

- Inserimento delle difficoltà per rendere il gioco più avvincente
