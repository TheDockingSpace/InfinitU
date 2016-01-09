# InfinitU - Infinite Universes

> Cogito ergo sum
> 
> -- <cite>René Descartes</cite>

What if in the 17th century we had current technology instead of pen and paper? This project is a exploration of ideas using programming without starting from replicating other implementations or theories (I am not a mathematician).

The idea is to create a data structures independent of the usual collection framework concepts, which usually establish what lists or maps are, but leave out concepts like dimensions out or implicit.

        One of the objectives of this project 
        is also exploring the limits of the Scala
        language, starting from small, somewhat 
        trivial concepts and someday getting
        to macros and all (roadmap yet to be 
        defined)

There are some implementations around for infinite collections, but although it will also be supported here, the focus is on being able of representing an unknown amount of objects in any position expected to be valid by definition in a certain dimension type.

This is not limited to a 3-dimensional space, but taking that as an example, the library could be used to define space with arbitrary precision like BigDecimal allows to numbers.

The structures should be convertible to traditional ones with some loss of precision when required, so a 2 dimension Universe with 0.5 precision would become a Vector of Vector where values from 0 and 0.5 would go to 0, 1 and 1.5 to 1, and so on.

In a first version, will experiment with a very prototype modelling, trying to solve some simple cube puzzle.

Then next iterations should refine the model exploring other problems.
