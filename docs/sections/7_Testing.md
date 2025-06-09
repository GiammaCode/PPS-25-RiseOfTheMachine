---
layout: default
title: Testing
nav_order: 7
---

# Testing

## Tecnologie utilizzate
Il sistema è stato testato utilizzando due framework principali:

- JUnit, impiegato inizialmente per scrivere test semplici e rapidi durante le prime fasi di sviluppo;
- ScalaTest, introdotto successivamente per testare in modo più approfondito e idiomatico le componenti core del sistema,
sfruttando appieno le potenzialità del linguaggio Scala.

Entrambi i framework sono pienamente compatibili con sbt e hanno consentito lo sviluppo di test unitari e di 
integrazione efficaci. L’adozione di ScalaTest ha inoltre favorito un approccio più espressivo e modulare, 
in linea con la struttura del progetto.

---

## Metodologia seguita
È stato adottato un approccio **TDD (Test-Driven Development)** durante lo sviluppo di componenti chiave:
- Ogni modulo è stato accompagnato dalla scrittura di test prima della sua implementazione;
- I test hanno servito come specifica del comportamento atteso;
- L’approccio ha favorito maggiore stabilità, sicurezza nella rifattorizzazione e chiarezza nel design.

---

## Copertura del codice
Per misurare il grado di copertura, è stato usato **scoverage**, uno strumento integrabile con sbt, questo ha reso
possibile generare un report di copertura.

Il quale ha evidenziato i nostri risultati nei test:

```
[info] Statement coverage.: 68.09%
[info] Branch coverage....: 54.35%
[info] Coverage reports completed
[info] All done. Coverage was stmt=[68.09%] branch=[54.35%]
```
è possibile analizzare nel dettaglio il report, cliccando nel tasto sottostante.
[📊 Visualizza Report Scoverage](scoverage-report/index.html){: .btn .btn-blue }


## Esempi di codice

**Esempio di ScalaTest con stile AnyFunSuite** nel file `test/scala/controller/GameControllerScalaTest.scala`

```scala
test("test humanAction globalDefence"):
val ListOfCity = List("A", "B", "C")
val testAction: Option[HumanAction] = Some(HumanAction.globalDefense(ListOfCity))
val (updatedState, _) = doHumanAction(testAction).run(gameState)
    val globalDefenseBoost = 2
    updatedState.worldState.worldMap.getCityByName(targetCityA).get.getDefense shouldBe
      gameState.worldState.worldMap.getCityByName(targetCityA).get.getDefense + globalDefenseBoost

    val wrongDefenseBoost = 5
    updatedState.worldState.worldMap.getCityByName(targetCityB).get.getDefense should not be
      gameState.worldState.worldMap.getCityByName(targetCityB).get.getDefense + wrongDefenseBoost
```

**Esempio di ScalaTest con stile AnyFlatSpec** nel `file test/scala/model/HumanStrategyScalaTest.scala`

```scala
  "SmartHumanStrategy in Easy mode" should "choose only CityDefense or GlobalDefense" in {
      given GameSettings = forSettings(GameMode.Singleplayer, Difficulty.Easy)
      given ActionProbabilities = EasyActionProbs
    
      val human = PlayerHuman.fromSettings
      val ai = PlayerAI.fromSettings
      val map = createWorldMap(MapSize)
      val state = createWorldState(map, ai, human, 0)
    
      val action = SmartHumanStrategy.decideAction(state)
      action match
      case _: CityDefense | _: GlobalDefense => succeed
      case _ => fail(s"Unexpected action: $action")
```
**Esempio di JUnit test** nel file `test/scala/model/CityTest.scala`

```scala
 @Test
 def testMethodOnCapital(): Unit =
    assertNotEquals(capital, createCity("Milano",capitalSize,isCapital = false))
    assertEquals(true , capital.isCapital)
    assertEquals(cityBaseDefense + (capitalSize * moltiplicator), capital.getDefense)
    assertEquals(cityBaseDefense + (capitalSize * moltiplicator) - playerAttack , capital.sabotateCity(playerAttack).getDefense)

```
