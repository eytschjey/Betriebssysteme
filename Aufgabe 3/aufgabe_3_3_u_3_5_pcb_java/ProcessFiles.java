/**
 * Kapselt die Dateiverwaltung eines Prozesses mit Platzhaltern für
 * StdInput, StdOutput, StdError und die Executable-Datei (Aufgabe 3.3).
 */
public class ProcessFiles {
    private String stdInput;
    private String stdOutput;
    private String stdError;
    private String executable;

    public ProcessFiles(String stdInput, String stdOutput, String stdError, String executable) {
        this.stdInput = stdInput;
        this.stdOutput = stdOutput;
        this.stdError = stdError;
        this.executable = executable;
    }

    /**
     * Copy-Konstruktor für forkProcess() (Kindprozess erbt offene Dateideskriptoren/Dateien).
     */
    public ProcessFiles(ProcessFiles other) {
        this.stdInput = other.stdInput;
        this.stdOutput = other.stdOutput;
        this.stdError = other.stdError;
        this.executable = other.executable;
    }

    public String getStdInput() {
        return stdInput;
    }

    public void setStdInput(String stdInput) {
        this.stdInput = stdInput;
    }

    public String getStdOutput() {
        return stdOutput;
    }

    public void setStdOutput(String stdOutput) {
        this.stdOutput = stdOutput;
    }

    public String getStdError() {
        return stdError;
    }

    public void setStdError(String stdError) {
        this.stdError = stdError;
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    @Override
    public String toString() {
        return String.format(
                "ProcessFiles{StdIn='%s', StdOut='%s', StdErr='%s', Executable='%s'}",
                stdInput, stdOutput, stdError, executable
        );
    }
}
