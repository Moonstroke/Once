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

	@Test
	void testRequirementsPositiveRejectsNegativeInt() {
		OnceSettableField<Integer> once = new OnceSettableField<>("field", Requirements.POSITIVE);
		assertThrows(IllegalArgumentException.class, () -> once.set(-1));
	}

	@Test
	void testRequirementsPositiveRejectsZeroFloat() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.POSITIVE);
		assertDoesNotThrow(() -> once.set(0F));
	}

	@Test
	void testRequirementsPositiveAcceptsPositiveDouble() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.POSITIVE);
		assertDoesNotThrow(() -> once.set(42.5D));
	}

	@Test
	void testRequirementsInRangeByteRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange((byte) 5, (byte) 1));
	}

	@Test
	void testRequirementsInRangeByteRejectsByteBelowRange() {
		OnceSettableField<Byte> once = new OnceSettableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 0));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteRangeMin() {
		OnceSettableField<Byte> once = new OnceSettableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 1));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteInRange() {
		OnceSettableField<Byte> once = new OnceSettableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 3));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteRangeMax() {
		OnceSettableField<Byte> once = new OnceSettableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 5));
	}

	@Test
	void testRequirementsInRangeByteRejectsByteAboveRange() {
		OnceSettableField<Byte> once = new OnceSettableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 7));
	}

	@Test
	void testRequirementsInRangeShortRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange((short) 5, (short) 1));
	}

	@Test
	void testRequirementsInRangeShortRejectsShortBelowRange() {
		OnceSettableField<Short> once = new OnceSettableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 0));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortRangeMin() {
		OnceSettableField<Short> once = new OnceSettableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 1));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortInRange() {
		OnceSettableField<Short> once = new OnceSettableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 3));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortRangeMax() {
		OnceSettableField<Short> once = new OnceSettableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 5));
	}

	@Test
	void testRequirementsInRangeShortRejectsShortAboveRange() {
		OnceSettableField<Short> once = new OnceSettableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 7));
	}

	@Test
	void testRequirementsInRangeIntRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(5, 1));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntBelowRange() {
		OnceSettableField<Integer> once = new OnceSettableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(0));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMin() {
		OnceSettableField<Integer> once = new OnceSettableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(1));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntInRange() {
		OnceSettableField<Integer> once = new OnceSettableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(3));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMax() {
		OnceSettableField<Integer> once = new OnceSettableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(5));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntAboveRange() {
		OnceSettableField<Integer> once = new OnceSettableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(7));
	}

	@Test
	void testRequirementsInRangeLongRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(5L, 1L));
	}

	@Test
	void testRequirementsInRangeLongRejectsLongBelowRange() {
		OnceSettableField<Long> once = new OnceSettableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(0L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongRangeMin() {
		OnceSettableField<Long> once = new OnceSettableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(1L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongInRange() {
		OnceSettableField<Long> once = new OnceSettableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(3L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongRangeMax() {
		OnceSettableField<Long> once = new OnceSettableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(5L));
	}

	@Test
	void testRequirementsInRangeLongRejectsLongAboveRange() {
		OnceSettableField<Long> once = new OnceSettableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(7L));
	}

	@Test
	void testRequirementsInRangeFloatRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(1.75F, 1.25F));
	}

	@Test
	void testRequirementsInRangeFloatRejectsFloatBelowRange() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(1F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatRangeMin() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(1.25F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatInRange() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(1.5F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatRangeMax() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(1.75F));
	}

	@Test
	void testRequirementsInRangeFloatRejectsFloatAboveRange() {
		OnceSettableField<Float> once = new OnceSettableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(2F));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(1.75, 1.25));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsDoubleBelowRange() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(1D));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleRangeMin() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(1.25));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleInRange() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(1.5));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleRangeMax() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(1.75));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsDoubleAboveRange() {
		OnceSettableField<Double> once = new OnceSettableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(2D));
	}
}
