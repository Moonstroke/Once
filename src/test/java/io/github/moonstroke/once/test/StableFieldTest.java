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
	void testSetFirstCallSucceeds() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> sf.set(value));
	}

	@Test
	void testSetSecondCallFails() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertThrows(IllegalStateException.class, () -> sf.set(value));
	}

	@Test
	void testSetNullValueFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(NullPointerException.class, () -> sf.set(null));
	}

	@Test
	void testSetSelfValueFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> sf.set(sf));
	}

	@Test
	void testSetCalledInParallelSucceedsOnce() {
		StableField<Object> sf = new StableField<>("field");
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
	void testTrySetNullValueFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(NullPointerException.class, () -> sf.trySet(null));
	}

	@Test
	void testTrySetSelfValueFails() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> sf.trySet(sf));
	}

	@Test
	void testTrySetReturnsTrueWhenDidSet() {
		StableField<Object> sf = new StableField<>("field");
		assertTrue(sf.trySet(new Object()));
	}

	@Test
	void testTrySetCalledInParallelSucceedsOnce() {
		StableField<Object> sf = new StableField<>("field");
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
	void testTrySetReturnsFalseWhenDidNotSet() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.trySet(new Object()));
	}

	@Test
	void testGetFailsIfNotSet() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(NoSuchElementException.class, sf::get);
	}

	@Test
	void testGetSucceedsIfSet() {
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
	void testGetOptReturnsEmptyOptionalIfNotSet() {
		StableField<Object> sf = new StableField<>("field");
		assertTrue(sf.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsNotEmptyOptionalIfSet() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsWrappedValuePassedToSet() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertEquals(value, sf.getOpt().get());
	}

	@Test
	void testGetDefaultNullDefautlValueSucceeds() {
		StableField<Object> sf = new StableField<>("field");
		assertDoesNotThrow(() -> sf.get(null));
	}

	@Test
	void testGetDefaultReturnsValuePassedToSetIfSet() {
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
	void testMapNullFunctionFails() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertThrows(NullPointerException.class, () -> sf.map(null));
	}

	@Test
	void testMapFailsIfNotSet() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(NoSuchElementException.class, () -> sf.map(String::valueOf));
	}

	@Test
	void testMapFunctionNotInvokedIfNotSet() {
		StableField<Object> sf = new StableField<>("field");
		assertThrows(NoSuchElementException.class, () -> sf.map(object -> fail("function should not have been called")));
	}

	@Test
	void testMapFunctionInvokedIfSet() {
		StableField<Object> sf = new StableField<>("field");
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
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertDoesNotThrow(() -> sf.map(object -> null));
	}

	@Test
	void testMapParamReturnsNullReturnsEmptyOptional() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertTrue(sf.map(object -> null).isEmpty());
	}

	@Test
	void testMapFunctionReturnsNotNullReturnsNotEmptyOptional() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.map(String::valueOf).isEmpty());
	}

	@Test
	void testIfSetNullConsumerFails() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertThrows(NullPointerException.class, () -> sf.ifSet(null));
	}

	@Test
	void testIfSetConsumerNotInvokedIfNotSet() {
		StableField<Object> sf = new StableField<>("field");
		sf.ifSet(object -> fail("consumer should not have been called"));
	}

	@Test
	void testIfSetConsumerInvokedIfSet() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		boolean[] called = new boolean[1];
		sf.ifSet(object -> {
			called[0] = true;
		});
		assertTrue(called[0]);
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
	void testEqualsUnsetInstReturnsTrueWhenUnsetSameName() {
		StableField<Object> sf = new StableField<>("field");
		assertTrue(sf.equals(new StableField<>("field")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenUnsetDifferentName() {
		StableField<Object> sf = new StableField<>("field");
		assertFalse(sf.equals(new StableField<>("other")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetSameName() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.equals(new StableField<>("field")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetDifferentName() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.equals(new StableField<>("other")));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetSameName() {
		StableField<Object> sf = new StableField<>("field");
		StableField<Object> other = new StableField<>("field");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetDifferentName() {
		StableField<Object> sf = new StableField<>("field");
		StableField<Object> other = new StableField<>("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueSameNameReturnsTrue() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		StableField<Object> other = new StableField<>("field");
		other.set(value);
		assertTrue(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueDifferetnNameReturnsFalse() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		StableField<Object> other = new StableField<>("other");
		other.set(value);
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueSameNameReturnsFalse() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		StableField<Object> other = new StableField<>("field");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueDifferentNameReturnsFalse() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		StableField<Object> other = new StableField<>("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testHashCodeReturnsNonZeroIfNotSet() {
		StableField<Object> sf = new StableField<>("field");
		assertNotEquals(0, sf.hashCode());
	}

	@Test
	void testHashCodeReturnsNonZeroIfSet() {
		StableField<Object> sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertNotEquals(0, sf.hashCode());
	}

	@Test
	void testToStringSucceedsWhenUnset() {
		StableField<Object> sf = new StableField<>("field");
		assertDoesNotThrow(sf::toString);
	}

	@Test
	void testToStringSucceedsWhenSet() {
		StableField<Object> sf = new StableField<>("field");
		sf.set(new Object());
		assertDoesNotThrow(sf::toString);
	}
}
