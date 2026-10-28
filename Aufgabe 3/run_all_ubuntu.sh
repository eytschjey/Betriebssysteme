#!/usr/bin/env bash
# Gesamtskript zum Kompilieren und Ausführen aller Lösungen in einer Ubuntu VM
set -e

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "======================================================================"
echo "Prüfe benötigte Werkzeuge (bash, gcc, javac, java)..."
echo "======================================================================"
for cmd in bash gcc javac java; do
    if ! command -v "$cmd" >/dev/null 2>&1; then
        echo "FEHLER: '$cmd' ist nicht installiert."
        echo "Bitte in Ubuntu installieren mit:"
        echo "  sudo apt update && sudo apt install -y build-essential default-jdk"
        exit 1
    fi
done
echo "Alle Werkzeuge vorhanden!"

echo ""
echo "======================================================================"
echo "AUFGABE 3.1 / 3.2: UNIX Prozessverwaltung (Shellscripts)"
echo "======================================================================"
chmod +x "$ROOT_DIR/aufgabe_3_2_unix/"*.sh
"$ROOT_DIR/aufgabe_3_2_unix/demo_prozessverwaltung.sh"

echo ""
echo "======================================================================"
echo "AUFGABE 3.3: Java Modell für den Prozess-Kontroll-Block (PCB)"
echo "======================================================================"
cd "$ROOT_DIR/aufgabe_3_3_u_3_5_pcb_java"
javac *.java
java MainAufgabe33

echo ""
echo "======================================================================"
echo "AUFGABE 3.4: Verwendung von fork() in C"
echo "======================================================================"
cd "$ROOT_DIR/aufgabe_3_4_fork_c"
make
echo "Starte ./fork_loop (Ausgabe gekürzt auf erste/letzte 20 Zeilen, volle Ausgabe in fork_output.txt):"
./fork_loop > fork_output.txt
head -n 20 fork_output.txt
echo "..."
tail -n 20 fork_output.txt

echo ""
echo "======================================================================"
echo "AUFGABE 3.5: Erweiterung des Java-Modells (IBSProcess & BS)"
echo "======================================================================"
cd "$ROOT_DIR/aufgabe_3_3_u_3_5_pcb_java"
java MainAufgabe35

echo ""
echo "======================================================================"
echo "AUFGABE 3.6: Java Threads / Race Conditions (ohne & mit Thread.yield())"
echo "======================================================================"
cd "$ROOT_DIR/aufgabe_3_6_race_java"
javac *.java
java MainAufgabe36

echo ""
echo "======================================================================"
echo "Alle Aufgaben erfolgreich kompiliert und ausgeführt!"
echo "======================================================================"
