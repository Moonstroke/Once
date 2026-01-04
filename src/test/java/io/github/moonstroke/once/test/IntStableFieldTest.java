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

import io.github.moonstroke.once.IntStableField;
import io.github.moonstroke.once.Requirement;
import io.github.moonstroke.once.Requirements;

class IntStableFieldTest {

	protected IntStableField getTestInstance(String name) {
		return new IntStableField(name);
	}

	private IntStableField getTestInstance() {
		return getTestInstance("field");
	}


	@Test
	void testConstructorNullNameFails() {
		assertThrows(NullPointerException.class, () -> new IntStableField(null));
	}

	@Test
	void testConstructorEmptyNameFails() {
		assertThrows(IllegalArgumentException.class, () -> new IntStableField(""));
	}

	@Test
	void testConstructorNullRequirementsFails() {
		assertThrows(NullPointerException.class, () -> new IntStableField("field", (Requirement<Object>[]) null));
	}

	@Test
	void testConstructorNullRequirementAloneFails() {
		assertThrows(NullPointerException.class, () -> new IntStableField("field", (Requirement<Object>) null));
	}

	@Test
	void testConstructorNullRequirementAmongOthersFails() {
		assertThrows(NullPointerException.class,
		             () -> new IntStableField("field", Requirements.POSITIVE, null, Requirements.NOT_ZERO));
	}

	@Test
	void testConstructorRequirementsAllowNullFails() {
		assertThrows(IllegalArgumentException.class, () -> new IntStableField("field", Requirements.ALLOW_NULL));
	}

