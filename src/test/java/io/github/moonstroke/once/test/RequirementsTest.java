package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.StableField;
import io.github.moonstroke.once.Requirements;

class RequirementsTest {

	@Test
	void testRequirementsAllowNullAcceptsNullObject() {
		StableField<Object> once = new StableField<>("optional field", Requirements.ALLOW_NULL);
		assertDoesNotThrow(() -> once.set(null));
	}

	@Test
	void testRequirementsStringNotEmptyRejectsEmptyString() {
		StableField<String> once = new StableField<>("field", Requirements.STRING_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(""));
	}

	@Test
	void testRequirementsStringNotEmptyAcceptsNonEmptyString() {
		StableField<String> once = new StableField<>("field", Requirements.STRING_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set("not empty"));
	}

	@Test
	void testRequirementsStringNotBlankRejectsBlankString() {
		StableField<String> once = new StableField<>("field", Requirements.STRING_NOT_BLANK);
		assertThrows(IllegalArgumentException.class, () -> once.set(" \t\n\f\r"));
	}

	@Test
	void testRequirementsStringNotBlankAcceptsNonBlankString() {
		StableField<String> once = new StableField<>("field", Requirements.STRING_NOT_BLANK);
		assertDoesNotThrow(() -> once.set("not blank"));
	}

	@Test
	void testRequirementsMatchesPatternRejectsNull() {
		assertThrows(NullPointerException.class, () -> Requirements.matches((Pattern) null));
	}

	@Test
	void testRequirementsMatchesPatternAcceptsMatchingString() {
		StableField<String> once = new StableField<>("field",
		                                                         Requirements.matches(Pattern.compile("foo(?:bar)?baz")));
		assertDoesNotThrow(() -> once.set("foobaz"));
	}

	@Test
	void testRequirementsMatchesPatternRejectsNonMatchingString() {
		StableField<String> once = new StableField<>("field",
		                                                         Requirements.matches(Pattern.compile("foo(?:bar)?baz")));
		assertDoesNotThrow(() -> once.set("foobar"));
	}

	@Test
	void testRequirementsMatchesStringRejectsNull() {
		assertThrows(NullPointerException.class, () -> Requirements.matches((String) null));
	}

	@Test
	void testRequirementsMatchesStringAcceptsMatchingString() {
		StableField<String> once = new StableField<>("field", Requirements.matches(Pattern.compile("a+b")));
		assertDoesNotThrow(() -> once.set("aab"));
	}

	@Test
	void testRequirementsMatchesStringRejectsNonMatchingString() {
		StableField<String> once = new StableField<>("field", Requirements.matches("a+b"));
		assertDoesNotThrow(() -> once.set("aaa"));
	}

