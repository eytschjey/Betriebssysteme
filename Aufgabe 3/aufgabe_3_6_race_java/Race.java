/**
 * Aufgabe 3.6: Thread-Klasse Race.
 * Durchläuft in run() 10-mal eine Schleife, in der zuerst der globale Wert j gesetzt
 * und anschließend die Operation k = k + j durchgeführt wird.
 */
public class Race extends Thread {
    private final int targetJ;
    private final boolean useYield;
    private final boolean verbose;

    public Race(String name, int targetJ, boolean useYield, boolean verbose) {
        super(name);
        this.targetJ = targetJ;
        this.useYield = useYield;
        this.verbose = verbose;
    }

    public Race(int targetJ) {
        this("RaceThread(" + targetJ + ")", targetJ, false, false);
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            // Operation 1: globalen Wert j setzen
            GlobalContainer.j = this.targetJ;
            if (useYield) {
                Thread.yield();
            }

            // Operation 2: k = k + j berechnen
            GlobalContainer.k = GlobalContainer.k + GlobalContainer.j;
            if (verbose) {
                System.out.printf("  [%s | Schritt %2d] setzte j=%2d -> aktuelles j=%2d, k=%2d%n",
                        getName(), i, targetJ, GlobalContainer.j, GlobalContainer.k);
            }
            if (useYield) {
                Thread.yield();
            }
        }
    }
}
