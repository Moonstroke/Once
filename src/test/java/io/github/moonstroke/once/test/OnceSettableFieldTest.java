package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.OnceSettableField;

class OnceSettableFieldTest {

	@Test
	void testConstructorNullNameFails() {
		assertThrows(NullPointerException.class, () -> new OnceSettableField<>(null));
	}

	@Test
	void testConstructorEmptyNameFails() {
		assertThrows(IllegalArgumentException.class, () -> new OnceSettableField<>(""));
	}

	@Test
	void testSetFirstCallSucceeds() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> once.set(value));
	}

	@Test
	void testSetSecondCallFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		once.set(value);
		assertThrows(IllegalStateException.class, () -> once.set(value));
	}

	@Test
	void testSetNullValueFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.set(null));
	}

	@Test
	void testSetFromNullSupplierFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.setFrom(null));
	}

	@Test
	void testSetFromSupplierReturnsNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.setFrom(() -> null));
	}

	@Test
	void testSetFromNonNullSupplierSucceeds() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertDoesNotThrow(() -> once.setFrom(Object::new));
	}

	@Test
	void testSetFromInvokesSupplier() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		boolean[] called = new boolean[1];
		assertDoesNotThrow(() -> once.setFrom(() -> {
			called[0] = true;
			return new Object();
		}));
		assertTrue(called[0]);
	}

	@Test
	void testSetFromSetsSuppliedValue() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> once.setFrom(() -> value));
		assertEquals(value, once.get());
	}

	@Test
	void testSetFromFailsIfAlreadySet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertThrows(IllegalStateException.class, () -> once.setFrom(Object::new));
	}

	@Test
	void testSetFromDoesNotInvokeSupplierIfAlreadySet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertThrows(IllegalStateException.class, () -> once.setFrom(() -> fail("supplier should not have been called")));
	}

	@Test
	void testTrySetNullValueFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.trySet(null));
	}

	@Test
	void testTrySetReturnsTrueWhenDidSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertTrue(once.trySet(new Object()));
	}

	@Test
	void testTrySetReturnsFalseWhenDidNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.trySet(new Object()));
	}

	@Test
	void testTrySetFromNullSupplierFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.trySetFrom(null));
	}

	@Test
	void testTrySetFromSupplierReturnsNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.trySetFrom(() -> null));
	}

	@Test
	void testTrySetFromNonNullSupplierSucceeds() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertTrue(once.trySetFrom(Object::new));
	}

	@Test
	void testTrySetFromInvokesSupplier() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		boolean[] called = new boolean[1];
		assertTrue(once.trySetFrom(() -> {
			called[0] = true;
			return new Object();
		}));
		assertTrue(called[0]);
	}

	@Test
	void testTrySetFromSetsSuppliedValue() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		assertTrue(once.trySetFrom(() -> value));
		assertEquals(value, once.get());
	}

	@Test
	void testTrySetFromReturnsFalseIfAlreadySet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.trySetFrom(Object::new));
	}

	@Test
	void testTrySetFromDoesNotInvokeSupplierIfAlreadySet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.trySetFrom(() -> fail("supplier should not have been called")));
	}

	@Test
	void testGetFailsIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(IllegalStateException.class, () -> once.get());
	}

	@Test
	void testGetSucceedsIfSet() {
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
	void testGetOptReturnsEmptyOptionalIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertTrue(once.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsNotEmptyOptionalIfSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsWrappedValuePassedToSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		once.set(value);
		assertEquals(value, once.getOpt().get());
	}

	@Test
	void testGetDefaultNullDefautlValueSucceeds() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertDoesNotThrow(() -> once.get(null));
	}

	@Test
	void testGetDefaultReturnsValuePassedToSetIfSet() {
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
	void testMapNullFunctionFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertThrows(NullPointerException.class, () -> once.map(null));
	}

	@Test
	void testMapFailsIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(IllegalStateException.class, () -> once.map(String::valueOf));
	}

	@Test
	void testMapFunctionNotInvokedIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(IllegalStateException.class, () -> once.map(object -> fail("function should not have been called")));
	}

	@Test
	void testMapFunctionInvokedIfSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		boolean[] called = new boolean[1];
		assertDoesNotThrow(() -> once.map(object -> {
			called[0] = true;
			return String.valueOf(object);
		}));
		assertTrue(called[0]);
	}

	@Test
	void testMapFunctionReturnsNullSucceeds() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertDoesNotThrow(() -> once.map(object -> null));
	}

	@Test
	void testMapParamReturnsNullReturnsEmptyOptional() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertTrue(once.map(object -> null).isEmpty());
	}

	@Test
	void testMapFunctionReturnsNotNullReturnsNotEmptyOptional() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.map(String::valueOf).isEmpty());
	}

	@Test
	void testIfSetNullConsumerFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertThrows(NullPointerException.class, () -> once.ifSet(null));
	}

	@Test
	void testIfSetConsumerNotInvokedIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.ifSet(object -> fail("consumer should not have been called"));
	}

	@Test
	void testIfSetConsumerInvokedIfSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		boolean[] called = new boolean[1];
		once.ifSet(object -> {
			called[0] = true;
		});
		assertTrue(called[0]);
	}
}