	@Test
	void testRequirementsListNotEmptyRejectsEmptyList() {
		StableField<List<Object>> once = new StableField<>("field", Requirements.LIST_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyList()));
	}

	@Test
	void testRequirementsListNotEmptyAcceptsNonEmptyList() {
		StableField<List<Object>> once = new StableField<>("field", Requirements.LIST_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsSetNotEmptyRejectsEmptySet() {
		StableField<Set<Object>> once = new StableField<>("field", Requirements.SET_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptySet()));
	}

	@Test
	void testRequirementsSetNotEmptyAcceptsNonEmptySet() {
		StableField<Set<Object>> once = new StableField<>("field", Requirements.SET_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptyList() {
		StableField<Collection<Object>> once = new StableField<>("field",
		                                                                     Requirements.COLLECTION_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyList()));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptyList() {
		StableField<Collection<Object>> once = new StableField<>("field",
		                                                                     Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptySet() {
		StableField<Collection<Object>> once = new StableField<>("field",
		                                                                     Requirements.COLLECTION_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptySet()));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptySet() {
		StableField<Collection<Object>> once = new StableField<>("field",
		                                                                     Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsMapNotEmptyRejectsEmptyMap() {
		StableField<Map<Object, Object>> once = new StableField<>("field", Requirements.MAP_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> once.set(Collections.emptyMap()));
	}

	@Test
	void testRequirementsMapNotEmptyAcceptsNonEmptyMap() {
		StableField<Map<Object, Object>> once = new StableField<>("field", Requirements.MAP_NOT_EMPTY);
		assertDoesNotThrow(() -> once.set(Collections.singletonMap(new Object(), new Object())));
	}

	@Test
	void testRequirementsCharNotZeroRejectsZeroChar() {
		StableField<Character> once = new StableField<>("field", Requirements.CHAR_NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> once.set('\0'));
	}

	@Test
	void testRequirementsCharNotZeroAcceptsNonZeroChar() {
		StableField<Character> once = new StableField<>("field", Requirements.CHAR_NOT_ZERO);
		assertDoesNotThrow(() -> once.set('*'));
	}

	@Test
	void testRequirementsNotZeroRejectsZeroInt() {
		StableField<Integer> once = new StableField<>("field", Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> once.set(0));
	}

	@Test
	void testRequirementsNotZeroRejectsZeroDouble() {
		StableField<Double> once = new StableField<>("field", Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> once.set(0D));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZeroInt() {
		StableField<Integer> once = new StableField<>("field", Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> once.set(42));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZeroDouble() {
		StableField<Double> once = new StableField<>("field", Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> once.set(42D));
	}

	@Test
	void testRequirementsFloatNotNanRejectsFloatNan() {
		StableField<Float> once = new StableField<>("field", Requirements.FLOAT_NOT_NAN);
		assertThrows(IllegalArgumentException.class, () -> once.set(Float.NaN));
	}

	@Test
	void testRequirementsFloatFiniteRejectsNegInf() {
		StableField<Float> once = new StableField<>("field", Requirements.FLOAT_FINITE);
		assertThrows(IllegalArgumentException.class, () -> once.set(Float.NEGATIVE_INFINITY));
	}

	@Test
	void testRequirementsFloatFiniteRejectsPosInf() {
		StableField<Float> once = new StableField<>("field", Requirements.FLOAT_FINITE);
		assertThrows(IllegalArgumentException.class, () -> once.set(Float.POSITIVE_INFINITY));
	}

	@Test
	void testRequirementsFloatNotNanFiniteAcceptRegularFloat() {
		StableField<Float> once = new StableField<>("field", Requirements.FLOAT_NOT_NAN,
		                                                        Requirements.FLOAT_FINITE);
		assertDoesNotThrow(() -> once.set(42F));
	}

	@Test
	void testRequirementsDoubleNotNanRejectsDoubleNan() {
		StableField<Double> once = new StableField<>("field", Requirements.DOUBLE_NOT_NAN);
		assertThrows(IllegalArgumentException.class, () -> once.set(Double.NaN));
	}

	@Test
	void testRequirementsDoubleFiniteRejectsNegInf() {
		StableField<Double> once = new StableField<>("field", Requirements.DOUBLE_FINITE);
		assertThrows(IllegalArgumentException.class, () -> once.set(Double.NEGATIVE_INFINITY));
	}

	@Test
	void testRequirementsDoubleFiniteRejectsPosInf() {
		StableField<Double> once = new StableField<>("field", Requirements.DOUBLE_FINITE);
		assertThrows(IllegalArgumentException.class, () -> once.set(Double.POSITIVE_INFINITY));
	}

	@Test
	void testRequirementsDoubleNotNanFiniteAcceptRegularDouble() {
		StableField<Double> once = new StableField<>("field", Requirements.DOUBLE_NOT_NAN,
		                                                         Requirements.DOUBLE_FINITE);
		assertDoesNotThrow(() -> once.set(42D));
	}

	@Test
	void testRequirementsNotNegativeRejectsNegativeByte() {
		StableField<Byte> once = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertThrows(IllegalArgumentException.class, () -> once.set((byte) 0x84));
	}

	@Test
	void testRequirementsNotNegativeAcceptsPositiveShort() {
		StableField<Short> once = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> once.set((short) 42));
	}

	@Test
	void testRequirementsNotNegativeAcceptsZeroLong() {
		StableField<Long> once = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> once.set(0L));
	}

	@Test
	void testRequirementsPositiveRejectsNegativeInt() {
		StableField<Integer> once = new StableField<>("field", Requirements.POSITIVE);
		assertThrows(IllegalArgumentException.class, () -> once.set(-1));
	}

	@Test
	void testRequirementsPositiveRejectsZeroFloat() {
		StableField<Float> once = new StableField<>("field", Requirements.POSITIVE);
		assertDoesNotThrow(() -> once.set(0F));
	}

	@Test
	void testRequirementsPositiveAcceptsPositiveDouble() {
		StableField<Double> once = new StableField<>("field", Requirements.POSITIVE);
		assertDoesNotThrow(() -> once.set(42.5D));
	}

	@Test
	void testRequirementsInRangeByteRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange((byte) 5, (byte) 1));
	}

	@Test
	void testRequirementsInRangeByteRejectsByteBelowRange() {
		StableField<Byte> once = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 0));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteRangeMin() {
		StableField<Byte> once = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 1));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteInRange() {
		StableField<Byte> once = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 3));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteRangeMax() {
		StableField<Byte> once = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 5));
	}

	@Test
	void testRequirementsInRangeByteRejectsByteAboveRange() {
		StableField<Byte> once = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> once.set((byte) 7));
	}

	@Test
	void testRequirementsInRangeShortRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange((short) 5, (short) 1));
	}

	@Test
	void testRequirementsInRangeShortRejectsShortBelowRange() {
		StableField<Short> once = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 0));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortRangeMin() {
		StableField<Short> once = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 1));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortInRange() {
		StableField<Short> once = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 3));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortRangeMax() {
		StableField<Short> once = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 5));
	}

	@Test
	void testRequirementsInRangeShortRejectsShortAboveRange() {
		StableField<Short> once = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> once.set((short) 7));
	}

	@Test
	void testRequirementsInRangeIntRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(5, 1));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntBelowRange() {
		StableField<Integer> once = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(0));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMin() {
		StableField<Integer> once = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(1));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntInRange() {
		StableField<Integer> once = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(3));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMax() {
		StableField<Integer> once = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(5));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntAboveRange() {
		StableField<Integer> once = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> once.set(7));
	}

	@Test
	void testRequirementsInRangeLongRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(5L, 1L));
	}

	@Test
	void testRequirementsInRangeLongRejectsLongBelowRange() {
		StableField<Long> once = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(0L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongRangeMin() {
		StableField<Long> once = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(1L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongInRange() {
		StableField<Long> once = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(3L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongRangeMax() {
		StableField<Long> once = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(5L));
	}

	@Test
	void testRequirementsInRangeLongRejectsLongAboveRange() {
		StableField<Long> once = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> once.set(7L));
	}

	@Test
	void testRequirementsInRangeFloatRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(1.75F, 1.25F));
	}

	@Test
	void testRequirementsInRangeFloatRejectsFloatBelowRange() {
		StableField<Float> once = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(1F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatRangeMin() {
		StableField<Float> once = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(1.25F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatInRange() {
		StableField<Float> once = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(1.5F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatRangeMax() {
		StableField<Float> once = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(1.75F));
	}

	@Test
	void testRequirementsInRangeFloatRejectsFloatAboveRange() {
		StableField<Float> once = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> once.set(2F));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(1.75, 1.25));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsDoubleBelowRange() {
		StableField<Double> once = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(1D));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleRangeMin() {
		StableField<Double> once = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(1.25));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleInRange() {
		StableField<Double> once = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(1.5));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleRangeMax() {
		StableField<Double> once = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(1.75));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsDoubleAboveRange() {
		StableField<Double> once = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> once.set(2D));
	}
}