	@Test
	void testFirstCallToSetDoesNotFail() {
		var sf = getTestInstance();
		var value = 42;
		assertDoesNotThrow(() -> sf.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		var sf = getTestInstance();
		var value = 42;
		sf.set(value);
		assertThrows(IllegalStateException.class, () -> sf.set(value));
	}

	@Test
	void testSetCalledInParallelSucceedsOnce() {
		var sf = getTestInstance();
		var successesCountPtr = new int[] {0};
		/* The lambda is not factored out in a variable so that the two occurrences are two separate runnable instances
		 * (assuming the compiler does not merge them) */
		var thread1 = new Thread(() -> {
			try {
				sf.set(1);
				successesCountPtr[0]++;
			} catch (IllegalStateException e) {
				/* Ignore */
			}
		});
		var thread2 = new Thread(() -> {
			try {
				sf.set(2);
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
	void testTrySetReturnsTrueWhenDidSet() {
		var sf = getTestInstance();
		assertTrue(sf.trySet(42));
	}

	@Test
	void testTrySetCalledInParallelSucceedsOnce() {
		var sf = getTestInstance();
		var successesCountPtr = new int[] {0};
		/* The lambda is not factored out in a variable so that the two occurrences are two separate runnable instances
		 * (assuming the compiler does not merge them) */
		var thread1 = new Thread(() -> {
			if (sf.trySet(1)) {
				successesCountPtr[0]++;
			}
		});
		var thread2 = new Thread(() -> {
			if (sf.trySet(2)) {
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
		sf.set(42);
		assertFalse(sf.trySet(43));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		var sf = getTestInstance();
		assertThrows(NoSuchElementException.class, sf::get);
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		var sf = getTestInstance();
		var value = 42;
		sf.set(value);
		assertDoesNotThrow(() -> sf.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		var sf = getTestInstance();
		var value = 42;
		sf.set(value);
		assertEquals(value, sf.get());
	}

	@Test
	void testGetOptReturnsEmptyOptionalIfNotSet() {
		var sf = getTestInstance();
		assertTrue(sf.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsNotEmptyOptionalIfSet() {
		var sf = getTestInstance();
		sf.set(42);
		assertFalse(sf.getOpt().isEmpty());
	}

	@Test
	void testGetOptReturnsWrappedValuePassedToSet() {
		var sf = getTestInstance();
		var value = 42;
		sf.set(value);
		assertEquals(value, sf.getOpt().get());
	}

	@Test
	void testGetDefaultReturnsStoredValueIfSet() {
		var sf = getTestInstance();
		var value = 42;
		var defaultValue = 43;
		sf.set(value);
		assertEquals(value, sf.get(defaultValue));
	}

	@Test
	void testGetDefaultReturnsDefaultValueIfNotSet() {
		var sf = getTestInstance();
		var defaultValue = 42;
		assertEquals(defaultValue, sf.get(defaultValue));
	}

	@Test
	void testMapNullFunctionFails() {
		var sf = getTestInstance();
		sf.set(42);
		assertThrows(NullPointerException.class, () -> sf.map(null));
	}

	@Test
	void testMapFailsIfNotSet() {
		var sf = getTestInstance();
		assertThrows(NoSuchElementException.class, () -> sf.map(String::valueOf));
	}

	@Test
	void testMapFunctionNotInvokedIfNotSet() {
		var sf = getTestInstance();
		assertThrows(NoSuchElementException.class,
		             () -> sf.map(object -> fail("function should not have been called")));
	}

	@Test
	void testMapFunctionInvokedIfSet() {
		var sf = getTestInstance();
		sf.set(42);
		var called = new boolean[1];
		assertDoesNotThrow(() -> sf.map(object -> {
			called[0] = true;
			return String.valueOf(object);
		}));
		assertTrue(called[0]);
	}

	@Test
	void testMapFunctionReturnsNullSucceeds() {
		var sf = getTestInstance();
		sf.set(42);
		assertDoesNotThrow(() -> sf.map(object -> null));
	}

	@Test
	void testMapParamReturnsNullReturnsEmptyOptional() {
		var sf = getTestInstance();
		sf.set(42);
		assertTrue(sf.map(object -> null).isEmpty());
	}

	@Test
	void testMapFunctionReturnsNotNullReturnsNotEmptyOptional() {
		var sf = getTestInstance();
		sf.set(42);
		assertFalse(sf.map(String::valueOf).isEmpty());
	}

	@Test
	void testIfSetNullConsumerFails() {
		var sf = getTestInstance();
		sf.set(42);
		assertThrows(NullPointerException.class, () -> sf.ifSet(null));
	}

	@Test
	void testIfSetConsumerNotInvokedIfNotSet() {
		var sf = getTestInstance();
		sf.ifSet(object -> fail("consumer should not have been called"));
	}

	@Test
	void testIfSetConsumerInvokedIfSet() {
		var sf = getTestInstance();
		sf.set(42);
		var called = new boolean[1];
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
		sf.set(42);
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
		sf.set(42);
		assertFalse(sf.equals(getTestInstance()));
	}

	@Test
	void testEqualsUnsetInstReturnsFalseWhenSetDifferentName() {
		var sf = getTestInstance();
		sf.set(42);
		assertFalse(sf.equals(getTestInstance("other")));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetSameName() {
		var sf = getTestInstance();
		var other = getTestInstance();
		other.set(42);
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsSetInstReturnsFalseWhenUnsetDifferentName() {
		var sf = getTestInstance();
		var other = getTestInstance("other");
		other.set(42);
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueSameNameReturnsTrue() {
		var sf = getTestInstance();
		var value = 42;
		sf.set(value);
		var other = getTestInstance();
		other.set(value);
		assertTrue(sf.equals(other));
	}

	@Test
	void testEqualsInstSetSameValueDifferetnNameReturnsFalse() {
		var sf = getTestInstance();
		var value = 42;
		sf.set(value);
		var other = getTestInstance("other");
		other.set(value);
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueSameNameReturnsFalse() {
		var sf = getTestInstance();
		sf.set(42);
		var other = getTestInstance();
		other.set(43);
		assertFalse(sf.equals(other));
	}

	@Test
	void testEqualsInstSetOtherValueDifferentNameReturnsFalse() {
		var sf = getTestInstance();
		sf.set(42);
		var other = getTestInstance("other");
		other.set(43);
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
		var value = 42;
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
		sf.set(42);
		assertDoesNotThrow(sf::toString);
	}
}
