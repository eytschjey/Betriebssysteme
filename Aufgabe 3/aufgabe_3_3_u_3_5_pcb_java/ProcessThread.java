import java.util.Arrays;

/**
 * Kapselt die Ausführungs- und Zustandsinformationen eines Threads (Folie 142 & 150).
 * Verwaltet u.a. die 16 Register einer 64-Bit CPU (long[16]), Befehlszähler, Stack und Zustand.
 */
public class ProcessThread {
    public static final int NUM_REGISTERS = 16;

    private final int threadId;
    private ProcessState state;
    private long programCounter;
    private long stackPointer;
    private final long[] registers; // 16 Register à 64-Bit

    public ProcessThread(int threadId, ProcessState state, long programCounter, long stackPointer) {
        this.threadId = threadId;
        this.state = state;
        this.programCounter = programCounter;
        this.stackPointer = stackPointer;
        this.registers = new long[NUM_REGISTERS];
    }

    /**
     * Copy-Konstruktor für forkProcess() (kopiert alle 16 Registerinhalte sowie PC und SP).
     */
    public ProcessThread(ProcessThread other) {
        this.threadId = other.threadId;
        this.state = ProcessState.READY;
        this.programCounter = other.programCounter;
        this.stackPointer = other.stackPointer;
        this.registers = Arrays.copyOf(other.registers, NUM_REGISTERS);
    }

    public int getThreadId() {
        return threadId;
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public long getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(long programCounter) {
        this.programCounter = programCounter;
    }

    public long getStackPointer() {
        return stackPointer;
    }

    public void setStackPointer(long stackPointer) {
        this.stackPointer = stackPointer;
    }

    public long getRegister(int index) {
        checkRegisterIndex(index);
        return registers[index];
    }

    public void setRegister(int index, long value) {
        checkRegisterIndex(index);
        registers[index] = value;
    }

    public long[] getRegisters() {
        return Arrays.copyOf(registers, NUM_REGISTERS);
    }

    private void checkRegisterIndex(int index) {
        if (index < 0 || index >= NUM_REGISTERS) {
            throw new IllegalArgumentException("Ungültiger Registerindex: " + index + " (erlaubt: 0..15)");
        }
    }

    @Override
    public String toString() {
        StringBuilder regStr = new StringBuilder("[");
        for (int i = 0; i < NUM_REGISTERS; i++) {
            if (i > 0) regStr.append(", ");
            regStr.append(String.format("R%d=0x%X", i, registers[i]));
        }
        regStr.append("]");
        return String.format(
                "ProcessThread{TID=%d, State=%s, PC=0x%X, SP=0x%X, Registers=%s}",
                threadId, state, programCounter, stackPointer, regStr
        );
    }
}
