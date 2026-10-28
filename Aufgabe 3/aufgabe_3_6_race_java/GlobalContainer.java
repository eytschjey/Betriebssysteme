/**
 * Aufgabe 3.6: Container-Klasse für die zwei globalen (statischen) Variablen j und k.
 */
public class GlobalContainer {
    public static int j = 0;
    public static int k = 5;

    /**
     * Setzt die globalen Variablen für Wiederholungsläufe auf ihre Startwerte zurück.
     */
    public static void reset() {
        j = 0;
        k = 5;
    }
}
