---
layout: default
title: Strategia Giocatore Umano
---

# Entità di gioco Human

## PlayerHuman
PlayerHuman rappresenta il giocatore umano in gioco, implementando un insieme di funzionalità tipiche 
del comportamento umano assistito da una strategia intelligente.

Estende PlayerEntity, condividendo l'interfaccia comune con altre entità (es. IA).
Le azioni disponibili e l’interazione con il mondo sono modellate in modo esplicito.

È supportato un comportamento strategico tramite SmartHumanStrategy.

### Aspetti implementativi
- Pattern usati:
  - Trait + Impl + Companion Object:Uso del trait PlayerHuman con una implementazione concreta 
  PlayerHumanImpl e un companion object per factory methods (fromSettings).

  - Strategy Pattern: decideActionByStrategy(worldState) usa SmartHumanStrategy, 
  permettendo comportamenti dinamici configurabili.

  - Immutable Data Structures: case class con metodi copy(...) per generare nuove istanze
  in seguito a modifiche (programmazione funzionale).

- Scelte architetturali:
  - Le azioni sono fortemente tipizzate tramite HumanAction, separate in sottotipi come CityDefense, 
  GlobalDefense, DevelopKillSwitch.

  - Ogni executeAction produce un ExecuteActionResult, incapsulando il risultato e i cambiamenti sul mondo.

### Funzionalità principali

| Funzionalità             | Descrizione                                                                |
|--------------------------| -------------------------------------------------------------------------- |
| `killSwitch`             | Tiene traccia del progresso nello sviluppo di una super arma (kill switch) |
| `defendedCities`         | Città attualmente difese dal giocatore                                     |
| `executedActions`        | Storico delle azioni eseguite                                              |
| `getPossibleAction`      | Ritorna l'elenco di azioni disponibili                                     |
| `decideActionByStrategy` | Sceglie un’azione basata sullo stato del mondo e una strategia configurata |
| `executeAction(action)`  | Applica gli effetti dell’azione al player e alla mappa                     |
| `fromSettings`           | Costruttori statici per inizializzare il player                            |
| `toString`               | Rappresentazione testuale dello stato del player                           |

### Diagramma delle classi
```mermaid
classDiagram
direction TB

    %% === Class Definitions ===
    class PlayerEntity {
        <<trait>>
        +type ValidAction <: TurnAction
        +type Self <: PlayerEntity
        +List~ValidAction~ executedActions
        +executeAction(action, worldMap): ExecuteActionResult~Self~
    }

    class PlayerHuman {
        <<trait>>
        +Int killSwitch
        +Set~String~ defendedCities
        +List~HumanAction~ executedActions
        +List~HumanAction~ getPossibleAction()
        +HumanAction decideActionByStrategy(worldState)
        +executeAction(action, worldMap): ExecuteActionResult~PlayerHuman~
    }

    class PlayerHumanImpl {
        <<case class>>
        -Int killSwitch
        -ActionProbabilities actionsWeight
        -Set~String~ defendedCities
        -List~HumanAction~ executedActions
        +getPossibleAction(): List~HumanAction~
        +executeAction(): ExecuteActionResult~PlayerHuman~
        +decideActionByStrategy(): HumanAction
        +toString(): String
    }

    class PlayerHumanFactory {
        <<object>>
        +fromStats(): PlayerHuman
        +fromSettings(): PlayerHuman
    }

    %% === Inheritance Relationships ===
    PlayerHuman ..|> PlayerEntity : «extends»
    PlayerHumanImpl ..|> PlayerHuman : «implements»

    %% === Associations ===
    PlayerHumanFactory --> PlayerHuman : «factory»
```
## PlayerStrategy
PlayerStrategy rappresenta un'interfaccia generica per qualsiasi strategia di gioco che possa decidere un'azione
(TurnAction) in base allo stato attuale del mondo (WorldState). È progettata per supportare decisioni sia umane 
che artificiali.

L'oggetto SmartHumanStrategy fornisce un'implementazione concreta pensata per il giocatore umano, 
con logiche di scelta influenzate dalla difficoltà del gioco (Easy, Normal, Hard), pesando le diverse azioni disponibili.

### Aspetti implementativi
- **Pattern e Tecniche Utilizzate**

  - Strategy Pattern: PlayerStrategy è un trait parametrico che permette di definire strategie intercambiabili 
  per agenti diversi.

  - Weighted Random Selection: SmartHumanStrategy usa una selezione casuale pesata per scegliere un’azione, 
  guidata da ActionProbabilities.

  - Pattern Matching e Filtering: selezione delle città e delle azioni tramite pattern e trasformazioni 
  funzionali (map/filter/sort).

  - Functional Programming: immutabilità e uso di costrutti come Option.when, collect, scanLeft, ecc.

- **Aspetti implementativi `SmartHumanStrategy`**:

  - La decisione viene determinata dalla funzione `decideAction(state: WorldState)`, che sceglie tra tre varianti:
    - **Easy** → difensiva e passiva (stessa priorità CityDefense e GlobalDefense, no KillswitchDevelopment)
    - **Normal** → scelta casuale tra tutte le azioni disponibili (Stessa priorità tutte e 3 le azioni)
    - **Hard** → priorità alle città più a rischio e azioni più aggressive (maggiori probabilità CityDefense)
  
  
  - Il modulo `PlayerHuman` utilizza `SmartHumanStrategy` come strategia di default. 


