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
    		<version>0.1-SNAPSHOT</version>
    	<dependency>
    </dependencies>

## Features

The project provides the class `StableField`, a generic container for a value
that can be initialized only once. It defines a setter, which throws a runtime
exception the second (and later) time it is called, a getter, which throws an
exception if the setter has not been called beforehand, as well as non-throwing
counterparts for both methods.

The class also overrides the `Object` methods `hashCode`, `equals` and
`toString`. `hashCode` returns the contained object's hash code (or `0` if it
not initialized); `equals` returns true iff the object compared to is also an
instance of `StableField` and either both contained values are not initialized,
or both values compare equal (according to their `equals` method).

The class is fully covered using JUnit 5 tests, which strive to check common use
cases as well as more obscure corner cases.


[1]: https://download.java.net/java/early_access/jdk25/docs/api/java.base/java/lang/StableValue.html "Official documentation for StableValue"
