# PPS project 24/25 -- Rise of the Machine

## Group's components
- Gianmaria Casamenti
- Luca Pasini
- Lorenzo Leoni


First Realese:

Strutture: 
    WorldState,
        turn
        world map
        IA action
        Human Action


Stampare 
Test unitari di 

una città è un insieme di caselle che può essere attaccata, sabotata(abbassata la difesa)
una città può essere anche una capitale quindi più grande e più difficile da sabotare e attaccare
le città possono essere sabotate, attaccate difese singolarmente e difese collettivamente



Interazioni:
    Creo due variabili sia l'IA che l'umanità dove ad ogni turno dopo la scelta dell'utente/i 
    chiamo il metodo execute action che mi restituisce la variabile aggiornata da cui aggiorno la mappa


- loro mi restituiscono delle azioni da applicare alle città
- come facciamo con le evoluzioni?
- nella cli per ogni azione dobbiamo restituire le possibilità di successo per permettere 
  all'utente di decidere in maniera sensata all'azione da fare