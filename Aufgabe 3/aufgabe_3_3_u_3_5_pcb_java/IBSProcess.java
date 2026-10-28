/**
 * Aufgabe 3.5: Schnittstelle zur Verwaltung von Prozessen (Systemaufrufe / Systemcalls).
 */
public interface IBSProcess {
    /**
     * Erzeugt einen neuen Prozess im Betriebssystem.
     *
     * @param executable Pfad zur ausführbaren Datei
     * @param userId     Benutzer-ID
     * @param priority   Prozesspriorität
     * @return Der neu erzeugte ProcessControlBlock
     */
    ProcessControlBlock createProcess(String executable, int userId, int priority);

    /**
     * Erzeugt einen neuen Prozess mit Standardwerten.
     *
     * @return Der neu erzeugte ProcessControlBlock
     */
    ProcessControlBlock createProcess();

    /**
     * Kopiert einen bestehenden Elternprozess analog zum UNIX-Systemaufruf fork().
     *
     * @param parentPid Prozess-ID des Elternprozesses
     * @return Der neu erzeugte Kind-ProcessControlBlock
     */
    ProcessControlBlock forkProcess(int parentPid);

    /**
     * Beendet und entfernt einen Prozess anhand seiner Prozess-ID analog zu kill().
     *
     * @param pid Prozess-ID des zu beendenden Prozesses
     * @return true, wenn der Prozess gefunden und entfernt wurde, sonst false
     */
    boolean killProcess(int pid);

    /**
     * Gibt den aktuellen Zustand des Betriebssystems (Prozesstabelle) aus.
     */
    void printSystemState();
}
