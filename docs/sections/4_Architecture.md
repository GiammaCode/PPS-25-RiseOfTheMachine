---
layout: default
title: Archittetura
nav_order: 4
---

# Architettura

## Pattern Architetturale
L'architettura adottata per il progetto "Rise of the Machine" si basa sul pattern Model-View-Controller (MVC),
una scelta diffusa per la sua capacità di separare la logica di business, la presentazione all'utente e la gestione 
delle interazioni. Questo pattern rientra tra i principi architetturali che definiscono i sottosistemi principali, i 
loro confini, contratti e ruoli.


**Pattern Architetturale:** MVC
### Model:
Il Model rappresenta i fondamenti della logica di gioco, includendo le regole, le entità dei giocatori (IA e Umanità)
e la mappa di gioco.
È progettato con un'enfasi sulla programmazione funzionale e l'immutabilità. Questo approccio garantisce che lo 
stato di gioco sia sempre prevedibile e thread-safe, poiché le modifiche producono nuove istanze anziché alterare 
quelle esistenti.


### View:
La View è responsabile della visualizzazione delle informazioni al giocatore.	L'interfaccia è implementata tramite
riga di comando (CLI), visualizzando la griglia della partita e ricevendo input da tastiera.Il sistema è stato 
progettato per essere facilmente estendibile con una futura interfaccia grafica (GUI), senza richiedere modifiche
sostanziali alla logica di gioco.

### Controller:
Il Controller ha il compito di gestire l'interazione dell'utente, interpretare gli input e invocare i metodi appropriati
sul Model, funge da punto centrale per il ciclo di gioco, orchestrando le azioni di entrambi i giocatori.


## Architettura Complessiva: Vantaggi
L'adozione del pattern MVC e dei principi di programmazione funzionale offre diversi vantaggi chiave:
1) **Separazione Chiara delle Responsabilità**: I diversi aspetti del sistema (logica di business, presentazione, 
interazione) sono distinti e gestiti da componenti dedicati, riflettendo il principio di Separation of Concerns.
2) **Elevata Manutenibilità**: La chiara divisione e l'uso di principi di immutabilità riducono gli effetti collaterali,
rendendo il codice più prevedibile e facile da modificare o correggere in futuro. 
3) **Flessibilità e Estendibilità**: Il design modulare consente di aggiungere facilmente nuove viste (es. una GUI), 
nuove modalità di input, o nuove strategie e abilità di gioco senza alterare la logica di base del Model. 
4) **Testabilità Migliorata**: Ogni componente può essere testato in isolamento, facilitando l'identificazione e
la correzione dei bug. Il progetto include test automatizzati per i moduli di Model, View e Controller.
