package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.OnceSettableField;
import io.github.moonstroke.once.Requirements;

class RequirementsTest {

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
		OnceSettableField<List<Object>> once = new OnceSettableField<>("field", Requirements.LIST_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyList()));
	}

	@Test
	void testRequirementsListNotEmptyAcceptsNonEmptyList() {
		OnceSettableField<List<Object>> once = new OnceSettableField<>("field", Requirements.LIST_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsSetNotEmptyRejectsEmptySet() {
		OnceSettableField<Set<Object>> once = new OnceSettableField<>("field", Requirements.SET_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptySet()));
	}

	@Test
	void testRequirementsSetNotEmptyAcceptsNonEmptySet() {
		OnceSettableField<Set<Object>> once = new OnceSettableField<>("field", Requirements.SET_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptyList() {
		OnceSettableField<Collection<Object>> once = new OnceSettableField<>("field",
		                                                                     Requirements.COLLECTION_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyList()));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptyList() {
		OnceSettableField<Collection<Object>> once = new OnceSettableField<>("field",
		                                                                     Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptySet() {
		OnceSettableField<Collection<Object>> once = new OnceSettableField<>("field",
		                                                                     Requirements.COLLECTION_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptySet()));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptySet() {
		OnceSettableField<Collection<Object>> once = new OnceSettableField<>("field",
		                                                                     Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsMapNotEmptyRejectsEmptyMap() {
		OnceSettableField<Map<Object, Object>> once = new OnceSettableField<>("field", Requirements.MAP_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyMap()));
	}

	@Test
	void testRequirementsMapNotEmptyAcceptsNonEmptyMap() {
		OnceSettableField<Map<Object, Object>> once = new OnceSettableField<>("field", Requirements.MAP_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonMap(new Object(), new Object())));
	}

	@Test
	void testRequirementsCharNotZeroRejectsZeroChar() {
		OnceSettableField<Character> once = new OnceSettableField<>("field", Requirements.CHAR_NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> once.set('\0'));
	}

	@Test
	void testRequirementsCharNotZeroAcceptsNonZeroChar() {
		OnceSettableField<Character> once = new OnceSettableField<>("field", Requirements.CHAR_NOT_ZERO);
		assertDoesNotThrow(() -> once.set('*'));
	}

	@Test
	void testRequirementsNotZeroRejectsZeroInt() {
		OnceSettableField<Integer> once = new OnceSettableField<>("field", Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> once.set(0));
	}

	@Test
	void testRequirementsNotZeroRejectsZeroDouble() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> once.set(0D));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZeroInt() {
		OnceSettableField<Integer> once = new OnceSettableField<>("field", Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> once.set(42));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZeroDouble() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> once.set(42D));
	}

	@Test
	void testRequirementsFloatNotNanRejectsFloatNan() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.FLOAT_NOT_NAN);
		assertThrows(IllegalArgumentException.class, () -> once.set(Float.NaN));
	}

	@Test
	void testRequirementsFloatFiniteRejectsNegInf() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.FLOAT_FINITE);
		assertThrows(IllegalArgumentException.class, () -> once.set(Float.NEGATIVE_INFINITY));
	}

	@Test
	void testRequirementsFloatFiniteRejectsPosInf() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.FLOAT_FINITE);
		assertThrows(IllegalArgumentException.class, () -> once.set(Float.POSITIVE_INFINITY));
	}

	@Test
	void testRequirementsFloatNotNanFiniteAcceptRegularFloat() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.FLOAT_NOT_NAN,
		                                                        Requirements.FLOAT_FINITE);
		assertDoesNotThrow(() -> once.set(42F));
	}

	@Test
	void testRequirementsDoubleNotNanRejectsDoubleNan() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.DOUBLE_NOT_NAN);
		assertThrows(IllegalArgumentException.class, () -> once.set(Double.NaN));
	}

	@Test
	void testRequirementsDoubleFiniteRejectsNegInf() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.DOUBLE_FINITE);
		assertThrows(IllegalArgumentException.class, () -> once.set(Double.NEGATIVE_INFINITY));
	}

	@Test
	void testRequirementsDoubleFiniteRejectsPosInf() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.DOUBLE_FINITE);
		assertThrows(IllegalArgumentException.class, () -> once.set(Double.POSITIVE_INFINITY));
	}

	@Test
	void testRequirementsDoubleNotNanFiniteAcceptRegularDouble() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.DOUBLE_NOT_NAN,
		                                                         Requirements.DOUBLE_FINITE);
		assertDoesNotThrow(() -> once.set(42D));
	}

	@Test
	void testRequirementsNotNegativeRejectsNegativeByte() {
		OnceSettableField<Byte> once = new OnceSettableField<>("field", Requirements.NOT_NEGATIVE);
		assertThrows(IllegalArgumentException.class, () -> once.set((byte) 0x84));
	}

	@Test
	void testRequirementsNotNegativeAcceptsPositiveShort() {
		OnceSettableField<Short> once = new OnceSettableField<>("field", Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> once.set((short) 42));
	}

	@Test
	void testRequirementsNotNegativeAcceptsZeroLong() {
		OnceSettableField<Long> once = new OnceSettableField<>("field", Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> once.set(0L));
	}
}
