# Lösungen zu Übung 3 – Prozess-Modelle und Threads

Dieses Verzeichnis enthält die vollständigen theoretischen und praktischen Lösungen zu **Übungsblatt 3 (`Übungen-03-Prozesse-Modell.pdf`)** sowie den zugehörigen Vorlesungsfolien (`02-Betriebssysteme-01-ProzessModell.pdf` und `02-Betriebssysteme-02-Übungen.pdf`).

---

## Schnellstart in der Ubuntu VM

Falls `gcc` oder das Java JDK in der Ubuntu VM noch nicht installiert sind:
```bash
sudo apt update && sudo apt install -y build-essential default-jdk
```

Anschließend können **alle Aufgaben auf einmal** über das mitgelieferte Startskript kompiliert und ausgeführt werden:
```bash
chmod +x run_all_ubuntu.sh
./run_all_ubuntu.sh
```

---

## Aufgabe 3.1 (Theorie): Wesentliche Unterschiede zwischen Programm und Prozess (inkl. Fragen aus der Vorlesung)

### 1. Unterschied zwischen Programm und Prozess
| Merkmal | Programm | Prozess |
| :--- | :--- | :--- |
| **Definition** | Eine passive, statische Folge von Anweisungen und Daten (z. B. eine ausführbare Datei wie `/bin/ls` auf der Festplatte). | Ein **Programm in Ausführung** – eine aktive, dynamische Einheit auf dem Rechner mit eigenem Zustand und eigenen Systemressourcen. |
| **Existenzdauer** | Dauerhaft (persistent) auf einem Speichermedium gespeichert, bis die Datei gelöscht wird. | Zeitlich begrenzt (transiente Lebensdauer): Entsteht beim Prozessstart (`fork`/`exec`), durchläuft Zustände (`READY`, `RUNNING`, `BLOCKED`) und endet mit `exit` oder `kill`. |
| **Ressourcen** | Belegt lediglich Speicherplatz auf dem Datenträger (Festplatte/SSD). | Besitzt eine virtuelle CPU (Befehlszähler/Program Counter, Registerinhalte, Stack), einen eigenen Adressraum im Hauptspeicher (Text-, Daten-, Stacksegment) sowie geöffnete Dateien und Betriebssystem-Metadaten (PID, UID, Priorität). |
| **Beziehung (1:n)** | Ein einzelnes Programm existiert einmal als Datei. | Dasselbe Programm kann **mehrfach gleichzeitig** gestartet werden – jeder Start erzeugt einen **eigenen, unabhängigen Prozess** mit eigenem Adressraum. |

### 2. Analogon (aus den Vorlesungsfolien)
* **Programm = Kuchenrezept:** Das Kochbuch mit den niedergeschriebenen Anweisungen im Regal (passiv).
* **Prozess = Aktivität des Backens:** Das tatsächliche Zubereiten des Kuchens unter Verwendung von Zutaten (Daten), Backofen/Werkzeugen (CPU/Ressourcen) und aktuellem Arbeitsschritt im Rezept (Befehlszähler/PC). Zwei Bäcker können gleichzeitig nach demselben Rezept zwei Kuchen backen (= zwei Prozesse desselben Programms).

### 3. Was realisiert das Betriebssystem durch Prozesse?
* **Nebenläufigkeit und (Quasi-)Parallelität (Multiprogrammierung):** Durch schnelles Hin- und Herschalten der physischen CPU zwischen mehreren Prozessen (Kontextwechsel / Umladen der Register) entsteht für jeden Prozess die Illusion einer eigenen virtuellen CPU, und die CPU-Auslastung steigt ($1 - p^N$ bei $N$ Prozessen und E/A-Warteanteil $p$).
* **Schutz und Isolation:** Jeder Prozess besitzt seinen eigenen geschützten Adressraum, sodass ein fehlerhafter Prozess nicht den Speicher anderer Prozesse überschreiben kann.

### 4. Welche Daten werden benötigt, um einen Prozess zu beschreiben (Prozesskontrollblock / PCB)?
1. **Prozessverwaltung:** Prozess-ID (`PID`), Elternprozess-ID (`PPID`), Benutzer- und Gruppenkennung (`UID`, `GID`), Priorität, verbrauchte CPU-Zeit, Programmstatuswort (`PSW`), anhängige Signale.
2. **Ausführungs- und Zustandsinformationen (Thread-Kontext):** Aktueller Prozesszustand (`READY`, `RUNNING`, `BLOCKED`), Befehlszähler (`Program Counter`), Stack-Pointer, Inhalte der CPU-Register.
3. **Speicherverwaltung:** Zeiger/Seitentabellen auf Textsegment (Programmcode), Datensegment (globale Variablen/Heap) und Stacksegment.
4. **Dateiverwaltung:** Wurzelverzeichnis, aktuelles Arbeitsverzeichnis, Dateideskriptoren für geöffnete Dateien (`StdInput`, `StdOutput`, `StdError`, `Executable`).

