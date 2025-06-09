---
layout: default
title: Processo di sviluppo
nav_order: 2
---

# **Processo di sviluppo**

Il processo di sviluppo adottato dal team è ispirato a Scrum: sarà basato su **sprint** e **obiettivi** per realizzare 
il progetto in maniera **agile**.

All’interno del team sono stati scelti un *committente* e un *product owner*.  
Il team effettua sprint della durata di circa un paio di settimane, durante i quali si definiscono gli obiettivi e si 
suddividono i compiti.  
Di seguito, si discutono gli elementi fondamentali del processo di sviluppo adottato.

## **Meeting**

I meeting sono un fattore fondamentale per il processo di sviluppo e avvengono con cadenza quasi giornaliera, e con
durate differenti in base all’importanza.

La maggior parte dei meeting è avvenuta di persona e in alcuni casi con videochiamate su Microsoft Teams.

## **Modalità di divisione in itinere dei task**

### **Definition of Done**

Una funzionalità di gioco viene definita completata quando, a seguito di una revisione da parte di un altro componente 
del team o in pair programming, viene pubblicata sul *branch* principale di sviluppo.

### **Coordinazione**

La comunicazione è fondamentale per un processo di sviluppo Agile, anche se i membri del team si conoscono a fondo.  
Per coordinarsi al meglio, il team ha deciso di utilizzare **Issue di Github**,  
con il quale vengono tracciati i task delle relative sprint, individuando un flusso di lavoro all’interno di ogni 
sprint organizzativo.

Inoltre, il *product owner* del team ha redatto un *product backlog* nel quale si è tenuto traccia dei task, indicando
per ciascuno il grado di difficoltà di progettazione e/o implementazione e l’effort richiesto in ciascuno sprint.

### **Meeting iniziale**

Prima della proposta del progetto, è avvenuto un incontro all’interno del quale sono stati decisi i seguenti
fattori essenziali:

* **Ruoli**: Gianmaria Casamenti sarà il product Owner e Sviluppatore, Luca il committente e sviluppatore e Lorenzo sarà 
Responsabile Test e sviluppatore.

* **Specifiche**: sono stati decisi gli obiettivi funzionali, facendo attenzione alla loro fattibilità.

### **Sprint Planning**

All’inizio di ogni sprint viene effettuato un incontro in cui si discutono i risultati dello sprint precedente e 
si definiscono gli obiettivi del successivo.  
I principali punti di discussione sono:

* Definizione degli obiettivi
* Definizione ed assegnazione dei task
* Valutazione dell’andamento complessivo del progetto
* Valutazione dello sprint precedente

La durata ideale dello Sprint Planning è fissata a 2 ore.

### **Divisione dei compiti**

L’effettiva divisione dei compiti, da eseguire nello sprint successivo, viene fatta contestualmente alla chiusura
dello sprint precedente.  
La suddivisione terrà conto del carico di lavoro, degli impegni del singolo componente e di eventuale lavoro incompiuto.

## **Modalità di revisione in itinere dei task**

### **Ridistribuzione del carico lavorativo**

All’interno dello sprint è prevista la ridistribuzione del lavoro.  
Se ci si accorge che il carico di lavoro di un task è diverso da quello previsto, è possibile ridefinire i 
partecipanti al task.  
Questo bilanciamento avviene a seguito di uno *Stand-up Meeting* e di una valutazione dell’intero gruppo.

### **Stand-up Meeting**

Con cadenza quasi giornaliera, il team effettua incontri brevi in cui ogni membro espone il lavoro svolto e 
le difficoltà.

La durata ideale degli Stand-up Meeting è di 10-15 minuti.

## **Tool Ausiliari**

Per supportare il processo agile, il team utilizza strumenti volti a migliorare l’efficienza e a concentrarsi 
sullo sviluppo.

### **Automazione**

Il progetto ha integrato pratiche moderne per il deployment e la manutenzione, rientranti 
nella **continuous integration/delivery**.

* **Continuous Integration (CI):** Il workflow pr-test.yml esegue i test su ogni **pull request** 
aperta o sincronizzata sul branch main dal branch develop. Questo garantisce l'integrità continua del progetto e
da la possibilità a tutti gli sviluppatori se una pull request ha fatto passare o meno tutti i test.
* **Continuous Delivery (CD)/Deployment:** Il workflow release.yml gestisce il rilascio automatico del progetto.
Si attiva su **tag semantici** (es. v\*.\*.\*) e produce un **JAR eseguibile** (RiseOfTheMachine.jar) utilizzando 
sbt assembly, che viene poi caricato come release su GitHub. Questo permette di avere una base stabile su cui iterare 
dopo ogni sprint.

### **Versioning**

Per la gestione del codice sorgente, il team utilizza **Git** adottando il flusso di lavoro **GitFlow**, che consente 
una gestione strutturata e collaborativa dello sviluppo. La strategia prevede:

* Due branch principali e stabili:

    * main: contiene le versioni di produzione

    * develop: raccoglie le ultime funzionalità pronte per il rilascio

* **Branch dedicati per ogni nuova feature**, che vengono creati da develop e reintegrati tramite pull request una
volta completati.

* **Branch hotfix**, creati direttamente da main, utilizzati per correggere rapidamente bug critici in produzione e 
poi reintegrati sia su main che su develop.

Per il tracciamento delle versioni viene adottato lo **Schema Semantico (Semantic Versioning)**, nel formato 
MAJOR.MINOR.PATCH, che consente di distinguere chiaramente tra aggiornamenti che introducono nuove funzionalità, 
miglioramenti minori o semplici correzioni di bug. Le versioni vengono taggate direttamente su Git e attivano 
automaticamente il processo di rilascio tramite GitHub Actions.