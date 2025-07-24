package io.github.moonstroke.once;

/**
 * A special container for a single value, allowing only a single initialization.
 */
public class OnceSettableField<T> {

	public void set(T value) {
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}

	public T get() {
		throw new UnsupportedOperationException("Not implemented"); // TODO
	}
}
