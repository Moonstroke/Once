/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once;

/**
 * A special container for a single value allowing only a single initialization, safe for concurrent manipulation by
 * multiple execution threads.
 */
public class SharedStableField<T> extends StableField<T> {

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

	protected void doSetValue(T value) {
		synchronized (lock) {
			checkSet();
			setValue(value);
		}
	}

	protected boolean doTrySetValue(T value) {
		synchronized (lock) {
			if (isSet()) {
				return false;
			}
			setValue(value);
		}
		return true;
	}
}
