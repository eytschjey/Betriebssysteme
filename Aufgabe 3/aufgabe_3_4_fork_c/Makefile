CC = gcc
CFLAGS = -Wall -Wextra -O2

all: fork_loop

fork_loop: fork_loop.c
	$(CC) $(CFLAGS) -o fork_loop fork_loop.c

run: fork_loop
	./fork_loop

clean:
	rm -f fork_loop
