#!/usr/bin/env bash
# Gesamtdemonstration der UNIX-Prozessverwaltungs-Aufgabe
set -u

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "============================================================"
echo "1. Zwei Hintergrundprozesse starten & Prozessstatus ausgeben"
echo "============================================================"
"$SCRIPT_DIR/schlaefer.sh" 1 4 &
PID1=$!
"$SCRIPT_DIR/schlaefer.sh" 2 4 &
PID2=$!

echo "Gestartet: Prozess 1 (PID=$PID1) und Prozess 2 (PID=$PID2)"
sleep 1
echo ""
echo "Aktueller Prozessstatus (ps -f):"
ps -f

echo ""
echo "Warte auf das reguläre Ende beider Prozesse..."
wait "$PID1" "$PID2"

echo ""
echo "============================================================"
echo "2. Einen Hintergrundprozess vorzeitig mit kill beenden"
echo "============================================================"
"$SCRIPT_DIR/schlaefer.sh" 1 5 &
PID1=$!
"$SCRIPT_DIR/schlaefer.sh" 2 5 &
PID2=$!

echo "Gestartet: Prozess 1 (PID=$PID1) und Prozess 2 (PID=$PID2)"
sleep 2
echo "Beende Prozess 2 (PID=$PID2) vorzeitig mit 'kill $PID2'..."
kill "$PID2" 2>/dev/null || true
echo "Prozessstatus nach kill:"
ps -f
echo "Warte auf Prozess 1 (PID=$PID1)..."
wait "$PID1" 2>/dev/null || true
wait "$PID2" 2>/dev/null || true

echo ""
echo "============================================================"
echo "3. Hintergrundprozess startet 3 Hintergrundprozesse (A, B, C)"
echo "============================================================"
echo "Starte starter_abc.sh im Hintergrund (Ausgabe gekürzt auf erste/letzte Zeilen zur Übersicht, volle Ausgabe in abc_output.txt)..."
"$SCRIPT_DIR/starter_abc.sh" > "$SCRIPT_DIR/abc_output.txt" &
STARTER_PID=$!
wait "$STARTER_PID"

echo "Erste 25 Zeilen aus abc_output.txt:"
head -n 25 "$SCRIPT_DIR/abc_output.txt"
echo "..."
echo "Letzte 15 Zeilen aus abc_output.txt:"
tail -n 15 "$SCRIPT_DIR/abc_output.txt"
echo ""
echo "Fertig! Die vollständige Ausgabe von A, B, C liegt in: $SCRIPT_DIR/abc_output.txt"
