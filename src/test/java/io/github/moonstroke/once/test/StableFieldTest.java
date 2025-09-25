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
		var sf = new StableField<>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> sf.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		var sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertThrows(IllegalStateException.class, () -> sf.set(value));
	}

	@Test
	void testCallToSetNullFails() {
		var sf = new StableField<>("field");
		assertThrows(NullPointerException.class, () -> sf.set(null));
	}

	@Test
	void testCallToSetSelfFails() {
		var sf = new StableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> sf.set(sf));
	}

	@Test
	void testSetCalledInParallelSucceedsOnce() {
		var sf = new StableField<>("field");
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
		var sf = new StableField<>("field");
		assertThrows(NullPointerException.class, () -> sf.trySet(null));
	}

	@Test
	void testCallToTrySetSelfFails() {
		var sf = new StableField<>("field");
		assertThrows(IllegalArgumentException.class, () -> sf.trySet(sf));
	}

	@Test
	void testTrySetReturnsTrueWhenDidSet() {
		var sf = new StableField<>("field");
		assertTrue(sf.trySet(new Object()));
	}

	@Test
	void testTrySetCalledInParallelSucceedsOnce() {
		var sf = new StableField<>("field");
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
		var sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.trySet(new Object()));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		var sf = new StableField<>("field");
		assertThrows(NoSuchElementException.class, sf::get);
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		var sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertDoesNotThrow(() -> sf.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		var sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertEquals(value, sf.get());
	}

	@Test
	void testGetDefaultAcceptsNull() {
		var sf = new StableField<>("field");
		assertDoesNotThrow(() -> sf.get(null));
	}

	@Test
	void testGetDefaultReturnsStoredValueIfSet() {
		var sf = new StableField<>("field");
		Object value = new Object(), defaultValue = new Object();
		sf.set(value);
		assertEquals(value, sf.get(defaultValue));
	}

	@Test
	void testGetDefaultReturnsDefaultValueIfNotSet() {
		var sf = new StableField<>("field");
		Object defaultValue = new Object();
		assertEquals(defaultValue, sf.get(defaultValue));
	}

	@Test
	void testEqualsNullReturnsFalseWhenUnset() {
		var sf = new StableField<>("field");
		assertFalse(sf.equals(null));
	}

	@Test
	void testEqualsNullReturnsFalseWhenSet() {
		var sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.equals(null));
	}

	@Test
	void testEqualsSelfReturnsTrue() {
		var sf = new StableField<>("field");
		assertTrue(sf.equals(sf));
	}

	@Test
	void testEqualsUnsetInstReturnsTrueWhenUnsetSameName() {
		var sf = new StableField<>("field");
		assertTrue(sf.equals(new StableField<>("field")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenUnsetDifferentName() {
		var sf = new StableField<>("field");
		assertFalse(sf.equals(new StableField<>("other")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetSameName() {
		var sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.equals(new StableField<>("field")));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetDifferentName() {
		var sf = new StableField<>("field");
		sf.set(new Object());
		assertFalse(sf.equals(new StableField<>("other")));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetSameName() {
		var sf = new StableField<>("field");
		var other = new StableField<>("field");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetDifferentName() {
		var sf = new StableField<>("field");
		var other = new StableField<>("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueSameNameReturnsTrue() {
		var sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		var other = new StableField<>("field");
		other.set(value);
		assertTrue(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueDifferetnNameReturnsFalse() {
		var sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		var other = new StableField<>("other");
		other.set(value);
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueSameNameReturnsFalse() {
		var sf = new StableField<>("field");
		sf.set(new Object());
		var other = new StableField<>("field");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueDifferentNameReturnsFalse() {
		var sf = new StableField<>("field");
		sf.set(new Object());
		var other = new StableField<>("other");
		other.set(new Object());
		assertFalse(sf.equals(other));
	}

	@Test
	void testHashCodeReturnsNonZeroIfNotSet() {
		var sf = new StableField<>("field");
		assertNotEquals(0, sf.hashCode());
	}

	@Test
	void testHashCodeReturnsNonZeroIfSet() {
		var sf = new StableField<>("field");
		Object value = new Object();
		sf.set(value);
		assertNotEquals(0, sf.hashCode());
	}

	@Test
	void testToStringSucceedsWhenUnset() {
		var sf = new StableField<>("field");
		assertDoesNotThrow(sf::toString);
	}

	@Test
	void testToStringSucceedsWhenSet() {
		var sf = new StableField<>("field");
		sf.set(new Object());
		assertDoesNotThrow(sf::toString);
	}
}
