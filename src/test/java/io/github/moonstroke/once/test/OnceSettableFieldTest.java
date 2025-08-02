package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
