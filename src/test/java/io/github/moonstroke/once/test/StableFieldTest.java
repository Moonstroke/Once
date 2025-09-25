/* SPDX-FileCopyrightText: 2025 (c) Joachim MARIE <moonstroke+github@live.fr>
 * SPDX-License-Identifier: MIT */
package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.Requirement;
import io.github.moonstroke.once.Requirements;
import io.github.moonstroke.once.StableField;

class StableFieldTest {

	protected <T> StableField<T> getTestInstance(String name) {
		return new StableField<>(name);
	}

	private <T> StableField<T> getTestInstance() {
		return getTestInstance("field");
	}


	@Test
	void testConstructorNullNameFails() {
		assertThrows(NullPointerException.class, () -> new StableField<>(null));
	}

	@Test
	void testConstructorEmptyNameFails() {
		assertThrows(IllegalArgumentException.class, () -> new StableField<>(""));
	}

	@Test
	void testConstructorNullRequirementsFails() {
		assertThrows(NullPointerException.class, () -> new StableField<>("field", (Requirement<Object>[]) null));
	}

	@Test
	void testConstructorNullRequirementAloneFails() {
		assertThrows(NullPointerException.class, () -> new StableField<>("field", (Requirement<Object>) null));
	}

	@Test
	void testConstructorNullRequirementAmongOthersFails() {
		assertThrows(NullPointerException.class,
		             () -> new StableField<>("field", Requirements.POSITIVE, null, Requirements.ALLOW_NULL));
	}

	@Test
	void testFirstCallToSetDoesNotFail() {
		var sf = getTestInstance();
		Object value = new Object();
		assertDoesNotThrow(() -> sf.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		var sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		assertThrows(IllegalStateException.class, () -> sf.set(value));
	}

	@Test
	void testCallToSetNullFails() {
		var sf = getTestInstance();
		assertThrows(NullPointerException.class, () -> sf.set(null));
	}

	@Test
	void testCallToSetSelfFails() {
		var sf = getTestInstance();
		assertThrows(IllegalArgumentException.class, () -> sf.set(sf));
	}

	@Test
	void testSetCalledInParallelSucceedsOnce() {
		var sf = getTestInstance();
		int[] successesCountPtr = new int[] {0};
		/* The lambda is not factored out in a variable so that the two occurrences are two separate runnable instances
		 * (assuming the compiler does not merge them) */
		Thread thread1 = new Thread(() -> {
			try {
				sf.set(new Object());
				successesCountPtr[0]++;
			} catch (IllegalStateException e) {
				/* Ignore */
			}
		});
		Thread thread2 = new Thread(() -> {
			try {
				sf.set(new Object());
				successesCountPtr[0]++;
			} catch (IllegalStateException e) {
				/* Ignore */
			}
		});
		thread1.start();
		thread2.start();
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			fail(e);
		}
		assertEquals(1, successesCountPtr[0]);
	}

	@Test
	void testCallToTrySetNullFails() {
		var sf = getTestInstance();
		assertThrows(NullPointerException.class, () -> sf.trySet(null));
	}

	@Test
	void testCallToTrySetSelfFails() {
		var sf = getTestInstance();
		assertThrows(IllegalArgumentException.class, () -> sf.trySet(sf));
	}

	@Test
	void testTrySetReturnsTrueWhenDidSet() {
		var sf = getTestInstance();
		assertTrue(sf.trySet(new Object()));
	}

