package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.OnceSettableField;
import io.github.moonstroke.once.Requirements;

class OnceSettableFieldTest {

	@Test
	void testConstructorCallNullNameFails() {
		assertThrows(NullPointerException.class, () -> new OnceSettableField<Object>(null));
	}

	@Test
	void testConstructorCallEmptyNameFails() {
		assertThrows(IllegalArgumentException.class, () -> new OnceSettableField<Object>(""));
	}

	@Test
	void testFirstCallToSetDoesNotFail() {
		OnceSettableField<Object> once = new OnceSettableField<Object>("field");
		Object value = new Object();
		assertDoesNotThrow(() -> once.set(value));
	}

	@Test
	void testSecondCallToSetFails() {
		OnceSettableField<Object> once = new OnceSettableField<Object>("field");
		Object value = new Object();
		once.set(value);
		assertThrows(IllegalStateException.class, () -> once.set(value));
	}

	@Test
	void testCallToSetNullFails() {
		OnceSettableField<Object> once = new OnceSettableField<Object>("field");
		assertThrows(NullPointerException.class, () -> once.set(null));
	}

	@Test
	void testCallToGetWithoutSetFails() {
		OnceSettableField<Object> once = new OnceSettableField<Object>("field");
		assertThrows(IllegalStateException.class, () -> once.get());
	}

	@Test
	void testCallToGetAfterSetDoesNotFail() {
		OnceSettableField<Object> once = new OnceSettableField<Object>("field");
		Object value = new Object();
		once.set(value);
		assertDoesNotThrow(() -> once.get());
	}

	@Test
	void testGetReturnsValuePassedToSet() {
		OnceSettableField<Object> once = new OnceSettableField<Object>("field");
		Object value = new Object();
		once.set(value);
		assertEquals(value, once.get());
	}


	/* Requirements */

	@Test
	void testRequirementsStringNotEmptyRejectsEmptyString() {
		OnceSettableField<String> once = new OnceSettableField<>("field", Requirements.STRING_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(""));
	}

	@Test
	void testRequirementsStringNotEmptyAcceptsNonEmptyString() {
		OnceSettableField<String> once = new OnceSettableField<>("field", Requirements.STRING_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set("not empty"));
	}

	@Test
	void testRequirementsStringNotBlankRejectsBlankString() {
		OnceSettableField<String> once = new OnceSettableField<>("field", Requirements.STRING_NOT_BLANK);
		assertThrows(IllegalArgumentException.class, () -> once.set(" \t\n\f\r"));
	}

	@Test
	void testRequirementsStringNotBlankAcceptsNonBlankString() {
		OnceSettableField<String> once = new OnceSettableField<>("field", Requirements.STRING_NOT_BLANK);
		assertDoesNotThrow(() -> once.set("not blank"));
	}

	@Test
	void testRequirementsListNotEmptyRejectsEmptyList() {
		OnceSettableField<List<?>> once = new OnceSettableField<>("field", Requirements.LIST_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyList()));
	}

	@Test
	void testRequirementsListNotEmptyAcceptsNonEmptyList() {
		OnceSettableField<List<?>> once = new OnceSettableField<>("field", Requirements.LIST_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsSetNotEmptyRejectsEmptySet() {
		OnceSettableField<Set<?>> once = new OnceSettableField<>("field", Requirements.SET_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptySet()));
	}

	@Test
	void testRequirementsSetNotEmptyAcceptsNonEmptySet() {
		OnceSettableField<Set<?>> once = new OnceSettableField<>("field", Requirements.SET_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptyList() {
		OnceSettableField<Collection<?>> once = new OnceSettableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyList()));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptyList() {
		OnceSettableField<Collection<?>> once = new OnceSettableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptySet() {
		OnceSettableField<Collection<?>> once = new OnceSettableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptySet()));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptySet() {
		OnceSettableField<Collection<?>> once = new OnceSettableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsMapNotEmptyRejectsEmptyMap() {
		OnceSettableField<Map<?, ?>> once = new OnceSettableField<>("field", Requirements.MAP_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyMap()));
	}

	@Test
	void testRequirementsMapNotEmptyAcceptsNonEmptyMap() {
		OnceSettableField<Map<?, ?>> once = new OnceSettableField<>("field", Requirements.MAP_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonMap(new Object(), new Object())));
	}
}
