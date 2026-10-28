import java.util.ArrayList;
import java.util.List;

/**
 * Aufgabe 3.5: Hauptprogramm zur Demonstration der Prozessverwaltung in BS.
 * 1. Erzeugt 5 Prozesse exemplarisch (createProcess()) -> PIDs 1..5
 * 2. Kopiert von jedem dieser Prozesse mit forkProcess() jeweils ein Kind -> PIDs 6..10
 * 3. Entfernt anschließend mit killProcess() die Prozesse mit der ID 5 und 8.
 */
public class MainAufgabe35 {
    public static void main(String[] args) {
        IBSProcess bs = new BS();

        System.out.println("=== Schritt 1: 5 Prozesse mit createProcess() erzeugen ===");
        List<Integer> initialPids = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ProcessControlBlock pcb = bs.createProcess("/usr/bin/dienst_" + i, 1000, 10 + i);
            initialPids.add(pcb.getMetadata().getPid());
        }
        bs.printSystemState();

        System.out.println("\n=== Schritt 2: Von diesen 5 Prozessen mit forkProcess() je ein Kind kopieren ===");
        for (int parentPid : initialPids) {
            bs.forkProcess(parentPid);
        }
        bs.printSystemState();

        System.out.println("\n=== Schritt 3: Prozesse mit ID 5 und ID 8 mit killProcess() entfernen ===");
        bs.killProcess(5);
        bs.killProcess(8);
        bs.printSystemState();
    }
}
