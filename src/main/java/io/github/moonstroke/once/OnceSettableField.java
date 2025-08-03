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
		this.value = value;
	}

	/**
	 * Retrieve the instance's value, or fail if it has not been set.
	 *
	 * @return the value set
	 *
	 * @throws IllegalStateException if the value was not initialized
	 */
	public T get() {
		return value;
	}
}
