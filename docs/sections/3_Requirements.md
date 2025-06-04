---
layout: default
title: Requisiti
nav_order: 3
---
# **Requisiti**

## **Business**
**Requisiti di Business**: Il sistema dovrà consentire il gioco di “Rise of the Machine”.

Sono stati individuati i seguenti requisiti di business: 
1) Il gioco dovrà avere un menù di inizio e fine partita.
2) Sarà possibile giocare una partita intera a “Rise of the Machine”
3) Sarà possibile visualizzare la mappa di gioco e interagire con esso.
4) Fornire un'esperienza di gioco a turni bilanciata e strategia, in cui l'IA e l'umanità (NPC) si affrontano 
in un ciclo di azioni e difese, gestendo una mappa dinamica

## **Funzionali**

Il gioco "Rise of the Machine" si compone di un insieme di entità e regole ben definite. Di seguito sono elencati i principali requisiti funzionali:

1) Le entità in gioco sono:
   - AI Player
   - Human Player
   - Città (normali o capitali)

2)  La mappa di gioco è strutturata come una griglia quadrata, composta da diverse città disposte spazialmente.

3) Ogni turno è composto da due fasi:
   - Fase dell’AI Player: il giocatore seleziona un’azione da eseguire
   - Fase dell’Human Player: un NPC rappresenta l’umanità e difende le città o lavora allo sviluppo del KillSwitch

4) Ogni città possiede un livello di difesa. Le città capitali hanno un peso strategico maggiore.

5) Le azioni disponibili per l’AI Player sono:
   - Infect: tenta di conquistare una città
   - Sabotage: riduce la difesa di una città
   - Evolve: consente di sbloccare nuove abilità offensive

6) Le azioni disponibili per l’Human Player sono:
   - CityDefense: aumenta la difesa di una città specifica
   - GlobalDefense: incrementa la difesa globale (meno potente ma applicabile ovunque)
   - DevelopKillSwitch: sviluppa una cura definitiva per fermare l’AI

7) Ogni azione presenta una probabilità di successo, influenzata dal livello di difficoltà e dai parametri di gioco.

8) L’AI può ottenere abilità speciali tramite l’azione Evolve.

9) Il gioco termina quando:
   - L’AI conquista almeno il 90% delle città o tutte e 3 le capitali
   - L’umanità completa il KillSwitch al 100%

10) A ogni turno il sistema deve visualizzare:
    - Lo stato attuale della mappa
    - La percentuale di città infette
    - Il progresso del KillSwitch
    - Le azioni eseguite nei turni precedenti

## **Utente**

L'utente deve poter:
1) Avviare una partita.
2) Selezionare modalità e difficoltà
3) Eseguire azioni sulla mappa
4) Vedere le statistiche a fine partita e riavviare il gioco.

## **Non Funzionali**

I requisiti non funzionali individuati per il progetto sono:
1) Realizzazione di software estendibile e rivisitabile. 
2) Realizzazione di un'esperienza di gioco godibile (usabilità, bilanciamento). 
3) Breve tutorial integrato che spieghi le regole, le azioni disponibili e le condizioni di vittoria.

## **Implementativi**

1) **Tecnologie:** Sviluppo in **Scala**. Utilizzo di **GitHub** per il repository e le **GitHub Actions** per 
automazione, **SBT** per la build e **IntelliJ IDEA** come ambiente di sviluppo.
2) **Testing:** Implementazione di **test automatizzati** per ogni entità sviluppata, coprendo più casi possibili. 
Utilizzo di **ScalaTest** e **JUnit**.

## **Opzionali**

Sono stati individuati dei requisiti non obbligatori del progetto ma che tuttavia accrescerebbe il valore del prodotto finale:
1) Possibilità di giocare con un amico alla modalità multiplayer 
2) Inserimento delle difficoltà per rendere il gioco più avvincente
