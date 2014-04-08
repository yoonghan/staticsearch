Static Search in Memory
=======================

Search Engine using MapReduce Method.

Client request for a page searching(on WAP) for static pages. 
As those pages are few, about 400 pages we decided not to use elastic search, instead to have an in memory search. So
texts to be search in the pages are loop and stored into the memory.

Using map reduce method, the processes are fork -> search -> reduced to generate the result.

Test case is created base Guava Cache engine.
In the real environment, the cached is stored into Memcache. Which is more efficient and cleaning the memory
using the command "service memcached restart" is more convinient.

This program is a standalone program and is easily pluggable as a jar.
