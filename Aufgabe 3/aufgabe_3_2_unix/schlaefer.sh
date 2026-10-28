#!/usr/bin/env bash
# Aufgabe 3.1 (Teil 2) / 3.2: Parametrisiertes Shellscript für Hintergrundprozesse
# Aufruf: ./schlaefer.sh <Kennziffer> <Sekunden>

if [ "$#" -lt 2 ]; then
    echo "Verwendung: $0 <Kennziffer> <Sekunden>"
    exit 1
fi

KENNZIFFER="$1"
SEKUNDEN="$2"

sleep "$SEKUNDEN"
echo "Hier Prozess $KENNZIFFER"
sleep "$SEKUNDEN"
echo "Hier Prozess $KENNZIFFER"
