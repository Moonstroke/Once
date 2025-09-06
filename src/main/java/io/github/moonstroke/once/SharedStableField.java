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

	@Override
	protected T getValue() {
		return (T) VALUE.getVolatile(this);
	}

	@Override
	protected boolean isSet() {
		return (boolean) SET.getVolatile(this);
	}

	@Override
	protected void doSet(T value) {
		synchronized (lock) {
			if ((boolean) SET.getOpaque(this)) {
				throw new IllegalStateException(name + " is already set");
			}
			VALUE.setOpaque(this, value);
			SET.setOpaque(this, true);
		}
	}

	@Override
	protected boolean doTrySet(T value) {
		synchronized (lock) {
			if ((boolean) SET.getOpaque(this)) {
				return false;
			}
			VALUE.setOpaque(this, value);
			SET.setOpaque(this, true);
		}
		return true;
	}
}
