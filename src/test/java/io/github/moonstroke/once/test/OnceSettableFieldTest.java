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
	void testCallToTrySetNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.trySet(null));
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
}
