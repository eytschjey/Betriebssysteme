#!/usr/bin/env bash
# Hintergrundprozess, der seinerseits drei neue Hintergrundprozesse (A, B, C) startet

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

"$SCRIPT_DIR/worker_abc.sh" "A" &
PID_A=$!

"$SCRIPT_DIR/worker_abc.sh" "B" &
PID_B=$!

"$SCRIPT_DIR/worker_abc.sh" "C" &
PID_C=$!

wait "$PID_A" "$PID_B" "$PID_C"
