package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.OnceSettableField;

class OnceSettableFieldTest {

	@Test
	void testConstructorCallNullNameFails() {
		assertThrows(NullPointerException.class, () -> new OnceSettableField<>(null));
	}

	@Test
	void testConstructorCallEmptyNameFails() {
		assertThrows(IllegalArgumentException.class, () -> new OnceSettableField<>(""));
	}

	@Test
	void testFirstCallToSetDoesNotFail() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> once.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		once.set(value);
		assertThrows(IllegalStateException.class, () -> once.set(value));
	}

	@Test
	void testCallToSetNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.set(null));
	}

	@Test
	void testCallToSetSelfFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> once.set(once));
	}

	@Test
	void testCallToTrySetNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.trySet(null));
	}

	@Test
	void testCallToTrySetSelfFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> once.trySet(once));
	}

	@Test
	void testTrySetReturnsTrueWhenDidSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertTrue(once.trySet(new Object()));
	}

	@Test
	void testTrySetReturnsTrueWhenDidNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.trySet(new Object()));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(IllegalStateException.class, () -> once.get());
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		once.set(value);
		assertDoesNotThrow(() -> once.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		once.set(value);
		assertEquals(value, once.get());
	}

	@Test
	void testGetDefaultAcceptsNull() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertDoesNotThrow(() -> once.get(null));
	}

	@Test
	void testGetDefaultReturnsStoredValueIfSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object(), defaultValue = new Object();
		once.set(value);
		assertEquals(value, once.get(defaultValue));
	}

	@Test
	void testGetDefaultReturnsDefaultValueIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object defaultValue = new Object();
		assertEquals(defaultValue, once.get(defaultValue));
	}

	@Test
	void testEqualsNullReturnsFalseWhenUnset() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertFalse(once.equals(null));
	}

	@Test
	void testEqualsNullReturnsFalseWhenSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.equals(null));
	}

	@Test
	void testEqualsSelfReturnsTrue() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertTrue(once.equals(once));
	}

	@Test
	void testEqualsUnsetInstReturnsTrueWhenUnset() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertTrue(once.equals(new OnceSettableField<>("other")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.equals(new OnceSettableField<>("other")));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnset() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		OnceSettableField<Object> other = new OnceSettableField<>("other");
		other.set(new Object());
		assertFalse(once.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueReturnsTrue() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		once.set(value);
		OnceSettableField<Object> other = new OnceSettableField<>("other");
		other.set(value);
		assertTrue(once.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueReturnsFalse() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		OnceSettableField<Object> other = new OnceSettableField<>("other");
		other.set(new Object());
		assertFalse(once.equals(other));
	}

	@Test
	void testHashCodeReturnsZeroIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertEquals(0, once.hashCode());
	}

	@Test
	void testHashCodeReturnsValueHashCodeIfSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		once.set(value);
		assertEquals(value.hashCode(), once.hashCode());
	}

	@Test
	void testToStringSucceedsWhenUnset() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertDoesNotThrow(once::toString);
	}
}
