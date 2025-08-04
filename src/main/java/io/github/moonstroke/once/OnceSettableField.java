package io.github.moonstroke.once;

/**
 * A special container for a single value, allowing only a single initialization.
 */
public class OnceSettableField<T> {

	private final String name;
	private T value;


	/**
	 * Create a field of given name that can only be set once.
	 *
	 * @param name The name of the field
	 *
	 * @throws NullPointerException     if name is {@code null}
	 * @throws IllegalArgumentException if name is empty
	 */
	public OnceSettableField(String name) {
		if (name == null) {
			throw new NullPointerException("Cannot have a null name");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("Cannot have an empty name");
		}
		this.name = name;
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
		if (this.value != null) {
			throw new IllegalStateException(name + " is already set");
		}
		if (value == null) {
			throw new NullPointerException("Cannot set a null value");
		}
		this.value = value;
	}

	/**
	 * Initialize the instance's value and return whether the operation succeeded.
	 *
	 * @param value The value to set
	 *
	 * @return {@code true} if the value was actually set, {@code false} if it was already set
	 *
	 * @throws NullPointerException if value is {@code null}
	 */
	public boolean trySet(T value) {
		throw new UnsupportedOperationException("not implemented"); // TODO
	}

	/**
	 * Retrieve the instance's value, or fail if it has not been set.
	 *
	 * @return the value set
	 *
	 * @throws IllegalStateException if the value was not initialized
	 */
	public T get() {
		if (value == null) {
			throw new IllegalStateException(name + " has not been set");
		}
		return value;
	}
}
