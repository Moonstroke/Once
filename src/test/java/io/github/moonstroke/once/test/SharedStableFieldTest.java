package io.github.moonstroke.once.test;

import io.github.moonstroke.once.StableField;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.Requirement;
import io.github.moonstroke.once.Requirements;
import io.github.moonstroke.once.SharedStableField;

class SharedStableFieldTest extends StableFieldTest {

	protected <T> StableField<T> getTestInstance(String name) {
		return new SharedStableField<>(name);
	}


	@Test
	void testConstructorCallNullNameFails() {
		assertThrows(NullPointerException.class, () -> new SharedStableField<>(null));
	}

	@Test
	void testConstructorCallEmptyNameFails() {
		assertThrows(IllegalArgumentException.class, () -> new SharedStableField<>(""));
	}

	@Test
	void testConstructorCallNullRequirementsFails() {
		assertThrows(NullPointerException.class, () -> new SharedStableField<>("field", (Requirement<Object>[]) null));
	}

	@Test
	void testConstructorCallNullRequirementAloneFails() {
		assertThrows(NullPointerException.class, () -> new SharedStableField<>("field", (Requirement<Object>) null));
	}

	@Test
	void testConstructorCallNullRequirementAmongOthersFails() {
		assertThrows(NullPointerException.class,
		             () -> new SharedStableField<>("field", Requirements.POSITIVE, null, Requirements.ALLOW_NULL));
	}
}