### 5. Vorlesungsfrage (Folie 146): Textverarbeitungsprogramm (Tastatureingabe, Bildschirmausgabe, Festplattensicherung)
* **Vorteil des klassischen Ein-Thread-Prozessmodells:** Strenge Speichertrennung und einfache Synchronisation, da nur ein einziger Ausführungsfaden (Thread of Control) auf den Adressraum zugreift.
* **Nachteil im Szenario Textverarbeitung:**
  * **Ein einzelner Prozess (ohne Threads):** Wenn das Programm die Sicherung auf die langsame Festplatte schreibt, blockiert der gesamte Prozess (`BLOCKED`). Währenddessen kann der Benutzer keine Tastatureingaben machen und der Bildschirm friert ein.
  * **Mehrere separate Prozesse:** Da jeder Prozess einen komplett eigenen Adressraum hat, können die Prozesse nicht direkt auf denselben Dokumenttext im Speicher zugreifen. Der gesamte Text müsste aufwendig über Interprozess-Kommunikation (Shared Memory, Pipes) ausgetauscht werden, und das Erzeugen/Umschalten ganzer Prozesse ist teuer.
  * **Lösung durch Threads:** Mehrere Threads innerhalb **desselben Prozesses** teilen sich denselben Adressraum (das Dokument im Speicher), können aber unabhängig voneinander blockieren (Thread 1 liest Tastatur, Thread 2 formatiert Bildschirm, Thread 3 schreibt Backup auf Festplatte).

---

## Aufgabe 3.1 (Teil 2 / Aufgabe 3.2): UNIX Prozessverwaltung

Die Skripte befinden sich im Ordner [`aufgabe_3_2_unix/`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_2_unix):
* [`schlaefer.sh`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_2_unix/schlaefer.sh): Parametrisiertes Shellscript (`./schlaefer.sh <Kennziffer> <Sekunden>`).
* [`worker_abc.sh`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_2_unix/worker_abc.sh): Gibt 200-mal unmittelbar hintereinander `Hallo, hier ist Prozess x` aus.
* [`starter_abc.sh`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_2_unix/starter_abc.sh): Startet die drei Hintergrundprozesse `A`, `B` und `C`.
* [`demo_prozessverwaltung.sh`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_2_unix/demo_prozessverwaltung.sh): Führt alle Teilaufgaben automatisiert aus.

### Dokumentation der UNIX-Kommandos

```bash
cd aufgabe_3_2_unix
chmod +x *.sh

# 1. Zwei parametrisierte Hintergrundprozesse starten (Kennziffer 1 bzw. 2, jeweils 5 Sekunden Schlafzeit)
./schlaefer.sh 1 5 &
./schlaefer.sh 2 5 &

# 2. Prozessstatus des Systems ausgeben, während die Prozesse schlafen
ps -f
# (Alternativ detaillierter: ps aux | grep schlaefer oder jobs -l)

# 3. Einen Prozess vorzeitig beenden
./schlaefer.sh 1 10 &
PID1=$!
./schlaefer.sh 2 10 &
PID2=$!
kill $PID2          # Sendet SIGTERM (Signal 15) an Prozess 2 (oder kill -9 $PID2 für SIGKILL)

# 4. Einen Hintergrundprozess starten, der seinerseits 3 Hintergrundprozesse (A, B, C) startet
./starter_abc.sh &
```

### Beobachtung zur Reihenfolge der Prozessausführung (Prozesse A, B und C)
* **Beobachtung:** Die 200 Ausgaben von `A`, `B` und `C` erscheinen **nicht deterministisch** und **nicht in einer festen Reihenfolge** (z. B. erst ein Block von `A`, dazwischen gemischt Zeilen von `B` und `C`, und bei jedem erneuten Aufruf von `./starter_abc.sh &` sieht die Durchmischung anders aus).
* **Erklärung:** Sobald die drei Hintergrundprozesse gestartet werden, befinden sie sich gleichzeitig im Zustand `READY` bzw. `RUNNING`. Der **Scheduler des Betriebssystems** teilt den Prozessen Zeitscheiben (Time Slices) bzw. CPU-Kerne zu. Wann genau ein Prozesswechsel (Interrupt, Ablauf der Zeitscheibe oder E/A-Blockade beim Schreiben auf `stdout`) stattfindet, hängt von der aktuellen Systemlast, den Kernel-Buffern und dem Timer-Interrupt ab. Wie auf Vorlesungsfolie 134 festgehalten: **Die Laufzeit und Ausführungsreihenfolge nebenläufiger Prozesse ist nicht reproduzierbar.**

