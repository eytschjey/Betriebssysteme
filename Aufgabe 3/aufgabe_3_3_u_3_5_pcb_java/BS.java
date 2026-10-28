import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Aufgabe 3.5: Implementierung des Betriebssystems (BS) mit Prozesstabelle
 * und den Systemaufrufen createProcess(), forkProcess(), killProcess() sowie printSystemState().
 */
public class BS implements IBSProcess {
    private final Map<Integer, ProcessControlBlock> processTable = new LinkedHashMap<>();
    private int nextPid = 1;

    @Override
    public ProcessControlBlock createProcess(String executable, int userId, int priority) {
        int pid = nextPid++;
        ProcessMetadata metadata = new ProcessMetadata(pid, 0, userId, userId, priority, ProcessState.READY);
        ProcessThread mainThread = new ProcessThread(1, ProcessState.READY, 0x400000L, 0x7FFFFFFFL);
        mainThread.setRegister(0, pid); // Beispielhaft PID in R0 hinterlegen
        ProcessAddressSpace addressSpace = new ProcessAddressSpace();
        ProcessFiles files = new ProcessFiles("/dev/stdin", "/dev/stdout", "/dev/stderr", executable);

        ProcessControlBlock pcb = new ProcessControlBlock(metadata, mainThread, addressSpace, files);
        processTable.put(pid, pcb);
        System.out.printf("[SYSCALL createProcess] Prozess erstellt: PID=%d, Executable='%s'%n", pid, executable);
        return pcb;
    }

    @Override
    public ProcessControlBlock createProcess() {
        return createProcess("/bin/app_" + nextPid, 1000, 10);
    }

    @Override
    public ProcessControlBlock forkProcess(int parentPid) {
        ProcessControlBlock parent = processTable.get(parentPid);
        if (parent == null) {
            System.out.printf("[SYSCALL forkProcess] FEHLER: Elternprozess mit PID=%d existiert nicht!%n", parentPid);
            return null;
        }
        int childPid = nextPid++;
        ProcessControlBlock child = new ProcessControlBlock(parent, childPid);
        // Beim echten fork() erhält das Kind im Rückgaberegister 0, der Vater die Kind-PID
        if (child.getMainThread() != null) {
            child.getMainThread().setRegister(0, 0L);
        }
        if (parent.getMainThread() != null) {
            parent.getMainThread().setRegister(0, childPid);
        }
        processTable.put(childPid, child);
        System.out.printf("[SYSCALL forkProcess] Prozess PID=%d hat Kindprozess PID=%d erzeugt.%n", parentPid, childPid);
        return child;
    }

    @Override
    public boolean killProcess(int pid) {
        ProcessControlBlock removed = processTable.remove(pid);
        if (removed != null) {
            System.out.printf("[SYSCALL killProcess] Prozess PID=%d (PPID=%d, Exec='%s') wurde beendet und entfernt.%n",
                    pid, removed.getMetadata().getParentPid(), removed.getFiles().getExecutable());
            return true;
        } else {
            System.out.printf("[SYSCALL killProcess] FEHLER: Prozess PID=%d nicht gefunden!%n", pid);
            return false;
        }
    }

    @Override
    public void printSystemState() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("Aktuelle Prozesstabelle (Anzahl aktiver Prozesse: %d):%n", processTable.size());
        System.out.println("PID\tPPID\tZustand\tPrio\tReg[R0]\tExecutable");
        System.out.println("--------------------------------------------------------------------------------");
        for (ProcessControlBlock pcb : processTable.values()) {
            ProcessMetadata meta = pcb.getMetadata();
            ProcessThread thread = pcb.getMainThread();
            long r0 = (thread != null) ? thread.getRegister(0) : 0L;
            System.out.printf("%d\t%d\t%s\t%d\t0x%X\t%s%n",
                    meta.getPid(),
                    meta.getParentPid(),
                    meta.getState(),
                    meta.getPriority(),
                    r0,
                    pcb.getFiles().getExecutable()
            );
        }
        System.out.println("--------------------------------------------------------------------------------");
    }
}
