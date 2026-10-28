/**
 * Aufgabe 3.3: Einfaches Beispielprogramm, das einen Prozess-Kontroll-Block (PCB)
 * mit Beispieldaten erstellt und auf der Konsole ausgibt.
 */
public class MainAufgabe33 {
    public static void main(String[] args) {
        System.out.println("=== Aufgabe 3.3: Beispiel für einen Prozess-Kontroll-Block (PCB) ===");

        // 1. Prozess-Metadaten anlegen (PID=101, PPID=1, UID=1000, GID=1000, Priorität=20, Zustand=RUNNING)
        ProcessMetadata metadata = new ProcessMetadata(101, 1, 1000, 1000, 20, ProcessState.RUNNING);
        metadata.setCpuTimeUsed(42L);
        metadata.setPsw(0x00000202L);

        // 2. Haupt-Thread für 64-Bit CPU mit 16 Registern erstellen
        ProcessThread mainThread = new ProcessThread(1, ProcessState.RUNNING, 0x4005A0L, 0x7FFF0000L);
        mainThread.setRegister(0, 1L);          // R0
        mainThread.setRegister(1, 0x1000L);     // R1
        mainThread.setRegister(2, 0xABCDEF01L); // R2
        mainThread.setRegister(15, 0x7FFF0000L);// R15

        // 3. Adressraum (Platzhalter) und Prozess-Dateien erstellen
        ProcessAddressSpace addressSpace = new ProcessAddressSpace();
        ProcessFiles files = new ProcessFiles(
                "/dev/pts/0 (stdin)",
                "/dev/pts/0 (stdout)",
                "/dev/pts/0 (stderr)",
                "/usr/bin/texteditor"
        );

        // 4. PCB zusammensetzen und ausgeben
        ProcessControlBlock pcb = new ProcessControlBlock(metadata, mainThread, addressSpace, files);
        System.out.println(pcb);
    }
}
