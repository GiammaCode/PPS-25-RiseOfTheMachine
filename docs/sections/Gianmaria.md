# WorldState
Il modulo `WorldState` rappresenta lo stato globale del gioco ad un dato turno.  
È progettato in stile **funzionale** e **immutabile** usando `opaque type`, e fornisce un'interfaccia 
pubblica attraverso metodi di estensione (`extension`).

---

## Componenti principali

- **WorldMap**: la mappa di gioco con le città
- **PlayerAI**: il giocatore controllato dalla CPU
- **PlayerHuman**: il giocatore umano
- **Turn**: intero che rappresenta il numero di turno corrente

---

## Funzionalità esposte

### Costruttore
```scala
def createWorldState(worldMap: WorldMap, playerAI: PlayerAI, playerHuman: PlayerHuman): WorldState
```
- turn -> Restituisce il numero del turno attuale
- worldMap ->	Restituisce la mappa attuale
- playerAI, playerHuman	-> Restituiscono i dati dei giocatori
- attackableCities -> Restituisce le città attaccabili e le probabilità di successo
- AIConqueredCities, humanConqueredCities -> Restituisce le città conquistate da ciascun giocatore
- infectionState -> Restituisce stato dell'infezione: (infette, totali)
- isGameOver -> Determina se il gioco è terminato
- updatePlayer, updateHuman, updateMap -> Restituiscono un nuovo WorldState aggiornato

![UML WorldState](../image/WorldState.png)

# PlayerHuman

# HumanAction

# ViewModule