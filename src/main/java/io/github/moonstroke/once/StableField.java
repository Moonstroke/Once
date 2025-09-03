/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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
	 * @param name         The name of the field
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

	/**
	 * Initialize the instance's value, or fail if has already been set.
	 *
	 * @param value The value to set
	 *
	 * @throws IllegalStateException    if the value has already been initialized
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
	 * @throws NoSuchElementException if the value was not initialized
	 */
	public T get() {
		if (!set) {
			throw new NoSuchElementException(name + " has not been set");
		}
		return value;
	}

	/**
	 * Retrieve an optional instance wrapping the object's value if present, or an empty optional otherwise.
	 *
	 * @return an {@link Optional} wrapping the value, or an empty one; never {@code null}
	 */
	public Optional<T> getOpt() {
		return Optional.ofNullable(value);
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
	public <R> Optional<R> map(Function<T, R> mapFunction) {
		if (mapFunction == null) {
			throw new NullPointerException("Cannot invoke a null function");
		}
		if (!set) {
			throw new NoSuchElementException(name + " has not been set");
		}
		return Optional.ofNullable(mapFunction.apply(value));
	}

	/**
	 * Call the provided function with the value if it is set.
	 *
	 * @param consumer The function to call if the value is set
	 *
	 * @throws NullPointerException if consumer is {@code null}
	 */
	public void ifSet(Consumer<T> consumer) {
		if (consumer == null) {
			throw new NullPointerException("Cannot invoke a null consumer");
		}
		if (set) {
			consumer.accept(value);
		}
	}

	/**
	 * Return a numeric sum of the object's state, comprising its name and value.
	 *
	 * @return a hash of the instance's name and value members
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, value);
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
		return name.equals(other.name) && Objects.equals(value, ((StableField<?>) o).value);
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
		if (!set) {
			sb.append("not set");
		} else {
			sb.append(value.toString());
		}
		sb.append(')');
		return sb.toString();
	}
}
