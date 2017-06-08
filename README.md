# InfinitU - Infinite Universes

[![Join the chat at https://gitter.im/TheDockingSpace/InfinitU](https://badges.gitter.im/TheDockingSpace/InfinitU.svg)](https://gitter.im/TheDockingSpace/InfinitU?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

> Cogito ergo sum
> 
> -- <cite>René Descartes</cite>

What if in the 17th century we had current technology instead of pen and paper? This project is an exploration of ideas using programming without starting from replicating other implementations or theories (I am not a mathematician).

The idea is to create a data structures independent of the collection framework concepts, which usually establish what lists or maps are, but leave out concepts like dimensions.

There are some implementations around for infinite collections, but although it will also be supported here, the focus is on being able of representing an unknown amount of objects in any position expected to be valid by definition in a certain dimension type.

This is not limited to a 3-dimensional space, but taking that as an example, the library could be used to define space with arbitrary precision like BigDecimal allows to numbers.

The structures should be convertible to traditional ones with some loss of precision when required, so a 2 dimension Universe with 0.5 precision would become a Vector of Vector where values from 0 and 0.5 would go to 0, 1 and 1.5 to 1, and so on.

In the first release I explore a prototypical modeling that can be used to solve things like cube puzzles but I wanted to explore something more interesting: Quantum Mechanics

## Quantum

Currently the implementation focusses on 3 main concepts: Superposition, Entanglement and Collapsation.

Without going too deep into the theoretical physics that surround them, the initial abstractions would allow exploration of even more interesting directions, turning weird and counter-intuitive concepts into manageable, understandable and hopefully useful.

The project was configured and organized in a way that you can run it on the JVM([ ![JVM](https://api.bintray.com/packages/thedockingspace/Universe/quantumjvm/images/download.svg) ](https://bintray.com/thedockingspace/Universe/quantumjvm/_latestVersion)), on the browser via a Scala.js cross build ([ ![Scala.js version](https://api.bintray.com/packages/thedockingspace/Universe/quantumjs/images/download.svg) ](https://bintray.com/thedockingspace/Universe/quantumjs/_latestVersion)), and eventually even natively.

Stay tuned for more details soon.

Meanwhile you can have a look on [how to check if the cat in the box is alive](https://github.com/TheDockingSpace/InfinitU/blob/e39188af054a3716db31f59b85160dd5ecba63d9/quantum/js/src/main/scala/space/thedocking/infinitu/quantum/App.scala#L26) :)
