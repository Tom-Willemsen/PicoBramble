# PicoBramble
An unfinished (and therefore unstable) lightweight library for running jobs across multiple machines. Originally designed for a cluster of Raspberry Pis.

There are more complete, and almost certainly faster libraries out there for cluster computing. This one was built as a learning exercise, and as such, ought to be relatively easy to understand.


The basic idea consists of three components, which a program is required to provide:
- A Job provider. This defines the tasks to be run, and runs on the master node physically (but on a seperate thread).
- One or more slave nodes. These run the jobs.
- The master node. This collects data from the slave nodes once they have finished their jobs.

As far as possible, "magical" external libraries have been avoided. The exception is a replacement for Java's slow serialization methods, which I've replaced with [FST](https://github.com/RuedigerMoeller/fast-serialization). This is a drop-in replacement for Java's `ObjectInputStream` and `ObjectOutputStream` methods, so it's easy to go back to Java's native methods if required.
