	Writer-Preferred Method 1 Implementation with Semaphores:
The use of semaphores in this implementation makes the code appear more complex,
but it allows for easier extension of the code when needed compared to using wait()
or notifyAll().

Writer-Preferred Method 2 Implementation with Synchronized Blocks, wait(), notify(), and notifyAll():
In this algorithm, the code becomes clearer and easier to follow. However, if we try
to extend this code with new functionalities, we may lose some of the previously
mentioned advantages and performance, due to the excessive use of notify() and notifyAll().

Performance Comparison Between Methods:
In the case of Method 1, if there are a large number of tasks and threads, semaphores are
more efficient because they allow each thread to execute one by one. On the other hand,
using notifyAll() would take more time to notify all participants (threads). However, if
the number of threads is smaller, regardless of the number of tasks, notifyAll() would be
faster than using sem.acquire() or sem.release(), as they represent repetitive methods that
may decrease performance in such a case.

Reader and Writer Starvation:
In the case of reader prioritization, we observe the phenomenon of "writer starvation," while
the opposite prioritization leads to "reader starvation." In both of these cases, there is an
imbalance, and we could implement a solution that provides priority to readers/writers after a
certain number of reads/writes or change priorities depending on how long writers/readers
have been waiting.

Testing Method:
The implementation uses reads/writes of varying lengths, which gives a clearer picture of
what happens under different load scenarios.

For different reader/writer ratios and prioritizations with a large number of tasks,
we can easily observe the correctness of the implementation for various prioritization scenarios.

The latency added to the read and write operations is a good method to ensure realism in verifying the implementation proposed by students.

Additionally, the time constraint for task execution imposed strict rules on the implementation. Therefore, to fit within the time limit, a parallelization algorithm was required for each database index, making the process as efficient as possible.