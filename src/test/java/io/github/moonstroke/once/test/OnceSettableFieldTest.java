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
	void testCallToSetFromNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.setFrom(null));
	}

	@Test
	void testCallToSetFromNullSuppliedFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.setFrom(() -> null));
	}

	@Test
	void testCallToSetFromNonNullSucceeds() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertDoesNotThrow(() -> once.setFrom(Object::new));
	}

	@Test
	void testCallToSetFromCallsSupplier() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		boolean[] called = new boolean[1];
		assertDoesNotThrow(() -> once.setFrom(() -> {
			called[0] = true;
			return new Object();
		}));
		assertTrue(called[0]);
	}

	@Test
	void testCallToSetFromSetsSuppliedValue() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> once.setFrom(() -> value));
		assertEquals(value, once.get());
	}

	@Test
	void testCallToSetFromFailsIfAlreadySet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertThrows(IllegalStateException.class, () -> once.setFrom(Object::new));
	}

	@Test
	void testCallToSetFromDoesNotCallSupplierIfAlreadySet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertThrows(IllegalStateException.class, () -> once.setFrom(() -> fail("supplier should not be called")));
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
	void testCallToTrySetFromNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.trySetFrom(null));
	}

	@Test
	void testCallToTrySetFromNullSuppliedFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertThrows(NullPointerException.class, () -> once.trySetFrom(() -> null));
	}

	@Test
	void testCallToTrySetFromNonNullSucceeds() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertTrue(once.trySetFrom(Object::new));
	}

	@Test
	void testCallToTrySetFromCallsSupplier() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		boolean[] called = new boolean[1];
		assertTrue(once.trySetFrom(() -> {
			called[0] = true;
			return new Object();
		}));
		assertTrue(called[0]);
	}

	@Test
	void testCallToTrySetFromSetsSuppliedValue() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		assertTrue(once.trySetFrom(() -> value));
		assertEquals(value, once.get());
	}

	@Test
	void testCallToTrySetFromReturnsFalseIfAlreadySet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.trySetFrom(Object::new));
	}

	@Test
	void testCallToTrySetFromDoesNotCallSupplierIfAlreadySet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.trySetFrom(() -> fail("supplier should not be called")));
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
	void testGetOptReturnsEmptyOptionalWhenNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		assertTrue(once.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsNotEmptyOptionalWhenSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsValueInOptionalWhenSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		Object value = new Object();
		once.set(value);
		assertEquals(value, once.getOpt().get());
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
	void testMapNullParamFails() {
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
	void testMapParamNotCalledIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		boolean[] called = new boolean[1];
		assertThrows(IllegalStateException.class, () -> once.map(object -> {
			called[0] = true;
			return String.valueOf(object);
		}));
		assertFalse(called[0]);
	}

	@Test
	void testMapParamCalledIfSet() {
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
	void testMapParamReturnsNullDoesNotFail() {
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
	void testMapParamReturnsNotNullReturnsNotEmptyOptional() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertFalse(once.map(String::valueOf).isEmpty());
	}

	@Test
	void testIfSetNullParamFails() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		assertThrows(NullPointerException.class, () -> once.ifSet(null));
	}

	@Test
	void testIfSetParamNotCalledIfNotSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		boolean[] called = new boolean[1];
		once.ifSet(object -> {
			called[0] = true;
		});
		assertFalse(called[0]);
	}

	@Test
	void testIfSetParamCalledIfSet() {
		OnceSettableField<Object> once = new OnceSettableField<>("field");
		once.set(new Object());
		boolean[] called = new boolean[1];
		once.ifSet(object -> {
			called[0] = true;
		});
		assertTrue(called[0]);
	}
}
