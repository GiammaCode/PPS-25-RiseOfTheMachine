Durante l'implementazione del progetto *Rise of the Machine*, mi sono occupato della progettazione e sviluppo di componenti chiave sia lato modello che lato vista, con particolare attenzione alla coerenza del sistema e all’estensibilità del codice.

Tra i moduli da me sviluppati troviamo: 

## **Stato di gioco del sistema**
Ho sviluppato il modulo `WorldState`, che gestisce lo stato globale del gioco. Include:
- Lo stato corrente delle città (livelli di infezione e sabotaggio)
- I giocatori coinvolti (umano e AI)
- La difficoltà selezionata e l'avanzamento del turno
- I metodi di aggiornamento e trasformazione dello stato tra turni

> Questo componente è centrale per mantenere una simulazione coerente del mondo e per permettere strategie dinamiche basate sull'evoluzione della partita.

🔍 [Full details](worldState.md)

---

#### Entità di gioco Human
Mi sono occupato della definizione dell'entità `PlayerHuman`, con responsabilità come:
- Gestione delle città difese
- Azioni disponibili per il giocatore umano
- Supporto alle strategie differenziate in base alla difficoltà (Easy, Normal, Hard)

> Il design è stato pensato per supportare strategie evolutive e test unitari efficaci.

🔍 [Full details](humanEntity.md)

---

#### Componentistica di View
Ho contribuito alla parte di interfaccia, progettando componenti della **View** in modo modulare:
- Rappresentazione dello stato delle città
- Interazione per le azioni del giocatore (es. difesa o sviluppo kill switch)
- Feedback visuale in base allo stato di gioco
- Regole di formattazione

> L'obiettivo era mantenere una separazione chiara tra logica e presentazione, seguendo i principi dell'architettura MVC.

🔍 [Full details](viewSystem.md)

---

*Per maggiori dettagli, consulta ciascun modulo linkato.*
