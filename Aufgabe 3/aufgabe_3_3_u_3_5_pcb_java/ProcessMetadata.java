/**
 * Kapselt die Prozessverwaltungs-Metadaten eines Prozesskontrollblocks (PCB) (Folie 142).
 */
public class ProcessMetadata {
    private int pid;
    private int parentPid;
    private int userId;
    private int groupId;
    private int priority;
    private long cpuTimeUsed;
    private long psw; // Program Status Word
    private ProcessState state;

    public ProcessMetadata(int pid, int parentPid, int userId, int groupId, int priority, ProcessState state) {
        this.pid = pid;
        this.parentPid = parentPid;
        this.userId = userId;
        this.groupId = groupId;
        this.priority = priority;
        this.cpuTimeUsed = 0L;
        this.psw = 0L;
        this.state = state;
    }

    /**
     * Copy-Konstruktor für forkProcess() mit neuer PID und neuer Eltern-PID.
     */
    public ProcessMetadata(ProcessMetadata other, int newPid, int newParentPid) {
        this.pid = newPid;
        this.parentPid = newParentPid;
        this.userId = other.userId;
        this.groupId = other.groupId;
        this.priority = other.priority;
        this.cpuTimeUsed = 0L; // Neues Kind startet mit 0 verbrauchter CPU-Zeit
        this.psw = other.psw;
        this.state = ProcessState.READY;
    }

    public int getPid() {
        return pid;
    }

    public int getParentPid() {
        return parentPid;
    }

    public int getUserId() {
        return userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getCpuTimeUsed() {
        return cpuTimeUsed;
    }

    public void setCpuTimeUsed(long cpuTimeUsed) {
        this.cpuTimeUsed = cpuTimeUsed;
    }

    public long getPsw() {
        return psw;
    }

    public void setPsw(long psw) {
        this.psw = psw;
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return String.format(
                "ProcessMetadata{PID=%d, PPID=%d, UID=%d, GID=%d, Prio=%d, CPU=%dms, PSW=0x%X, State=%s}",
                pid, parentPid, userId, groupId, priority, cpuTimeUsed, psw, state
        );
    }
}
