package io.github.moonstroke.once;

/**
 * A special container for a single value, allowing only a single initialization.
 */
public class OnceSettableField<T> {

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
	public OnceSettableField(String name, Requirement<T>... requirements) {
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}

	/**
	 * Initialize the instance's value, or fail if has already been set.
	 *
	 * @param value The value to set
	 *
	 * @throws IllegalStateException if the value has already been initialized
	 * @throws NullPointerException  if value is {@code null}
	 */
	public void set(T value) {
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}

	/**
	 * Retrieve the instance's value, or fail if it has not been set.
	 *
	 * @return the value set
	 *
	 * @throws IllegalStateException if the value was not initialized
	 */
	public T get() {
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}
}
