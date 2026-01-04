/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.IntStableField;
import io.github.moonstroke.once.Requirement;
import io.github.moonstroke.once.Requirements;

class RequirementsIntTest {

	protected IntStableField getTestInstance(String name) {
		return new IntStableField(name);
	}

	protected IntStableField getTestInstance(String name, Requirement<? super Integer> requirement) {
		return new IntStableField(name, requirement);
	}

	private IntStableField getTestInstance(Requirement<? super Integer> requirement) {
		return getTestInstance("field", requirement);
	}


	@Test
	void testRequirementsNotZeroRejectsZero() {
		var sf = getTestInstance(Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> sf.set(0));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZero() {
		var sf = getTestInstance(Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> sf.set(42));
	}

	@Test
	void testRequirementsNotNegativeRejectsNegative() {
		var sf = getTestInstance(Requirements.NOT_NEGATIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(-132));
	}

	@Test
	void testRequirementsNotNegativeAcceptsPositive() {
		var sf = getTestInstance(Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> sf.set(42));
	}

	@Test
	void testRequirementsNotNegativeAcceptsZero() {
		var sf = getTestInstance(Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> sf.set(0));
	}

	@Test
	void testRequirementsPositiveRejectsNegative() {
		var sf = getTestInstance(Requirements.POSITIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(-1));
	}

	@Test
	void testRequirementsPositiveRejectsZero() {
		var sf = getTestInstance(Requirements.POSITIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(0));
	}

	@Test
	void testRequirementsPositiveAcceptsPositive() {
		var sf = getTestInstance(Requirements.POSITIVE);
		assertDoesNotThrow(() -> sf.set(42));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntBelowRange() {
		var sf = getTestInstance(Requirements.inRange(1, 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set(0));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMin() {
		var sf = getTestInstance(Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(1));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntInRange() {
		var sf = getTestInstance(Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(3));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMax() {
		var sf = getTestInstance(Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(5));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntAboveRange() {
		var sf = getTestInstance(Requirements.inRange(1, 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set(7));
	}
}
