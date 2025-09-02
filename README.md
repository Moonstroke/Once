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
    		<version>0.1</version>
    	<dependency>
    </dependencies>

## Features

The project provides the class `StableField`, a generic container for a value
that can be initialized only once. It defines a setter, which throws a runtime
exception the second (and later) time it is called, a getter, which throws an
exception if the setter has not been called beforehand, as well as non-throwing
counterparts for both methods.

The class also overrides the `Object` methods `hashCode`, `equals` and
`toString`. `hashCode` returns a numeric value based on the field's name and the
contained value, if initialized; `equals` returns true iff the object compared
to is also an instance of `StableField`, its name is the same as the object
compared against, and either both contained values are not initialized, or both
values compare equal (according to their `equals` method).

### `Requirement`s

The `StableField` constructor accepts a dynamic number of *requirements* that a
value passed to any setter must satisfy in order to be successfully assigned.
These are implementations of a new functional interface: `Requirement`, whose
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

The class is fully thread-safe: initialization of the value by one thread will
be immediately visible to others (no race condition). Thus instances can be
shared among threads without the need for external synchronization.

### Full test coverage

The class is fully[^1] covered using JUnit 5 tests, which strive to check common use
cases as well as more obscure corner cases.


[^1]: Almost.  The coverage for `StableField` setter methods using the
double-check locking pattern is not total, as synchronization is inherently
difficult to cover systematically. There is a way to cover the methods using
synchronization, explained [here][2], but it involves making a `private` field
`public`, and because of this it will not be made part of the repository.

[1]: https://download.java.net/java/early_access/jdk25/docs/api/java.base/java/lang/StableValue.html "Official documentation for StableValue"
[2]: https://github.com/Moonstroke/Once/commit/42e94d9bbc147b1a037bb94f27ebf6ae5121bc86
