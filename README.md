<!-- SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
     SPDX-License-Identifier: MIT -->

# Once - A simple container for fields to set only once

This project provides a Java container class that enforces a single
initialization of its value.

It is similar in concept to Java 25's [`StableValue`][1], but on the one hand,
without the performance improvements perspective offered by being an integral
part of the JDK, and on the other, available as a non-preview feature for
previous versions of the JDK.


## How to use

This project is Maven-based, and is published on the official Maven repository,
Central. To use it, simply declare it as a dependency:


    <dependencies>
    	...
    	<dependency>
    		<groupId>io.github.moonstroke</groupId>
    		<artifactId>once</artifactId>
    		<version>0.3</version>
    	<dependency>
    </dependencies>

## Features

The project provides the class `StableField` and its concurrency-ready subclass
`SharedStableField` (more on this in the dedicated section below), generic
container for a single value that can be initialized only once. They define a
setter, which throws a runtime exception the second (and later) time it is
called, a getter, which throws an exception if the setter has not been called
beforehand, as well as non-throwing counterparts for both methods.

Provided as well is a set of *functional* methods, which allow to interact with
more functional-programming-oriented features of Java: `Optional` and functional
interfaces like `Supplier`, `Function` and `Consumer`. These methods generally
do not throw (unless, for those who accept a functional object,  a `null` is
passed).

The classes also override the `Object` methods `hashCode`, `equals` and
`toString`. `hashCode` returns a numeric value based on the field's name and the
contained value, if initialized; `equals` returns true iff the object compared
to is also an instance of `StableField` (or its subclass), its name is the same
as the object compared against, and either both contained values are not
initialized, or both values compare equal (according to their `equals` method).

### `Requirement`s

Each class' constructor accepts a dynamic number of *requirements* that a value
passed to any setter must satisfy in order to be successfully assigned.
These are implementations of a functional interface called `Requirement`, whose
`check` method by contract accepts the candidate value and throws an
`IlleglArgumentException` if the value does not meet the criterion that the
instance represents.

A couple of helper static methods is defined in the interface to convert a
`Predicate` object into a requirement (with either a custom or default exception
message).

The static class `Requirements` provides a set of useful requirement instances
applicable to the standard types (primitives, strings, collections): emptiness
(for strings and collections), having a specific sign, or falling in a given
range (for numeric types), not being the `NUL` character, matching a regular
expression (for character sequences).

A special constant defined in this class lifts a requirement, instead of adding
one: it is an indicator that the field is nullable, meaning that its presence
instructs the setters to accept a `null` as value (instead of throwing a
`NullPointerException` as they normally do).

This mechanism will allow to define functional rules right at the same place
where the container is defined, allowing to improve the centralization of a
project's business logic.

### Thread-safety

The base class, `StableField`, is not safe for concurrent use. Its derived class
`SharedStableField`, on the other hand, is fully thread-safe: initialization of
the value by one thread will be immediately visible to others (no race
condition). Thus instances can be shared among threads without the need for
external synchronization. This distinction in behavior allows to avoid the
synchronization overhead when it is not required, or to enable explicitly
concurrency-safe behavior, when it is actually intended.

The API (i.e. public methods) for both classes is the same, which means that a
thread-unsafe instance can be made thread-safe (or vice-versa) simply by
changing the class with which it is initialized; this design of enabling
behavior though polymorphism was deemed preferable to a method-based one (that
is, provide a single class which defines two sets of methods: one thread-safe
and the other not, of which respectively `get` and `getShared` for instance)
because it limits the selection of a behavior to a single location.

### Full test coverage

The class is fully[^1] covered using JUnit 5 tests, which strive to check common
use cases as well as more obscure corner cases.


[^1]: Almost.  The coverage for `StableField` setter methods using the
double-check locking pattern is not total, as synchronization is inherently
difficult to cover systematically. There is a way to cover the methods using
synchronization, explained [here][2], but it involves making a `private` field
`public`, and because of this it will not be made part of the repository.

[1]: https://download.java.net/java/early_access/jdk25/docs/api/java.base/java/lang/StableValue.html "Official documentation for StableValue"
[2]: https://github.com/Moonstroke/Once/commit/42e94d9bbc147b1a037bb94f27ebf6ae5121bc86
