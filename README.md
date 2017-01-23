# PicoBramble
An unfinished (and therefore unstable) lightweight API for running jobs across multiple machines. Originally designed for a cluster of Raspberry Pis.

There are more complete, and almost certainly faster libraries out there for cluster computing. This one was built as a learning exercise, and as such, ought to be relatively easy to understand.

As far as possible, "magical" external libraries have been avoided. The exception is a replacement for Java's slow serialization methods, which I've replaced with [FST](https://github.com/RuedigerMoeller/fast-serialization).
