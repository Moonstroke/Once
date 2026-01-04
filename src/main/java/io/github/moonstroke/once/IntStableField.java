/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

/**
 * A special container for a single value, allowing only a single initialization.
 */
public class IntStableField {

	/* The name of the field. Used in error messages and toString representation */
	protected final String name;
	protected boolean set;
	protected int value;
	private final List<Requirement<? super Integer>> requirements;


	/**
	 * Create a field of given name that can only be set once.
	 *
	 * @param name         The name of the field
	 * @param requirements The requirements that the value must meet before being set
	 *
	 * @throws NullPointerException     if any parameter is {@code null}
	 * @throws IllegalArgumentException if name is empty
	 */
	@SafeVarargs
	public IntStableField(String name, Requirement<? super Integer>... requirements) {
		if (name == null) {
			throw new NullPointerException("Cannot have a null name");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("Cannot have an empty name");
		}
		this.name = name;
		var reqs = Arrays.asList(requirements);
		if (reqs.contains(null)) {
			throw new NullPointerException(name + "cannot have a null requirement");
		}
		if (reqs.contains(Requirements.ALLOW_NULL)) {
			throw new IllegalArgumentException("Primitive field " + name + " cannot have the ALLOW_NULL requirement");
		}
		/* Store a copy to avoid a dependency on the caller-provided array */
		this.requirements = new ArrayList<>(reqs);
	}

	/* Ensure that the given value is eligible for being contained by this instance. Does not check whether this
	 * instance already contains a value. */
	private void checkValueToSet(int value) {
		for (var r : requirements) {
			r.check(value);
		}
	}

	protected int getValue() {
		return value;
	}

	protected boolean isSet() {
		return set;
	}

	/**
	 * Initialize the instance's value, or fail if has already been set.
	 *
	 * @param value The value to set
	 *
	 * @throws IllegalArgumentException if the value fails to meet any requirement
	 * @throws IllegalStateException    if the value has already been initialized
	 */
	public void set(int value) {
		checkValueToSet(value);
		if (isSet()) {
			throw new IllegalStateException(name + " is already set");
		}
		doSet(value);
	}

	protected void doSet(int value) {
		this.value = value;
		this.set = true;
	}

	/**
	 * Initialize the instance's value and return whether the operation succeeded.
	 *
	 * @param value The value to set
	 *
	 * @return {@code true} if the value was actually set, {@code false} if it was already set
	 *
	 * @throws IllegalArgumentException if the value fails to meet any requirement
	 */
	public boolean trySet(int value) {
		checkValueToSet(value);
		if (isSet()) {
			return false;
		}
		return doTrySet(value);
	}

	protected boolean doTrySet(int value) {
		this.value = value;
		this.set = true;
		return true;
	}

	/**
	 * Retrieve the instance's value, or fail if it has not been set.
	 *
	 * @return the value set
	 *
	 * @throws NoSuchElementException if the value was not initialized
	 */
	public int get() {
		if (!isSet()) {
			throw new NoSuchElementException(name + " has not been set");
		}
		return getValue();
	}

	/**
	 * Retrieve an optional instance wrapping the object's value if present, or an empty optional otherwise.
	 *
	 * @return an {@link Optional} wrapping the value, or an empty one; never {@code null}
	 */
	public Optional<Integer> getOpt() {
		return isSet() ? Optional.of(value) : Optional.empty();
	}

	/**
	 * Retrieve the instance's value, or return the provided default if unset.
	 *
	 * @param defaultValue The default value, {@code null} accepted
	 *
	 * @return the value set, or the default one if unset
	 */
	public int get(int defaultValue) {
		return isSet() ? getValue() : defaultValue;
	}

	/**
	 * Retrieve the value and transform it using to the given function.
	 *
	 * @param <R>         The output type of the transformation function
	 * @param mapFunction The transformation function
	 *
	 * @return an {@link Optional} containing the transformed value, or an empty one if the function returned
	 *         {@code null}
	 *
	 * @throws NoSuchElementException if the value was not initialized
	 * @throws NullPointerException   if mapFunction is {@code null}
	 *
	 * @apiNote It is deliberate that the function throw an error if the field is unset. It was deemed better than the
	 *          alternatives of passing {@code null} to the function (thereby allowing to simplify the latter by
	 *          removing the need for a {@code null}-check) or returning an empty optional without calling the function
	 *          (which would have made an empty optional return ambiguous). If a non-throwing alternative is required,
	 *          users may instead call the method {@link #getOpt} followed by {@link Optional#map} with the same
	 *          function as its parameter.
	 */
	public <R> Optional<R> map(IntFunction<R> mapFunction) {
		if (mapFunction == null) {
			throw new NullPointerException(name + " cannot be passed to a null function");
		}
		if (!isSet()) {
			throw new NoSuchElementException(name + " has not been set");
		}
		return Optional.ofNullable(mapFunction.apply(getValue()));
	}

	/**
	 * Call the provided function with the value if it is set.
	 *
	 * @param consumer The function to call if the value is set
	 *
	 * @throws NullPointerException if consumer is {@code null}
	 */
	public void ifSet(IntConsumer consumer) {
		if (consumer == null) {
			throw new NullPointerException(name + " cannot be passed to a null consumer");
		}
		if (isSet()) {
			consumer.accept(getValue());
		}
	}

	/**
	 * Return a numeric sum of the object's state, comprising its name and value.
	 *
	 * @return a hash of the instance's name and value members
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, getValue());
	}

	/**
	 * Indicate whether the given object is equal to this instance.
	 *
	 * The two objects are equal if the following holds:
	 * <ul>
	 * <li>it is not {@code null},
	 * <li>it is a {@code StableField} instance,
	 * <li>its {@linkplain #name name member} is equal to this object's name,
	 * <li>and either:
	 * <ul>
	 * <li>its value is unset and this instance's value is unset, or
	 * <li>its value is set to one that compare equal to the one set in this instance.
	 * </ul>
	 * </ul>
	 *
	 * @param o The object to compare
	 *
	 * @return {@code true} if the two objects compare equal
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof IntStableField)) {
			return false;
		}
		var other = (IntStableField) o;
		return name.equals(other.name) && isSet() == other.isSet() && getValue() == other.getValue();
	}

	/**
	 * Return a string representation of this object.
	 *
	 * @return a String representation of this object
	 */
	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append(' ');
		sb.append('"');
		sb.append(name);
		sb.append('"');
		sb.append(' ');
		sb.append('(');
		if (!isSet()) {
			sb.append("not set");
		} else {
			sb.append(Integer.toString(getValue()));
		}
		sb.append(')');
		return sb.toString();
	}
}
