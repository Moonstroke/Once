<!-- SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
     SPDX-License-Identifier: MIT -->

# Changelog

## (2025/09/25) version 0.3

### New (sub)class: `SharedStableField`

This new class is a child of the original `StableField` one. It inherits the
thread-safety characteristic of its parent, which itself is reverted to a basic,
not-thread-safe implementation. This allows users to opt into thread safety at
their discretion, and the choice is explicit (using the new class' name); and if
concurrency is not required, no runtime overhead is imposed.

### New feature: functional API

Methods are added to `StableField` (and inherited in the new subclass),
providing a functional-like interface:
- `Optional<T> getOpt()`
- `Optional<R> <R> map(Function<T, R>)`
- `void ifSet(Consumer<T>)`

### Minor changes

The member `set` is now taken into account when comparing two `StableField`
objects. This fixes a corner case where an instance explicitly set to `null`
(because it has the `ALLOW_NULL` requirement set) compares equals to an unset
field instance.

A test method for `Requirements.matches(String)` is fixed: it (inadvertently)
used the overload accepting a `Pattern`.

The code now makes systematic use of [LVTI](https://openjdk.org/jeps/286). This
is most notable in tests, which almost all have at least one local variable (the
instance under test).

## (2025/09/01) version 0.2

### New feature: requirements

The functional interface `Requirement` is added. It represents a business rule
that a candidate value to a setter must satisfy in order to be successfully set.
A stable field's requirements are defined at creation time: the constructor
accepts a variable number of them (including none).
The helper class `Requirements` provides a set of useful, base requirements on
primitive types, strings and collections.

### Minor changes

- The getter method without a default value now throws `NoSuchElementException`
  instead of `IllegalStateException` when the value has not been set. This
  conforms to the behavior of standard classes (e.g. `Optional`).
- The member name is now taken into account in methods `hashCode` and `equals`.
  This means that the former never returns `0`, and the latter only returns
  `true` when the argument is a `StableField` instance with the same name, and
  an equal value (or both unset).

## (2025/08/24) version 0.1

First version 🎉

Available in [Maven central](https://repo.maven.apache.org/maven2/io/github/moonstroke/once/0.1/).

Declares the `StableField` class, a generic, thread-safe container for one value
that can be initialized only once.

Also includes unit tests with full coverage, as technical (code) as functional
(use cases, including corner ones).
