# Processo di sviluppo
Il processo di sviluppo adottato dal team è ispirato a Scrum: sarà basato su **sprint** e **obiettivi**
per realizzare il progetto in maniera **agile**.  

All'interno del team sono stati scelti un *committente* e un *product owner*.  
Il team effettua sprint della durata di circa una settimana, durante i quali si definiscono gli obiettivi e si suddividono i compiti.  
Di seguito, si discutono gli elementi fondamentali del processo di sviluppo adottato.

## Meeting
I meeting sono un fattore fondamentale per il processo di sviluppo e avvengono con cadenza quasi giornaliera,
e con durate differenti in base all'importanza. 

La maggiorparte dei meeting è avvenuta di persona e in alcuni casi con videochiamate su Microsoft Teams.

## Modalità di divisione in itinere dei task

### Definition of Done
Una funzionalità di gioco viene definita completata quando, a seguito di una revisione da parte
di un altro componente del team, viene pubblicata sul *branch* principale.  

Questa revisione può avvenire tramite *pair-programming* oppure tramite il meccanismo di *pull-request*.

### Coordinazione
La comunicazione è fondamentale per un processo di sviluppo Agile, anche se i membri del team si conoscono a fondo.  
Per coordinarsi al meglio, il team ha deciso di utilizzare **Issue di Github**,  
con il quale vengono tracciati i task dei singoli membri con il rispettivo andamento,
individuando un flusso di lavoro all'interno di ogni sprint organizzativo.  

Inoltre, il *product owner* del team ha redatto un *product backlog* nel
quale si è tenuto traccia dei task, indicando
per ciascuno il grado di difficoltà di progettazione e/o implementazione e l'effort richiesto in ciascuno sprint.

### Meeting iniziale
Prima della proposta del progetto, è avvenuto un incontro all'interno del quale sono stati decisi i seguenti fattori essenziali:

- **Ruoli**:
- **Specifiche**: sono stati decisi gli obiettivi funzionali, facendo attenzione alla loro fattibilità.
- **Primo Sprint**: è stata decisa l'organizzazione del primo sprint, definendo l'obiettivo finale e suddividendo i task tra i componenti del gruppo.

> La durata del primo meeting è stata di circa 3 ore.

### Sprint Planning
All'inizio di ogni sprint viene effettuato un incontro in cui si discutono i
risultati dello sprint precedente e si definiscono gli obiettivi del successivo.  
I principali punti di discussione sono:

- Definizione degli obiettivi
- Definizione ed assegnazione dei task
- Valutazione dell'andamento complessivo del progetto, rimarcando eventuali ritardi
- Valutazione dello sprint precedente

> La durata ideale dello Sprint Planning è fissata a 2 ore.

### Divisione dei compiti
L'effettiva divisione dei compiti, da eseguire nello sprint successivo, viene fatta
contestualmente alla chiusura dello sprint precedente.  
La suddivisione terrà conto del carico di lavoro, degli impegni del singolo componente e di eventuale lavoro incompiuto.

## Modalità di revisione in itinere dei task

### Ridistribuzione del carico lavorativo
All'interno dello sprint è prevista la ridistribuzione del lavoro.  
Se ci si accorge che il carico di lavoro di un task è diverso da quello previsto, è possibile ridefinire i partecipanti al task.  
Questo bilanciamento avviene a seguito di uno *Stand-up Meeting* e di una valutazione dell'intero gruppo.

### Stand-up Meeting
Con cadenza quasi giornaliera, il team effettua incontri brevi in cui ogni membro espone il lavoro svolto e le difficoltà.
> La durata ideale degli Stand-up Meeting è di 10-15 minuti.

## Tool Ausiliari
Per supportare il processo agile, il team utilizza strumenti volti a migliorare
l’efficienza e a concentrarsi sullo sviluppo.

### Automazione

//TODO

### Versioning
Per la gestione del codice sorgente, il team adotta **Git** e il flusso **GitFlow**, con:

- due *stable branch* (`main` e `develop`)
- branch separati per ogni feature

> Al momento **non** viene usato Semantic Versioning,  
> ma si utilizzano i **Conventional Commits** per avere messaggi chiari e aprire in futuro al versionamento semantico.  
> Le release saranno fatte al termine di ogni sprint, con tag del tipo `0.1.0`.