---

## Aufgabe 3.3: Java-Modell für den Prozess-Kontroll-Block (PCB)

Die Implementierung befindet sich im Ordner [`aufgabe_3_3_u_3_5_pcb_java/`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java):
* [`ProcessState.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/ProcessState.java): Enum für das Zustandsmodell (`READY`, `RUNNING`, `BLOCKED`).
* [`ProcessMetadata.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/ProcessMetadata.java): Kapselt `pid`, `parentPid`, `userId`, `groupId`, `priority`, `cpuTimeUsed`, `psw` und `state`.
* [`ProcessThread.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/ProcessThread.java): Kapselt `threadId`, `state`, `programCounter` (64-Bit `long`), `stackPointer` (64-Bit `long`) sowie die **16 Register einer 64-Bit CPU** (`long[16]`).
* [`ProcessAddressSpace.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/ProcessAddressSpace.java): Leere Klasse als Platzhalter für den Adressraum.
* [`ProcessFiles.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/ProcessFiles.java): Platzhalter für `stdInput`, `stdOutput`, `stdError` und `executable`.
* [`ProcessControlBlock.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/ProcessControlBlock.java): Bündelt alle Teilklassen. Intern wird bereits eine `List<ProcessThread>` verwendet (erweiterbar für mehrere Threads pro Prozess), während der Konstruktor im ersten Schritt direkt 1 Haupt-Thread entgegennimmt.
* [`MainAufgabe33.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/MainAufgabe33.java): Erstellt einen Beispiel-PCB und gibt ihn aus.

### Kompilieren und Ausführen in Ubuntu
```bash
cd aufgabe_3_3_u_3_5_pcb_java
javac *.java
java MainAufgabe33
```

---

## Aufgabe 3.4: Verwendung von `fork()` in C

Der C-Quellcode und das `Makefile` befinden sich im Ordner [`aufgabe_3_4_fork_c/`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_4_fork_c):
* [`fork_loop.c`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_4_fork_c/fork_loop.c)
* [`Makefile`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_4_fork_c/Makefile)

### Kompilieren und Ausführen in Ubuntu
```bash
cd aufgabe_3_4_fork_c
make
./fork_loop
```

### Beobachtung und Erklärung des Programmverhaltens
1. **Verhalten bei mehrfacher Ausführung:**
   * Nach dem Systemaufruf `fork()` existieren zwei unabhängige Prozesse (Vaterprozess mit Rückgabewert `pid > 0` und Kindprozess mit Rückgabewert `pid == 0`), die beide ab der Zeile nach `fork()` ihre jeweilige 500er-Schleife durchlaufen.
   * Da 500 Schleifendurchläufe auf modernen CPUs sehr schnell ablaufen, kommt es in manchen Läufen vor, dass ein Prozess (z. B. der Vater) alle 500 Zeilen am Stück ausgibt, bevor das Kind überhaupt das erste Mal die CPU erhält – oder dass sich die Ausgaben von `[VATER]` und `[KIND]` mitten im Lauf abwechseln bzw. auf Mehrkern-Systemen gleichzeitig in den Ausgabepuffer geschrieben werden.
   * Vergleicht man mehrere Läufe miteinander, variiert die Stelle, an der zwischen Vater und Kind umgeschaltet wird, von Lauf zu Lauf.
2. **Ursache:**
   * Der Kernel-Scheduler entscheidet unabhängig für jeden Lauf, ob nach `fork()` zuerst der Vaterprozess weiterläuft oder der Kindprozess eingeplant wird, wann ein Timer-Interrupt zuschlägt und ob beide Prozesse auf verschiedenen CPU-Kernen echt parallel laufen.

---

## Aufgabe 3.5: Erweiterung des Java-Modells zur Prozessverwaltung (`IBSProcess` & `BS`)

Die Erweiterung befindet sich ebenfalls im Ordner [`aufgabe_3_3_u_3_5_pcb_java/`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java):
* [`IBSProcess.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/IBSProcess.java): Schnittstelle mit `createProcess()`, `forkProcess(int parentPid)`, `killProcess(int pid)` und `printSystemState()`.
* [`BS.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/BS.java): Implementiert das Betriebssystem inkl. Prozesstabelle (`Map<Integer, ProcessControlBlock>`) und automatischer PID-Vergabe.
  * Bei `forkProcess(parentPid)` wird eine exakte Kopie des Eltern-PCBs mit neuer `PID` und `PPID = parentPid` angelegt; wie in UNIX erhält das Kind im Register `R0` den Rückgabewert `0` und der Vater die `childPid`.
* [`MainAufgabe35.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_3_u_3_5_pcb_java/MainAufgabe35.java):
  1. Erzeugt 5 Prozesse mit `createProcess()` (`PID` 1 bis 5).
  2. Kopiert von jedem dieser 5 Prozesse mit `forkProcess()` je ein Kind (`PID` 6 bis 10).
  3. Entfernt anschließend mit `killProcess(5)` und `killProcess(8)` die Prozesse mit der ID 5 und 8.

