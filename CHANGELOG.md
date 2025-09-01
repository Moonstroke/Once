<!-- SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
     SPDX-License-Identifier: MIT -->

# Changelog

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