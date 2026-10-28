/**
 * Aufgabe 3.6: Hauptprogramm zur Untersuchung von Race Conditions
 * ohne und mit Thread.yield().
 */
public class MainAufgabe36 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("======================================================================");
        System.out.println("Aufgabe 3.6: Java Threads / Race Conditions (Startwerte: j=0, k=5)");
        System.out.println("Erwarteter Wert ohne Race Condition nach +10*(+1) und +10*(-1): k = 5");
        System.out.println("======================================================================\n");

        System.out.println("--- TEIL A: 20 Durchläufe OHNE Thread.yield() ---");
        int abweichungenOhneYield = 0;
        for (int run = 1; run <= 20; run++) {
            GlobalContainer.reset();
            Race t1 = new Race("Thread+1", 1, false, false);
            Race t2 = new Race("Thread-1", -1, false, false);

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            if (GlobalContainer.k != 5) {
                abweichungenOhneYield++;
            }
            System.out.printf("Lauf %2d (ohne yield): Endwert k = %3d, j = %2d %s%n",
                    run, GlobalContainer.k, GlobalContainer.j,
                    (GlobalContainer.k != 5 ? "<-- RACE CONDITION!" : ""));
        }
        System.out.printf("-> Abweichungen vom erwarteten Wert (k=5) ohne yield: %d von 20 Läufen%n%n",
                abweichungenOhneYield);

        System.out.println("--- TEIL B: 20 Durchläufe MIT Thread.yield() nach jeder Operation ---");
        int abweichungenMitYield = 0;
        for (int run = 1; run <= 20; run++) {
            GlobalContainer.reset();
            Race t1 = new Race("Thread+1", 1, true, false);
            Race t2 = new Race("Thread-1", -1, true, false);

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            if (GlobalContainer.k != 5) {
                abweichungenMitYield++;
            }
            System.out.printf("Lauf %2d (mit yield):  Endwert k = %3d, j = %2d %s%n",
                    run, GlobalContainer.k, GlobalContainer.j,
                    (GlobalContainer.k != 5 ? "<-- RACE CONDITION!" : ""));
        }
        System.out.printf("-> Abweichungen vom erwarteten Wert (k=5) mit yield:  %d von 20 Läufen%n%n",
                abweichungenMitYield);

        System.out.println("--- TEIL C: Detailprotokoll eines einzelnen Laufs MIT Thread.yield() ---");
        GlobalContainer.reset();
        Race t1Detail = new Race("Thread+1", 1, true, true);
        Race t2Detail = new Race("Thread-1", -1, true, true);
        t1Detail.start();
        t2Detail.start();
        t1Detail.join();
        t2Detail.join();
        System.out.printf("Endstand Detail-Lauf: j = %d, k = %d%n", GlobalContainer.j, GlobalContainer.k);
    }
}
