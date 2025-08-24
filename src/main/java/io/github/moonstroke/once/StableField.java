/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A special container for a single value, allowing only a single initialization.
 */
public class StableField<T> {

	/* The name of the field. Used in error messages and toString representation */
	private final String name;
	/* Thread-safe reference to the contained value */
	private final AtomicReference<T> valueRef = new AtomicReference<>();


	/**
	 * Create a field of given name that can only be set once.
	 *
	 * @param name The name of the field
	 *
	 * @throws NullPointerException     if name is {@code null}
	 * @throws IllegalArgumentException if name is empty
	 */
	public StableField(String name) {
		if (name == null) {
			throw new NullPointerException("Cannot have a null name");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("Cannot have an empty name");
		}
		this.name = name;
	}

	/* Ensure that the given value is eligible for being contained by this instance. Does not check whether this
	 * instance already contains a value. */
	private void checkValueToSet(T value) {
		if (value == null) {
			throw new NullPointerException(name + "cannot bet set to null");
		}
		if (value == this) {
			throw new IllegalArgumentException(name + "cannot be set to itself");
		}
	}

	/**
	 * Initialize the instance's value, or fail if has already been set.
	 *
	 * @param value The value to set
	 *
	 * @throws IllegalStateException    if the value has already been initialized
	 * @throws IllegalArgumentException if value is {@code this}
	 * @throws NullPointerException     if value is {@code null}
	 */
	public void set(T value) {
		checkValueToSet(value);
		if (!valueRef.compareAndSet(null, value)) {
			throw new IllegalStateException(name + " is already set");
		}
	}

	/**
	 * Initialize the instance's value and return whether the operation succeeded.
	 *
	 * @param value The value to set
	 *
	 * @return {@code true} if the value was actually set, {@code false} if it was already set
	 *
	 * @throws IllegalArgumentException if value is {@code this}
	 * @throws NullPointerException     if value is {@code null}
	 */
	public boolean trySet(T value) {
		checkValueToSet(value);
		return valueRef.compareAndSet(null, value);
	}

	/**
	 * Retrieve the instance's value, or fail if it has not been set.
	 *
	 * @return the value set
	 *
	 * @throws IllegalStateException if the value was not initialized
	 */
	public T get() {
		T value = valueRef.get();
		if (value == null) {
			throw new IllegalStateException(name + " has not been set");
		}
		return value;
	}

	/**
	 * Retrieve the instance's value, or return the provided default if unset.
	 *
	 * @param defaultValue The default value, {@code null} accepted
	 *
	 * @return the value set, or the default one if unset
	 */
	public T get(T defaultValue) {
		T value = valueRef.get();
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * Return a hash code of the value, if set; otherwise, return {@code 0}.
	 *
	 * @return the hash code of the instance's value if set, or {@code 0} if it is not set
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(valueRef.get());
	}

	/**
	 * Indicate whether the given object is equal to this instance.
	 *
	 * The two objects are equal if the following holds:
	 * <ul>
	 * <li>it is not {@code null},
	 * <li>it is a {@code StableField} instance,
	 * <li>its value is unset if and this instance's value is unset, or
	 * <li>its value is set to one that compare equal to the one set in this instance.
	 * </ul>
	 *
	 * @param o The object to compare
	 *
	 * @return {@code true} if the two objects compare equal
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof StableField && Objects.equals(get(null), ((StableField<?>) o).get(null));
	}

	/**
	 * Return a string representation of this object.
	 *
	 * @return a String representation of this object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append(' ');
		sb.append('"');
		sb.append(name);
		sb.append('"');
		sb.append(' ');
		sb.append('(');
		T value = valueRef.get();
		if (value == null) {
			sb.append("not set");
		} else {
			sb.append(value.toString());
		}
		sb.append(')');
		return sb.toString();
	}
}
