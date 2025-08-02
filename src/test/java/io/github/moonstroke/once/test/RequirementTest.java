package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.Requirement;

class RequirementTest {

	@Test
	void testRequirementFromPredicateRejectsNullPredicate() {
		assertThrows(NullPointerException.class, () -> Requirement.fromPredicate(null));
	}

	@Test
	void testRequirementFromPredicateMsgRejectsNullPredicate() {
		assertThrows(NullPointerException.class, () -> Requirement.fromPredicate(null, "error message"));
	}

	@Test
	void testRequirementFromPredicateMsgRejectsNullMessage() {
		assertThrows(NullPointerException.class, () -> Requirement.fromPredicate(o -> true, null));
	}

	@Test
	void testRequirementFromPredicateMsgRejectsEmptyMessage() {
		assertThrows(IllegalArgumentException.class, () -> Requirement.fromPredicate(o -> true, ""));
	}
}