### Funzionalità principali

| Componente            | Funzione                                                     |
| --------------------- | ------------------------------------------------------------ |
| `ActionProbabilities` | Contiene i pesi (priorità) per ciascuna azione umana         |
| `PlayerStrategy[A]`   | Trait generico per strategie di azione                       |
| `SmartHumanStrategy`  | Strategia concreta per il giocatore umano                    |
| `decideAction(state)` | Seleziona un’azione in base allo stato del mondo             |
| `possibleTargets`     | Ritorna le città attaccabili senza ordinamento               |
| `topRiskTargets`      | Ordina le città in base al rischio di infezione + sabotaggio |
| `nonDefended`         | Filtra le città già difese dal giocatore                     |
| `baseHumanActions`    | Costruisce la lista di azioni disponibili                    |
| `chooseWeighted`      | Algoritmo di selezione pesata tra azioni                     |

### Diagramma delle classi

```mermaid
classDiagram
direction TB

    class ActionProbabilities {
        <<case class>>
        +Int cityDefenseWeight
        +Int globalDefenseWeight
        +Int killSwitchWeight
    }

    class PlayerStrategy~A~ {
        <<trait>>
        +decideAction(state: WorldState): A
    }

    class SmartHumanStrategy {
        <<object>>
        +decideAction(state: WorldState): HumanAction
        -possibleTargets(state): List~String~
        -topRiskTargets(state): List~String~
        -nonDefended(cities, state): List~String~
        -baseHumanActions(defend, allTargets): List~HumanAction~
        -chooseWeighted(actions): HumanAction
    }

    PlayerStrategy <|.. SmartHumanStrategy : «implements»
    SmartHumanStrategy --> ActionProbabilities : «uses»

```

### Diagramma delle classi PlayerHuman-SmartHumanStrategy

```mermaid
classDiagram
  class PlayerHuman {
    +decideActionByStrategy(state): HumanAction
    +executeAction(action): ExecuteActionResult
  }

  class SmartHumanStrategy {
    +decideAction(state): HumanAction
    -decideEasy()
    -decideNormal()
    -decideHard()
  }

  class HumanAction {
    <<sealed trait>>
    +targets: List[ActionTarget]
    +name: String
  }

  class CityDefense
  class GlobalDefense
  class DevelopKillSwitch

  PlayerHuman --> SmartHumanStrategy
  SmartHumanStrategy --> HumanAction
  HumanAction <|-- CityDefense
  HumanAction <|-- GlobalDefense
  HumanAction <|-- DevelopKillSwitch
```

## ActionProbabilities
Il tipo ActionProbabilities rappresenta un punto di forza chiave dell’architettura strategica del giocatore umano.
È una struttura semplice ma altamente espressiva, che consente di assegnare pesi relativi (priorità) alle tre azioni
fondamentali: CityDefense, GlobalDefense e DevelopKillSwitch.

**Benefici chiave**: 

- Controllo del comportamento strategico :le strategie non sono hardcoded: possono essere adattate dinamicamente
in base alla difficoltà (Easy, Normal, Hard) e alla configurazione del gioco. Questo consente un bilanciamento fine 
del comportamento umano simulato.

- Random ma prevedibile: grazie alla selezione pesata (weighted random), si ottiene un comportamento pseudo-randomico 
ma controllabile, fondamentale in giochi strategici dove l’IA non deve essere totalmente deterministica né
totalmente imprevedibile.

- Testing mirato e robusto: in fase di test (SmartHumanStrategyFlatSpec.scala), ActionProbabilities si è
rivelato essenziale per:
  - Forzare la strategia a non eseguire certe azioni (es. peso 0 a DevelopKillSwitch);

  - Verificare comportamenti in modalità differenti senza bisogno di mock complessi;

  - Confrontare strategie a parità di pesi, ma con logica decisionale differente
  (es. confronti tra Normal e Hard in targeting delle città ad alto rischio).

### Esempio tratto dai test
```scala
"SmartHumanStrategy" should "prefer high-risk cities more in Hard than in Normal mode" in {
given DeterministicMap: CreateModuleType = DeterministicMapModule
val map = createWorldMap(8)

    def runWithDifficulty(diff: Difficulty, probs: ActionProbabilities) =
      given GameSettings = forSettings(GameMode.Singleplayer, diff)
      given ActionProbabilities = probs

      val human = PlayerHuman.fromSettings
      val ai = PlayerAI.fromSettings
      val state = createWorldState(map, ai, human, 0)
      SmartHumanStrategy.decideAction(state)

    val normalAction = runWithDifficulty(Difficulty.Normal, ActionProbabilities(100, 0, 0))
    val hardAction = runWithDifficulty(Difficulty.Hard, ActionProbabilities(100, 0, 0))

    normalAction shouldBe a[CityDefense]

    hardAction match
      case CityDefense(targets) =>
        targets should contain ("C") // C is the city with highest risk in DeterministicMap (size = 8)
      case _ => fail("Expected CityDefense targeting high-risk city")
}
```
