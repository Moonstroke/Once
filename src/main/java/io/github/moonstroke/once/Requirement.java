package io.github.moonstroke.once;

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
}
