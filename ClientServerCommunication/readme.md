## Message Oriented Client Server Communication

This task implements a distributed system consisting of client-server communication and remote procedure call (RPC) based communication. The system includes a file upload and download service and a computation service.

P1 is about implementing a basic single-threaded file server. It supports four operations: UPLOAD, DOWNLOAD, DELETE, and RENAME. The server uses message-oriented communication protocol.

P2 builds upon the single-threaded server by implementing a multi-threaded file server. The server now supports multiple concurrent operations without considering locking at this stage.

P3 introduces synchronous RPCs for the computation server. Four RPCs are supported: calculate_pi(), add(i, j), sort(arrayA), and matrix_multiply(matrixA, matrixB, matrixC). Client and server stubs are implemented to pack and unpack parameters for communication.

P4 re-implements the computation server using asynchronous and deferred synchronous RPCs. For asynchronous RPCs, the server immediately acknowledges a call before performing a computation and saves the result for later lookup by the client. Deferred synchronous RPCs interrupt the client to return the result when the server completes its local computation.