	@Test
	void testTrySetCalledInParallelSucceedsOnce() {
		var sf = getTestInstance();
		int[] successesCountPtr = new int[] {0};
		/* The lambda is not factored out in a variable so that the two occurrences are two separate runnable instances
		 * (assuming the compiler does not merge them) */
		Thread thread1 = new Thread(() -> {
			if (sf.trySet(new Object())) {
				successesCountPtr[0]++;
			}
		});
		Thread thread2 = new Thread(() -> {
			if (sf.trySet(new Object())) {
				successesCountPtr[0]++;
			}
		});
		thread1.start();
		thread2.start();
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			fail(e);
		}
		assertEquals(1, successesCountPtr[0]);
	}

	@Test
	void testTrySetReturnsTrueWhenDidNotSet() {
		var sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.trySet(new Object()));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		var sf = getTestInstance();
		assertThrows(NoSuchElementException.class, sf::get);
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		var sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		assertDoesNotThrow(() -> sf.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		var sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		assertEquals(value, sf.get());
	}

	@Test
	void testGetOptReturnsEmptyOptionalIfNotSet() {
		StableField<Object> sf = getTestInstance();
		assertTrue(sf.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsNotEmptyOptionalIfSet() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsWrappedValuePassedToSet() {
		StableField<Object> sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		assertEquals(value, sf.getOpt().get());
	}

	@Test
	void testGetDefaultAcceptsNull() {
		var sf = getTestInstance();
		assertDoesNotThrow(() -> sf.get(null));
	}

	@Test
	void testGetDefaultReturnsStoredValueIfSet() {
		var sf = getTestInstance();
		Object value = new Object(), defaultValue = new Object();
		sf.set(value);
		assertEquals(value, sf.get(defaultValue));
	}

	@Test
	void testGetDefaultReturnsDefaultValueIfNotSet() {
		var sf = getTestInstance();
		Object defaultValue = new Object();
		assertEquals(defaultValue, sf.get(defaultValue));
	}

	@Test
	void testMapNullFunctionFails() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertThrows(NullPointerException.class, () -> sf.map(null));
	}

	@Test
	void testMapFailsIfNotSet() {
		StableField<Object> sf = getTestInstance();
		assertThrows(NoSuchElementException.class, () -> sf.map(String::valueOf));
	}

	@Test
	void testMapFunctionNotInvokedIfNotSet() {
		StableField<Object> sf = getTestInstance();
		assertThrows(NoSuchElementException.class,
		             () -> sf.map(object -> fail("function should not have been called")));
	}

	@Test
	void testMapFunctionInvokedIfSet() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		boolean[] called = new boolean[1];
		assertDoesNotThrow(() -> sf.map(object -> {
			called[0] = true;
			return String.valueOf(object);
		}));
		assertTrue(called[0]);
	}

	@Test
	void testMapFunctionReturnsNullSucceeds() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertDoesNotThrow(() -> sf.map(object -> null));
	}

	@Test
	void testMapParamReturnsNullReturnsEmptyOptional() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertTrue(sf.map(object -> null).isEmpty());
	}

	@Test
	void testMapFunctionReturnsNotNullReturnsNotEmptyOptional() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.map(String::valueOf).isEmpty());
	}

	@Test
	void testIfSetNullConsumerFails() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertThrows(NullPointerException.class, () -> sf.ifSet(null));
	}

	@Test
	void testIfSetConsumerNotInvokedIfNotSet() {
		StableField<Object> sf = getTestInstance();
		sf.ifSet(object -> fail("consumer should not have been called"));
	}

	@Test
	void testIfSetConsumerInvokedIfSet() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		boolean[] called = new boolean[1];
		sf.ifSet(object -> {
			called[0] = true;
		});
		assertTrue(called[0]);
	}

	@Test
	void testEqualsNullReturnsFalseWhenUnset() {
		var sf = getTestInstance();
		assertFalse(sf.equals(null));
	}

	@Test
	void testEqualsNullReturnsFalseWhenSet() {
		var sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.equals(null));
	}

	@Test
	void testEqualsSelfReturnsTrue() {
		var sf = getTestInstance();
		assertTrue(sf.equals(sf));
	}

	@Test
	void testEqualsUnsetInstReturnsTrueWhenUnsetSameName() {
		var sf = getTestInstance();
		assertTrue(sf.equals(getTestInstance()));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenUnsetDifferentName() {
		var sf = getTestInstance();
		assertFalse(sf.equals(getTestInstance("other")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetSameName() {
		var sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.equals(getTestInstance()));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetDifferentName() {
		var sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.equals(getTestInstance("other")));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetSameName() {
		var sf = getTestInstance();
		var other = getTestInstance();
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetDifferentName() {
		var sf = getTestInstance();
		var other = getTestInstance("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueSameNameReturnsTrue() {
		var sf = getTestInstance();
		var value = new Object();
		sf.set(value);
		var other = getTestInstance();
		other.set(value);
		assertTrue(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueDifferetnNameReturnsFalse() {
		var sf = getTestInstance();
		var value = new Object();
		sf.set(value);
		var other = getTestInstance("other");
		other.set(value);
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueSameNameReturnsFalse() {
		var sf = getTestInstance();
		sf.set(new Object());
		var other = getTestInstance();
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueDifferentNameReturnsFalse() {
		var sf = getTestInstance();
		sf.set(new Object());
		var other = getTestInstance("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testHashCodeReturnsNonZeroIfNotSet() {
		var sf = getTestInstance();
		assertNotEquals(0, sf.hashCode());
	}

	@Test
	void testHashCodeReturnsNonZeroIfSet() {
		var sf = getTestInstance();
		var value = new Object();
		sf.set(value);
		assertNotEquals(0, sf.hashCode());
	}

	@Test
	void testToStringSucceedsWhenUnset() {
		var sf = getTestInstance();
		assertDoesNotThrow(sf::toString);
	}

	@Test
	void testToStringSucceedsWhenSet() {
		var sf = getTestInstance();
		sf.set(new Object());
		assertDoesNotThrow(sf::toString);
	}
}
