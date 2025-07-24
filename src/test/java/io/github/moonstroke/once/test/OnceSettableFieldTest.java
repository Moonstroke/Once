package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.OnceSettableField;

class OnceSettableFieldTest {

	@Test
	void testFirstCallToSetDoesNotFail() {
		OnceSettableField<Object> once = new OnceSettableField<Object>();
		Object value = new Object();
		assertDoesNotThrow(() -> once.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		OnceSettableField<Object> once = new OnceSettableField<Object>();
		Object value = new Object();
		once.set(value);
		assertThrows(IllegalStateException.class, () -> once.set(value));
	}

	@Test
	void testCallToSetNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<Object>();
		assertThrows(NullPointerException.class, () -> once.set(null));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		OnceSettableField<Object> once = new OnceSettableField<Object>();
		assertThrows(IllegalStateException.class, () -> once.get());
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		OnceSettableField<Object> once = new OnceSettableField<Object>();
		Object value = new Object();
		once.set(value);
		assertDoesNotThrow(() -> once.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		OnceSettableField<Object> once = new OnceSettableField<Object>();
		Object value = new Object();
		once.set(value);
		assertEquals(value, once.get());
	}
}
