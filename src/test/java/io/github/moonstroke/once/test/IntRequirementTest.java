/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.IntRequirement;

class IntRequirementTest {

	@Test
	void testRequirementFromPredicateRejectsNullPredicate() {
		assertThrows(NullPointerException.class, () -> IntRequirement.fromPredicate(null));
	}

	@Test
	void testRequirementFromPredicateMsgRejectsNullPredicate() {
		assertThrows(NullPointerException.class, () -> IntRequirement.fromPredicate(null, "error message"));
	}

	@Test
	void testRequirementFromPredicateMsgRejectsNullMessage() {
		assertThrows(NullPointerException.class, () -> IntRequirement.fromPredicate(o -> true, null));
	}

	@Test
	void testRequirementFromPredicateMsgRejectsEmptyMessage() {
		assertThrows(IllegalArgumentException.class, () -> IntRequirement.fromPredicate(o -> true, ""));
	}
}
