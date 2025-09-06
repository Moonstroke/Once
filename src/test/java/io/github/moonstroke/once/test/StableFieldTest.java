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
		StableField<Object> sf = getTestInstance();
		Object value = new Object();
		assertDoesNotThrow(() -> sf.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		StableField<Object> sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		assertThrows(IllegalStateException.class, () -> sf.set(value));
	}

	@Test
	void testCallToSetNullFails() {
		StableField<Object> sf = getTestInstance();
		assertThrows(NullPointerException.class, () -> sf.set(null));
	}

	@Test
	void testCallToSetSelfFails() {
		StableField<Object> sf = getTestInstance();
		assertThrows(IllegalArgumentException.class, () -> sf.set(sf));
	}

	@Test
	void testSetCalledInParallelSucceedsOnce() {
		StableField<Object> sf = getTestInstance();
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
		StableField<Object> sf = getTestInstance();
		assertThrows(NullPointerException.class, () -> sf.trySet(null));
	}

	@Test
	void testCallToTrySetSelfFails() {
		StableField<Object> sf = getTestInstance();
		assertThrows(IllegalArgumentException.class, () -> sf.trySet(sf));
	}

	@Test
	void testTrySetReturnsTrueWhenDidSet() {
		StableField<Object> sf = getTestInstance();
		assertTrue(sf.trySet(new Object()));
	}

	@Test
	void testTrySetCalledInParallelSucceedsOnce() {
		StableField<Object> sf = getTestInstance();
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
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.trySet(new Object()));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		StableField<Object> sf = getTestInstance();
		assertThrows(NoSuchElementException.class, sf::get);
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		StableField<Object> sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		assertDoesNotThrow(() -> sf.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		StableField<Object> sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		assertEquals(value, sf.get());
	}

	@Test
	void testGetDefaultAcceptsNull() {
		StableField<Object> sf = getTestInstance();
		assertDoesNotThrow(() -> sf.get(null));
	}

	@Test
	void testGetDefaultReturnsStoredValueIfSet() {
		StableField<Object> sf = getTestInstance();
		Object value = new Object(), defaultValue = new Object();
		sf.set(value);
		assertEquals(value, sf.get(defaultValue));
	}

	@Test
	void testGetDefaultReturnsDefaultValueIfNotSet() {
		StableField<Object> sf = getTestInstance();
		Object defaultValue = new Object();
		assertEquals(defaultValue, sf.get(defaultValue));
	}

	@Test
	void testEqualsNullReturnsFalseWhenUnset() {
		StableField<Object> sf = getTestInstance();
		assertFalse(sf.equals(null));
	}

	@Test
	void testEqualsNullReturnsFalseWhenSet() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.equals(null));
	}

	@Test
	void testEqualsSelfReturnsTrue() {
		StableField<Object> sf = getTestInstance();
		assertTrue(sf.equals(sf));
	}

	@Test
	void testEqualsUnsetInstReturnsTrueWhenUnsetSameName() {
		StableField<Object> sf = getTestInstance();
		assertTrue(sf.equals(getTestInstance()));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenUnsetDifferentName() {
		StableField<Object> sf = getTestInstance();
		assertFalse(sf.equals(getTestInstance("other")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetSameName() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.equals(getTestInstance()));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetDifferentName() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertFalse(sf.equals(getTestInstance("other")));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetSameName() {
		StableField<Object> sf = getTestInstance();
		StableField<Object> other = getTestInstance();
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetDifferentName() {
		StableField<Object> sf = getTestInstance();
		StableField<Object> other = getTestInstance("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueSameNameReturnsTrue() {
		StableField<Object> sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		StableField<Object> other = getTestInstance();
		other.set(value);
		assertTrue(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueDifferetnNameReturnsFalse() {
		StableField<Object> sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		StableField<Object> other = getTestInstance("other");
		other.set(value);
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueSameNameReturnsFalse() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		StableField<Object> other = getTestInstance();
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueDifferentNameReturnsFalse() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		StableField<Object> other = getTestInstance("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testHashCodeReturnsNonZeroIfNotSet() {
		StableField<Object> sf = getTestInstance();
		assertNotEquals(0, sf.hashCode());
	}

	@Test
	void testHashCodeReturnsNonZeroIfSet() {
		StableField<Object> sf = getTestInstance();
		Object value = new Object();
		sf.set(value);
		assertNotEquals(0, sf.hashCode());
	}

	@Test
	void testToStringSucceedsWhenUnset() {
		StableField<Object> sf = getTestInstance();
		assertDoesNotThrow(sf::toString);
	}

	@Test
	void testToStringSucceedsWhenSet() {
		StableField<Object> sf = getTestInstance();
		sf.set(new Object());
		assertDoesNotThrow(sf::toString);
	}
}
