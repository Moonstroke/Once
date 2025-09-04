/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A special container for a single value, allowing only a single initialization.
 */
public class StableField<T> {

	/* The name of the field. Used in error messages and toString representation */
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
		this.requirements = new ArrayList<>(reqs);
		if (allowNull) {
			/* We don't need to store this requirement */
			this.requirements.remove(Requirements.ALLOW_NULL);
		}
	}

	/* Ensure that the given value is eligible for being contained by this instance. Does not check whether this
	 * instance already contains a value. */
	private void checkValueToSet(T value) {
		if (value == null) {
			if (!allowNull) {
				throw new NullPointerException(name + "cannot bet set to null");
			}
			/* Do not iterate over requirements: they do not apply to a null value */
		} else if (value == this) {
			throw new IllegalArgumentException(name + "cannot be set to itself");
		} else {
			for (Requirement<? super T> r : requirements) {
				r.check(value);
			}
		}
	}

	protected void setValue(T value) {
		this.value = value;
		this.set = true;
	}

	protected T getValue() {
		return value;
	}

	protected boolean isSet() {
		return set;
	}

	protected void checkSet() {
		if (isSet()) {
			throw new IllegalStateException(name + " is already set");
		}
	}

	/**
	 * Initialize the instance's value, or fail if has already been set.
	 *
	 * @param value The value to set
	 *
	 * @throws IllegalStateException    if the value has already been initialized
	 * @throws IllegalArgumentException if value is {@code this}
=	 * @throws NullPointerException     if value is {@code null} and this instance does not
	 *                                  {@linkplain Requirements#ALLOW_NULL allow nulls}
=	 */
	public void set(T value) {
		checkValueToSet(value);
		checkSet();
		doSetValue(value);
	}

	protected void doSetValue(T value) {
		synchronized (lock) {
			checkSet();
			setValue(value);
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
=	 * @throws NullPointerException     if value is {@code null} and this instance does not
	 *                                  {@linkplain Requirements#ALLOW_NULL allow nulls}
=	 */
	public boolean trySet(T value) {
		checkValueToSet(value);
		if (isSet()) {
			return false;
		}
		return doTrySetValue(value);
	}

	protected boolean doTrySetValue(T value) {
		synchronized (lock) {
			if (isSet()) {
				return false;
			}
			setValue(value);
		}
		return true;
	}

	/**
	 * Retrieve the instance's value, or fail if it has not been set.
	 *
	 * @return the value set
	 *
	 * @throws NoSuchElementException if the value was not initialized
	 */
	public T get() {
		if (!isSet()) {
			throw new NoSuchElementException(name + " has not been set");
		}
		return getValue();
	}

	/**
	 * Retrieve the instance's value, or return the provided default if unset.
	 *
	 * @param defaultValue The default value, {@code null} accepted
	 *
	 * @return the value set, or the default one if unset
	 */
	public T get(T defaultValue) {
		return isSet() ? getValue() : defaultValue;
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
		if (!(o instanceof StableField)) {
			return false;
		}
		StableField<?> other = (StableField<?>) o;
		return name.equals(other.name) && Objects.equals(getValue(), ((StableField<?>) o).getValue());
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
		if (!isSet()) {
			sb.append("not set");
		} else {
			sb.append(getValue().toString());
		}
		sb.append(')');
		return sb.toString();
	}
}
