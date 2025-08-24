/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A special container for a single value, allowing only a single initialization.
 */
public class StableField<T> {

	private final String name;
	private volatile boolean set;
	private volatile T value;
	private final Object lock = new Object();
	private final List<Requirement<? super T>> requirements;
	private final boolean allowNull;


	/**
	 * Create a field of given name that can only be set once.
	 *
	 * @param name The name of the field
	 * @param requirements The requirements that the value must meet before being set
	 *
	 * @throws NullPointerException     if any parameter is {@code null}
	 * @throws IllegalArgumentException if name is empty
	 */
	@SafeVarargs
	public StableField(String name, Requirement<? super T>... requirements) {
		if (name == null) {
			throw new NullPointerException("Cannot have a null name");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("Cannot have an empty name");
		}
		this.name = name;
		List<Requirement<? super T>> reqs = Arrays.asList(requirements);
		if (reqs.contains(null)) {
			throw new NullPointerException(name + "cannot have a null requirement");
		}
		allowNull = reqs.contains(Requirements.ALLOW_NULL);
		this.requirements = reqs;
	}

	private void checkValueToSet(T value) {
		if (value == null && !allowNull) {
			throw new NullPointerException("Cannot set a null value");
		}
		if (value == this) {
			throw new IllegalArgumentException("cannot set the value to itself");
		}
		for (Requirement<? super T> r : requirements) {
			r.check(value);
		}
	}

	/**
	 * Initialize the instance's value, or fail if has already been set.
	 *
	 * @param value The value to set
	 *
	 * @throws IllegalStateException if the value has already been initialized
	 * @throws IllegalArgumentException if value is {@code this}
	 * @throws NullPointerException     if value is {@code null} and this instance does not
	 *                                  {@linkplain Requirements#ALLOW_NULL allow nulls}
	 */
	public void set(T value) {
		checkValueToSet(value);
		if (set) {
			throw new IllegalStateException(name + " is already set");
		}
		synchronized (lock) {
			if (set) {
				throw new IllegalStateException(name + " is already set");
			}
			this.value = value;
			set = true;
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
	 * @throws NullPointerException     if value is {@code null} and this instance does not
	 *                                  {@linkplain Requirements#ALLOW_NULL allow nulls}
	 */
	public boolean trySet(T value) {
		checkValueToSet(value);
		if (set) {
			return false;
		}
		synchronized (lock) {
			if (set) {
				return false;
			}
			this.value = value;
			set = true;
		}
		return true;
	}

	/**
	 * Retrieve the instance's value, or fail if it has not been set.
	 *
	 * @return the value set
	 *
	 * @throws IllegalStateException if the value was not initialized
	 */
	public T get() {
		if (!set) {
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
		return set ? value : defaultValue;
	}

	/**
	 * Return a hash code of the value, if set; otherwise, return {@code 0}.
	 *
	 * @return the hash code of the instance's value if set, or {@code 0} if it is not set
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
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
		return o instanceof StableField && Objects.equals(value, ((StableField<?>) o).value);
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
		sb.append('(');
		if (!set) {
			sb.append("not set");
		} else {
			sb.append(value.toString());
		}
		sb.append(')');
		return sb.toString();
	}
}
