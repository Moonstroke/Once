package io.github.moonstroke.once;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
	 * Initialize the instance's value using the specified supplier function.
	 *
	 * The supplier function will only be invoked if the value is not set yet, so this method can be convenient for
	 * cases where the computation of the value is expensive, as it can avoid the cost if not necessary.
	 *
	 * @param supplier The function providing the value to set
	 *
	 * @throws IllegalStateException if the value has already been initialized
	 * @throws NullPointerException  if supplier is, or returns, {@code null}
	 */
	public void setFrom(Supplier<T> supplier) {
		if (this.value != null) {
			throw new IllegalStateException(name + " is already set");
		}
		if (supplier == null) {
			throw new NullPointerException("Cannot invoke a null supplier");
		}
		T value = supplier.get();
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
		if (this.value != null) {
			return false;
		}
		if (value == null) {
			throw new NullPointerException("Cannot set a null value");
		}
		this.value = value;
		return true;
	}

	/**
	 * Initialize the instance's value using the specified supplier function and return whether the operation succeeded.
	 *
	 * The supplier function will only be invoked if the value is not set yet, so this method can be convenient for
	 * cases where the computation of the value is expensive, as it can avoid the cost if not necessary.
	 *
	 * @param supplier The function providing the value to set
	 *
	 * @return {@code true} if the value was actually set, {@code false} if it was already set
	 *
	 * @throws NullPointerException if supplier is, or returns, {@code null}
	 */
	public boolean trySetFrom(Supplier<T> supplier) {
		if (this.value != null) {
			return false;
		}
		if (supplier == null) {
			throw new NullPointerException("Cannot invoke a null supplier");
		}
		T value = supplier.get();
		if (value == null) {
			throw new NullPointerException("Cannot set a null value");
		}
		this.value = value;
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
		if (value == null) {
			throw new IllegalStateException(name + " has not been set");
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
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * Retrieve the value and transform it using to the given function.
	 *
	 * @param <R>         The output type of the transformation function
	 * @param mapFunction The transformation function
	 *
	 * @return an {@link Optional} containing the transformed value, or an empty one if the function returned {@code null}
	 *
	 * @throws IllegalStateException if the value was not initialized
	 * @throws NullPointerException if mapFunction is {@code null}
	 */
	public <R> Optional<R> map(Function<T, R> mapFunction) {
		throw new UnsupportedOperationException("not implemented"); // TODO
	}

	/**
	 * Call the provided function with the value if it is set.
	 *
	 * @param consumer The function to call if the value is set
	 *
	 * @throws NullPointerException if consumer is {@code null}
	 */
	public void ifSet(Consumer<T> consumer) {
		throw new UnsupportedOperationException("not implemented"); // TODO
	}
}
