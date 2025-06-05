

[![Build](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Scala](https://img.shields.io/badge/scala-3.3.1-red)]()
[![License](https://img.shields.io/badge/license-MIT-blue)]()
[![Test](https://img.shields.io/badge/tests-100%25-success)]()

---
```bash
____    ___   ____    _____      ___    _____     _____   _   _   _____     __  __      _       ____   _   _   ___   _   _   _____ 
|  _ \  |_ _| / ___|  | ____|    / _ \  |  ___|   |_   _| | | | | | ____|   |  \/  |    / \     / ___| | | | | |_ _| | \ | | | ____|
| |_) |  | |  \___ \  |  _|     | | | | | |_        | |   | |_| | |  _|     | |\/| |   / _ \   | |     | |_| |  | |  |  \| | |  _|  
|  _ <   | |   ___) | | |___    | |_| | |  _|       | |   |  _  | | |___    | |  | |  / ___ \  | |___  |  _  |  | |  | |\  | | |___
|_| \_\ |___| |____/  |_____|    \___/  |_|         |_|   |_| |_| |_____|   |_|  |_| /_/   \_\  \____| |_| |_| |___| |_| \_| |_____|
```

## Panoramica del Progetto

**Rise of the Machine** è un gioco strategico a turni sviluppato in Scala, progettato nell’ambito del corso *Programming Paradigms and Development* presso l'Università di Bologna (Cesena).

Il giocatore umano deve difendere le città da un'IA ostile tramite azioni strategiche, cercando di sviluppare un Kill Switch per fermare l’invasione.

---

## Caratteristiche

- Gioco a turni con IA contro umano
- Azioni umane: `CityDefense`, `GlobalDefense`, `DevelopKillSwitch`
- humanPlayer con strategia adattiva
- CLI interattiva e visualizzazione della mappa
- Sistema di punteggio e fine partita condizionale
- Testing completo con JUnit e ScalaTest

---

## Architettura

- `model`: logica di gioco e struttura dati
- `strategy`: AI e strategia del giocatore umano
- `view`: interfaccia a riga di comando (CLI)
- `controller`: coordinamento del gioco
- `util`: configurazioni, impostazioni e costanti

---

## Avvio del progetto

### Prerequisiti

- [Scala 3](https://www.scala-lang.org/download/)
- [sbt (Scala Build Tool)](https://www.scala-sbt.org/)

### ▶Avviare il gioco

```bash
git clone https://github.com/yourusername/PPS-25-RiseOfTheMachine.git
cd PPS-25-RiseOfTheMachine
sbt run
```

## Documentazione
Si può visionare la documentazione ufficiale del progetto al seguente link delle github pages

👉 [Vai alla Documentazione](https://giammacode.github.io/PPS-25-RiseOfTheMachine/)

## Componenti del gruppo

- Gianmaria Casamenti – [gianmaria.casamenti@studio.unibo.it](mailto:gianmaria.casamenti@studio.unibo.it)
- Luca Pasini – [luca.pasini8@studio.unibo.it](mailto:luca.pasini8@studio.unibo.it)
- Lorenzo Leoni – [lorenzo.leoni5@studio.unibo.it](mailto:lorenzo.leoni5@studio.unibo.it)

[![Gianmaria](https://github.com/GiammaCode.png?size=80)](https://github.com/GiammaCode)
[![Luca](https://github.com/Paso2000.png?size=80)](https://github.com/Paso2000)
[![Lorenzo](https://github.com/LoryBug.png?size=80)](https://github.com/LoryBug)


