## Multicasting-Locking

This task involves developing an n-node distributed system that supports totally ordered events and a distributed locking scheme. The distributed system utilizes logical clocks to timestamp messages exchanged between nodes. A total order of events is achieved through totally ordered multicasting and vector clocks. Additionally, a distributed locking scheme is implemented to prevent concurrent accesses to a shared file.

P1 executes totally ordered multicasting using Lamport’s algorithm. Each process conducts local operations and numbers them as PID.EVENT_ID. After each operation is done, a process multicasts the event to all other processes in the distributed system. The expected outcome is that events occurred at different processes will appear in the same order at each individual process.

P2 applies the vector clock algorithm to enable causally-ordered events in the distributed system. Similar to the previous assignment, multiple processes emulate multiple nodes. After completing a local operation, each process sends its updated vector clock to all other processes.

P3 carries out a locking scheme to protect a shared file in the distributed system. Choose one of the following distributed locking schemes: decentralized, distributed locking, or the token ring algorithm. When a process acquires the lock, it opens the file, increments a counter in the file, and then closes it.
