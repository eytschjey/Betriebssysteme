#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

/*
 * Aufgabe 3.4: Verwendung von fork()
 * Erzeugt einen Kindprozess. Vater und Kind geben jeweils in einer Schleife
 * mit 500 Durchläufen ihren Schleifenzähler aus.
 */
int main(void) {
    /* Pufferung von stdout deaktivieren, damit Ausgaben sofort sichtbar werden */
    setbuf(stdout, NULL);

    pid_t pid = fork();

    if (pid < 0) {
        perror("Fehler bei fork()");
        return EXIT_FAILURE;
    } else if (pid == 0) {
        /* Kindprozess */
        pid_t my_pid = getpid();
        for (int i = 1; i <= 500; i++) {
            printf("[KIND  | PID=%d] Durchlauf %3d / 500\n", (int)my_pid, i);
        }
    } else {
        /* Vaterprozess */
        pid_t my_pid = getpid();
        for (int i = 1; i <= 500; i++) {
            printf("[VATER | PID=%d] Durchlauf %3d / 500\n", (int)my_pid, i);
        }
        /* Auf Beendigung des Kindprozesses warten, um Zombie-Prozess zu vermeiden */
        wait(NULL);
    }

    return EXIT_SUCCESS;
}
