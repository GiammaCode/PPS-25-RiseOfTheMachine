---
layout: default
title: Testing
nav_order: 7
---

# Testing

## Tecnologie utilizzate
Il sistema è stato testato utilizzando:
- **ScalaTest** 
- **JUnit**

Entrambi permettono test unitari e di integrazione e sono compatibili con sbt.

---

## Metodologia seguita
È stato adottato un approccio **TDD (Test-Driven Development)** durante lo sviluppo di componenti chiave:
- Ogni modulo è stato accompagnato dalla scrittura di test prima della sua implementazione;
- I test hanno servito come specifica del comportamento atteso;
- L’approccio ha favorito maggiore stabilità, sicurezza nella rifattorizzazione e chiarezza nel design.

---

## Copertura del codice
Per misurare il grado di copertura, è stato usato **scoverage**, uno strumento integrabile con sbt.

[📊 Report Scoverage](scoverage-report/index.html){: .btn .btn-blue }


## Esempi di codice
