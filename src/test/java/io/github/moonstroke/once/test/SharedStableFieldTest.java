/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once.test;

import io.github.moonstroke.once.StableField;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.Requirement;
import io.github.moonstroke.once.Requirements;
import io.github.moonstroke.once.SharedStableField;

class SharedStableFieldTest extends StableFieldTest {

	@Override
	protected <T> StableField<T> getTestInstance(String name) {
		return new SharedStableField<>(name);
	}


	@Test
	@Override
	void testConstructorNullNameFails() {
		assertThrows(NullPointerException.class, () -> new SharedStableField<>(null));
	}

	@Test
	@Override
	void testConstructorEmptyNameFails() {
		assertThrows(IllegalArgumentException.class, () -> new SharedStableField<>(""));
	}

	@Test
	@Override
	void testConstructorNullRequirementsFails() {
		assertThrows(NullPointerException.class, () -> new SharedStableField<>("field", (Requirement<Object>[]) null));
	}

	@Test
	@Override
	void testConstructorNullRequirementAloneFails() {
		assertThrows(NullPointerException.class, () -> new SharedStableField<>("field", (Requirement<Object>) null));
	}

	@Test
	@Override
	void testConstructorNullRequirementAmongOthersFails() {
		assertThrows(NullPointerException.class,
		             () -> new SharedStableField<>("field", Requirements.POSITIVE, null, Requirements.ALLOW_NULL));
	}
}
