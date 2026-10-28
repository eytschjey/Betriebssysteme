import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Prozesskontrollblock (PCB), der alle Informationen eines Prozesses bündelt (Aufgabe 3.3).
 * Unterstützt konzeptionell mehrere Threads pro Prozess (Liste von ProcessThread),
 * bietet für den ersten Schritt jedoch direkt einen Konstruktor für 1 Thread pro Prozess.
 */
public class ProcessControlBlock {
    private final ProcessMetadata metadata;
    private final List<ProcessThread> threads;
    private final ProcessAddressSpace addressSpace;
    private final ProcessFiles files;

    /**
     * Konstruktor für 1 Thread pro Prozess (erster Schritt gemäß Aufgabe 3.3),
     * intern bereits als Liste gespeichert für spätere Multi-Threading-Erweiterung.
     */
    public ProcessControlBlock(
            ProcessMetadata metadata,
            ProcessThread mainThread,
            ProcessAddressSpace addressSpace,
            ProcessFiles files) {
        this.metadata = metadata;
        this.threads = new ArrayList<>();
        if (mainThread != null) {
            this.threads.add(mainThread);
        }
        this.addressSpace = addressSpace;
        this.files = files;
    }

    /**
     * Copy-Konstruktor für forkProcess() (Aufgabe 3.5).
     * Erstellt eine Kopie des Elternprozesses mit neuer PID und gesetzter PPID.
     */
    public ProcessControlBlock(ProcessControlBlock parent, int newPid) {
        this.metadata = new ProcessMetadata(parent.metadata, newPid, parent.metadata.getPid());
        this.threads = new ArrayList<>();
        for (ProcessThread t : parent.threads) {
            this.threads.add(new ProcessThread(t));
        }
        this.addressSpace = new ProcessAddressSpace(parent.addressSpace);
        this.files = new ProcessFiles(parent.files);
    }

    public ProcessMetadata getMetadata() {
        return metadata;
    }

    public ProcessThread getMainThread() {
        return threads.isEmpty() ? null : threads.get(0);
    }

    public List<ProcessThread> getThreads() {
        return Collections.unmodifiableList(threads);
    }

    public void addThread(ProcessThread thread) {
        this.threads.add(thread);
    }

    public ProcessAddressSpace getAddressSpace() {
        return addressSpace;
    }

    public ProcessFiles getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return String.format(
                "PCB [PID=%d] {\n  %s\n  Threads(%d)=%s\n  %s\n  %s\n}",
                metadata.getPid(), metadata, threads.size(), threads, addressSpace, files
        );
    }
}
