## Descrizione

Il modulo `PlayerHuman` rappresenta il giocatore umano nel gioco.  
È progettato per essere **immutabile**, **tipato in modo sicuro**, e conforme a una gerarchia funzionale basata sul trait `PlayerEntity`.

## Componenti principali
- `PlayerHuman` è un trait che definisce il comportamento pubblico del giocatore umano.
- `PlayerHumanImpl` è l'implementazione privata, non accessibile dall'esterno.
- Azioni eseguibili: `CityDefense`, `GlobalDefense`, `DevelopKillSwitch`.

## Campi principali
| Campo | Tipo | Descrizione |
|-------|------|-------------|
| `killSwitch` | `Int` | Stato di avanzamento dello sviluppo del kill switch |
| `defendedCities` | `Set[String]` | Città attualmente difese |
| `executedActions` | `List[HumanAction]` | Storico delle azioni eseguite |
| `conqueredCities` | `Set[String]` | (Opzionale) Città conquistate dal giocatore |

## Funzionalità principali
| Metodo | Descrizione |
|--------|-------------|
| `executeAction(action, worldMap)` | Esegue un'azione umana sullo stato attuale |
| `addAction(action)` | Metodo interno per aggiornare la lista delle azioni eseguite |
| `toString()` | Rende lo stato del giocatore umano formattato in output leggibile |

## Comportamento `executeAction`

### Azione `CityDefense`
- Aggiunge le città target a `defendedCities`
- Aggiorna `executedActions`
- Modifica la città nella mappa (se trovata)

### Azione `GlobalDefense`
- Aggiunge target a `defendedCities`
- Nessuna modifica sulla mappa (per il momento) andrà implementata

### Azione `DevelopKillSwitch`
- Incrementa il valore `killSwitch`

## Dettagli implementativi

//TODO

![UML PlayerHuman](../../image/PlayerHuman.png)
---

# HumanAction

---