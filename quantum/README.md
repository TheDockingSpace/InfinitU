> Attention: This library is in a very early experimental stage.
> Not meant for production but currently to incrementally explore the possibilities of bridging the gap between classical and quantum from a radically different perspective
>
> \- HERE BE DRAGONS \-

What if quantum concepts were as easy to use as the ones in mainstream programming languages are today? What if the learning curve required to take profit from it was as low as explaining the [Schrödinger's Cat experiment in a way that even a 6yo could understand](https://www.youtube.com/watch?v=IOYyCHGWJq4)?

Other libraries I've seen come from very low level quantum physics concepts, which is ok depending on what kind of research you are doing.

Comparing to traditional software development stacks, from frameworks down to the hardware, usually you wont find every possible low level gate, operation or CPU vendor specific circuitry immediately accessible in the programming languages we use to write most of the software solutions developed in the world today.

IMHO this takes to some consequences which in a way hold back the evolution of the technology as a whole, especially because the learning curve that is practically impossible to climb for most devs in the world

Today we have many mainstream languages with different design decisions and foundation concepts like object orientation or functional programming, interpreted or compiled, static or dynamic, and so on.

The idea here is, what if we consider that a type from one point of view not only describes all possible values of it but actually IS all values in a superpositioned state? Then when we need it to collapse to a specific value we just call *Boolean.collapse*?

Sounds straightforward right? The code for this first experiment is ready [now](https://github.com/TheDockingSpace/InfinitU/blob/e39188af054a3716db31f59b85160dd5ecba63d9/quantum/js/src/main/scala/space/thedocking/infinitu/quantum/App.scala#L26)

If you already know any programming language you will probably agree that this is not harder - or maybe even easier - then calling some function to get a random number and converting it to a boolean value, which is actually what is happening in the current code.

What is the difference then? Imagine you want to run the same block in a quantum computer but you don't have the time to learn its internals just as you probably don't have the time to learn the internals of a x86_64 CPU.

You don't want to know that your code needs to be translated to QASM, allocate a qubit as *|0>*, apply a *Hadamard gate*, *measure* the output and *serialize* the result back to your main program flow. You just want to *configure* your *high level code* to run in some quantum runtime.

That is exactly where this code is going. It opens the doors to discuss and productively experiment very interesting combinations between quantum and recursion, simulation, genetic algorithms, neural networks, fractals...

Stay tuned :)