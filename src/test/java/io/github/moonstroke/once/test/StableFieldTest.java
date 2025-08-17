package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
	void testFirstCallToSetDoesNotFail() {
		StableField<Object> once = new StableField<>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> once.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		StableField<Object> once = new StableField<>("field");
		Object value = new Object();
		once.set(value);
		assertThrows(IllegalStateException.class, () -> once.set(value));
	}

	@Test
	void testCallToSetNullFails() {
		StableField<Object> once = new StableField<>("field");
		assertThrows(NullPointerException.class, () -> once.set(null));
	}

	@Test
	void testCallToSetSelfFails() {
		StableField<Object> once = new StableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> once.set(once));
	}

	@Test
	void testCallToTrySetNullFails() {
		StableField<Object> once = new StableField<>("field");
		assertThrows(NullPointerException.class, () -> once.trySet(null));
	}

	@Test
	void testCallToTrySetSelfFails() {
		StableField<Object> once = new StableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> once.trySet(once));
	}

	@Test
	void testTrySetReturnsTrueWhenDidSet() {
		StableField<Object> once = new StableField<>("field");
		assertTrue(once.trySet(new Object()));
	}

	@Test
	void testTrySetReturnsTrueWhenDidNotSet() {
		StableField<Object> once = new StableField<>("field");
		once.set(new Object());
		assertFalse(once.trySet(new Object()));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		StableField<Object> once = new StableField<>("field");
		assertThrows(IllegalStateException.class, () -> once.get());
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		StableField<Object> once = new StableField<>("field");
		Object value = new Object();
		once.set(value);
		assertDoesNotThrow(() -> once.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		StableField<Object> once = new StableField<>("field");
		Object value = new Object();
		once.set(value);
		assertEquals(value, once.get());
	}

	@Test
	void testGetDefaultAcceptsNull() {
		StableField<Object> once = new StableField<>("field");
		assertDoesNotThrow(() -> once.get(null));
	}

	@Test
	void testGetDefaultReturnsStoredValueIfSet() {
		StableField<Object> once = new StableField<>("field");
		Object value = new Object(), defaultValue = new Object();
		once.set(value);
		assertEquals(value, once.get(defaultValue));
	}

	@Test
	void testGetDefaultReturnsDefaultValueIfNotSet() {
		StableField<Object> once = new StableField<>("field");
		Object defaultValue = new Object();
		assertEquals(defaultValue, once.get(defaultValue));
	}

	@Test
	void testEqualsNullReturnsFalseWhenUnset() {
		StableField<Object> once = new StableField<>("field");
		assertFalse(once.equals(null));
	}

	@Test
	void testEqualsNullReturnsFalseWhenSet() {
		StableField<Object> once = new StableField<>("field");
		once.set(new Object());
		assertFalse(once.equals(null));
	}

	@Test
	void testEqualsSelfReturnsTrue() {
		StableField<Object> once = new StableField<>("field");
		assertTrue(once.equals(once));
	}

	@Test
	void testEqualsUnsetInstReturnsTrueWhenUnset() {
		StableField<Object> once = new StableField<>("field");
		assertTrue(once.equals(new StableField<>("other")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSet() {
		StableField<Object> once = new StableField<>("field");
		once.set(new Object());
		assertFalse(once.equals(new StableField<>("other")));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnset() {
		StableField<Object> once = new StableField<>("field");
		StableField<Object> other = new StableField<>("other");
		other.set(new Object());
		assertFalse(once.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueReturnsTrue() {
		StableField<Object> once = new StableField<>("field");
		Object value = new Object();
		once.set(value);
		StableField<Object> other = new StableField<>("other");
		other.set(value);
		assertTrue(once.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueReturnsFalse() {
		StableField<Object> once = new StableField<>("field");
		once.set(new Object());
		StableField<Object> other = new StableField<>("other");
		other.set(new Object());
		assertFalse(once.equals(other));
	}

	@Test
	void testHashCodeReturnsZeroIfNotSet() {
		StableField<Object> once = new StableField<>("field");
		assertEquals(0, once.hashCode());
	}

	@Test
	void testHashCodeReturnsValueHashCodeIfSet() {
		StableField<Object> once = new StableField<>("field");
		Object value = new Object();
		once.set(value);
		assertEquals(value.hashCode(), once.hashCode());
	}

	@Test
	void testToStringSucceedsWhenUnset() {
		StableField<Object> once = new StableField<>("field");
		assertDoesNotThrow(once::toString);
	}
}
