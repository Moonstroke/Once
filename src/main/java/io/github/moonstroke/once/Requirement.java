package io.github.moonstroke.once;

import java.util.function.Predicate;

/**
 * An additional requirement that the field value must meet when it is being set.
 *
 * @param <T> The type of the value under scrutiny
 */
@FunctionalInterface
public interface Requirement<T> {

	/**
	 * Perform the check that this requirement represents.
	 *
	 * @param value The value to check
	 *
	 * @throws IllegalArgumentException if the value does not meet the requirement
	 */
	void check(T value) throws IllegalArgumentException;


	/**
	 * Wrap a boolean predicate into a requirement instance.
	 *
	 * @param <U>       The type of the value checked
	 * @param predicate The boolean predicate to wrap
	 *
	 * @return A requirement instance performing the check represented by the given predicate
	 *
	 * @throws NullPointerException if predicate is {@code null}
	 */
	public static <U> Requirement<U> fromPredicate(Predicate<U> predicate) {
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}

	/**
	 * Wrap a boolean predicate into a requirement instance, specifying a custom error message.
	 *
	 * @param <U>       The type of the value checked
	 * @param predicate The boolean predicate to wrap
	 * @param message   The error message to throw if the requirement is not met
	 *
	 * @return A requirement instance performing the check represented by the given predicate
	 *
	 * @throws NullPointerException     if predicate or message is {@code null}
	 * @throws IllegalArgumentException if message is empty
	 */
	public static <U> Requirement<U> fromPredicate(Predicate<U> predicate, String message) {
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}
}
