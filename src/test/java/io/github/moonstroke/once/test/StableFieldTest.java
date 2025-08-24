/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.Requirement;
import io.github.moonstroke.once.Requirements;
import io.github.moonstroke.once.StableField;

class StableFieldTest {

	@Test
	void testConstructorCallNullNameFails() {
		assertThrows(NullPointerException.class, () -> new StableField<>(null));
	}

	@Test
	void testConstructorCallEmptyNameFails() {
		assertThrows(IllegalArgumentException.class, () -> new StableField<>(""));
	}

	@Test
	void testConstructorCallNullRequirementsFails() {
		assertThrows(NullPointerException.class, () -> new StableField<>("field", (Requirement<Object>[]) null));
	}

	@Test
	void testConstructorCallNullRequirementAloneFails() {
		assertThrows(NullPointerException.class, () -> new StableField<>("field", (Requirement<Object>) null));
	}

	@Test
	void testConstructorCallNullRequirementAmongOthersFails() {
		assertThrows(NullPointerException.class,
		             () -> new StableField<>("field", Requirements.POSITIVE, null, Requirements.ALLOW_NULL));
	}

	@Test
	void testFirstCallToSetDoesNotFail() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> sf.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertThrows(IllegalStateException.class, () -> sf.set(value));
	}

	@Test
	void testCallToSetNullFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(NullPointerException.class, () -> sf.set(null));
	}

	@Test
	void testCallToSetSelfFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> sf.set(sf));
	}

	@Test
	void testCallToTrySetNullFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(NullPointerException.class, () -> sf.trySet(null));
	}

	@Test
	void testCallToTrySetSelfFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> sf.trySet(sf));
	}

	@Test
	void testTrySetReturnsTrueWhenDidSet() {
		StableField<Object> sf = new StableField<>("field");
		assertTrue(sf.trySet(new Object()));
	}

	@Test
	void testTrySetReturnsTrueWhenDidNotSet() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.trySet(new Object()));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(IllegalStateException.class, () -> sf.get());
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertDoesNotThrow(() -> sf.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertEquals(value, sf.get());
	}

	@Test
	void testGetDefaultAcceptsNull() {
		StableField<Object> sf = new StableField<>("field");
		assertDoesNotThrow(() -> sf.get(null));
	}

	@Test
	void testGetDefaultReturnsStoredValueIfSet() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object(), defaultValue = new Object();
		sf.set(value);
		assertEquals(value, sf.get(defaultValue));
	}

	@Test
	void testGetDefaultReturnsDefaultValueIfNotSet() {
		StableField<Object> sf = new StableField<>("field");
		Object defaultValue = new Object();
		assertEquals(defaultValue, sf.get(defaultValue));
	}

	@Test
	void testEqualsNullReturnsFalseWhenUnset() {
		StableField<Object> sf = new StableField<>("field");
		assertFalse(sf.equals(null));
	}

	@Test
	void testEqualsNullReturnsFalseWhenSet() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.equals(null));
	}

	@Test
	void testEqualsSelfReturnsTrue() {
		StableField<Object> sf = new StableField<>("field");
		assertTrue(sf.equals(sf));
	}

	@Test
	void testEqualsUnsetInstReturnsTrueWhenUnset() {
		StableField<Object> sf = new StableField<>("field");
		assertTrue(sf.equals(new StableField<>("other")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSet() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.equals(new StableField<>("other")));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnset() {
		StableField<Object> sf = new StableField<>("field");
		StableField<Object> other = new StableField<>("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueReturnsTrue() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		StableField<Object> other = new StableField<>("other");
		other.set(value);
		assertTrue(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueReturnsFalse() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		StableField<Object> other = new StableField<>("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testHashCodeReturnsZeroIfNotSet() {
		StableField<Object> sf = new StableField<>("field");
		assertEquals(0, sf.hashCode());
	}

	@Test
	void testHashCodeReturnsValueHashCodeIfSet() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertEquals(value.hashCode(), sf.hashCode());
	}

	@Test
	void testToStringSucceedsWhenUnset() {
		StableField<Object> sf = new StableField<>("field");
		assertDoesNotThrow(sf::toString);
	}
}
