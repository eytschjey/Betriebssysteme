#!/usr/bin/env bash
# Gibt 200-mal unmittelbar hintereinander "Hallo, hier ist Prozess x" aus
# Aufruf: ./worker_abc.sh <A|B|C>

KENNUNG="${1:-A}"

for ((i = 1; i <= 200; i++)); do
    echo "Hallo, hier ist Prozess $KENNUNG (Durchlauf $i)"
done
