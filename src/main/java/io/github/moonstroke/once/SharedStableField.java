/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * A special container for a single value allowing only a single initialization, safe for concurrent manipulation by
 * multiple execution threads.
 */
public class SharedStableField<T> extends StableField<T> {

	private static final VarHandle SET;
	private static final VarHandle VALUE;

	static {
		try {
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			SET = lookup.findVarHandle(StableField.class, "set", boolean.class);
			VALUE = lookup.findVarHandle(StableField.class, "value", Object.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
	}


	private final Object lock = new Object();


	/**
	 * Create a field of given name that can only be set once, fit for concurrent access.
	 * 
	 * {@inheritDoc}
	 */
	@SafeVarargs
	public SharedStableField(String name, Requirement<? super T>... requirements) {
		super(name, requirements);
	}

	protected T getValue() {
		return (T) VALUE.getVolatile(this);
	}

	protected boolean isSet() {
		return (boolean) SET.getVolatile(this);
	}

	protected void doSet(T value) {
		synchronized (lock) {
			checkSet();
			VALUE.setVolatile(this, value);
			SET.setVolatile(this, true);
		}
	}

	protected boolean doTrySet(T value) {
		synchronized (lock) {
			if (isSet()) {
				return false;
			}
			VALUE.setVolatile(this, value);
			SET.setVolatile(this, true);
		}
		return true;
	}
}