### Kompilieren und Ausführen in Ubuntu
```bash
cd aufgabe_3_3_u_3_5_pcb_java
javac *.java
java MainAufgabe35
```

---

## Aufgabe 3.6: Java Threads / Race Conditions

Die Implementierung befindet sich im Ordner [`aufgabe_3_6_race_java/`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_6_race_java):
* [`GlobalContainer.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_6_race_java/GlobalContainer.java): Statischer Container mit `public static int j = 0;` und `public static int k = 5;`.
* [`Race.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_6_race_java/Race.java): Thread-Klasse, die 10-mal `GlobalContainer.j = targetJ;` und `GlobalContainer.k = GlobalContainer.k + GlobalContainer.j;` ausführt (optional mit `Thread.yield()` nach jeder Operation).
* [`MainAufgabe36.java`](file:///Users/jan/Desktop/Betriebssysteme/aufgabe_3_6_race_java/MainAufgabe36.java): Führt die Läufe zuerst **ohne** `Thread.yield()` und anschließend **mit** `Thread.yield()` aus und vergleicht die Ergebnisse.

### Kompilieren und Ausführen in Ubuntu
```bash
cd aufgabe_3_6_race_java
javac *.java
java MainAufgabe36
```

### Beschreibung und Erklärung der Ergebnisse

1. **Mathematisch erwarteter Endwert (ohne Nebenläufigkeitsfehler):**
   * Startwert ist $k = 5$.
   * `Thread+1` addiert $10 \times (+1) = +10$.
   * `Thread-1` addiert $10 \times (-1) = -10$.
   * Ohne Überlagerung (Race Condition) müsste am Ende immer **$k = 5 + 10 - 10 = 5$** herauskommen.

2. **Ergebnis OHNE `Thread.yield()`:**
   * In den meisten Durchläufen ist das Ergebnis tatsächlich **$k = 5$** (oder nur selten abweichend).
   * **Grund:** Eine Schleife mit nur 10 Durchläufen ist so kurz, dass `Thread+1` nach `t1.start()` häufig innerhalb weniger Mikrosekunden komplett durchläuft und beendet ist, noch bevor das Betriebssystem `Thread-1` bei `t2.start()` überhaupt eingeplant hat. Es findet dann faktisch eine serielle Ausführung statt.

3. **Ergebnis MIT `Thread.yield()` nach jeder Operation:**
   * Durch `Thread.yield()` gibt der aktuelle Thread nach `j = targetJ` sowie nach `k = k + j` die CPU freiwillig an den Scheduler ab.
   * **Das Ergebnis ändert sich drastisch:** $k$ nimmt fast in jedem Lauf unterschiedliche, falsche Werte an (z. B. $k = -3$, $k = 9$, $k = 1$, $k = -5$, $k = 13$ usw.).
   * **Ursache (Race Condition / Kritischer Abschnitt):**
     * **Überschreiben von `j`:** `Thread+1` setzt `j = 1` und ruft `Thread.yield()` auf. Nun läuft `Thread-1` los und überschreibt `j = -1`. Wenn `Thread+1` danach wieder dran ist und `k = k + j` ausführt, addiert auch `Thread+1` fälschlicherweise `-1` statt `+1`!
     * **Lost Update bei `k = k + j`:** Die Anweisung `k = k + j` ist nicht atomar (sie besteht aus: `k` lesen, `j` lesen, addieren, `k` zurückschreiben). Wenn beide Threads denselben alten Wert von `k` lesen und nacheinander zurückschreiben, geht eine Addition komplett verloren.
