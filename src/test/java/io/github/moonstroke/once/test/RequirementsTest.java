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
		StableField<Object> sf = new StableField<>("optional field", Requirements.ALLOW_NULL);
		assertDoesNotThrow(() -> sf.set(null));
	}

	@Test
	void testRequirementsAllowNullDoesNotAllowSetTwice() {
		StableField<Object> sf = new StableField<>("optional field", Requirements.ALLOW_NULL);
		sf.set(null);
		assertThrows(IllegalStateException.class, () -> sf.set(null));
	}

	@Test
	void testRequirementsAllowNullDoesNotAllowSetZeroTimesBeforeGet() {
		StableField<Object> sf = new StableField<>("optional field", Requirements.ALLOW_NULL);
		assertThrows(IllegalStateException.class, sf::get);
	}

	@Test
	void testRequirementsStringNotEmptyRejectsEmptyString() {
		StableField<String> sf = new StableField<>("field", Requirements.STRING_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> sf.set(""));
	}

	@Test
	void testRequirementsStringNotEmptyAcceptsNonEmptyString() {
		StableField<String> sf = new StableField<>("field", Requirements.STRING_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set("not empty"));
	}

	@Test
	void testRequirementsStringNotBlankRejectsBlankString() {
		StableField<String> sf = new StableField<>("field", Requirements.STRING_NOT_BLANK);
		assertThrows(IllegalArgumentException.class, () -> sf.set(" \t\n\f\r"));
	}

	@Test
	void testRequirementsStringNotBlankAcceptsNonBlankString() {
		StableField<String> sf = new StableField<>("field", Requirements.STRING_NOT_BLANK);
		assertDoesNotThrow(() -> sf.set("not blank"));
	}

	@Test
	void testRequirementsMatchesPatternRejectsNull() {
		assertThrows(NullPointerException.class, () -> Requirements.matches((Pattern) null));
	}

	@Test
	void testRequirementsMatchesPatternAcceptsMatchingString() {
		StableField<String> sf = new StableField<>("field", Requirements.matches(Pattern.compile("foo(?:bar)?baz")));
		assertDoesNotThrow(() -> sf.set("foobaz"));
	}

	@Test
	void testRequirementsMatchesPatternRejectsNonMatchingString() {
		StableField<String> sf = new StableField<>("field", Requirements.matches(Pattern.compile("foo(?:bar)?baz")));
		assertThrows(IllegalArgumentException.class, () -> sf.set("foobar"));
	}

	@Test
	void testRequirementsMatchesStringRejectsNull() {
		assertThrows(NullPointerException.class, () -> Requirements.matches((String) null));
	}

	@Test
	void testRequirementsMatchesStringAcceptsMatchingString() {
		StableField<String> sf = new StableField<>("field", Requirements.matches(Pattern.compile("a+b")));
		assertDoesNotThrow(() -> sf.set("aab"));
	}

	@Test
	void testRequirementsMatchesStringRejectsNonMatchingString() {
		StableField<String> sf = new StableField<>("field", Requirements.matches("a+b"));
		assertThrows(IllegalArgumentException.class, () -> sf.set("aaa"));
	}

	@Test
	void testRequirementsListNotEmptyRejectsEmptyList() {
		StableField<List<Object>> sf = new StableField<>("field", Requirements.LIST_NOT_EMPTY);
		List<Object> emptyList = Collections.emptyList();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptyList));
	}

	@Test
	void testRequirementsListNotEmptyAcceptsNonEmptyList() {
		StableField<List<Object>> sf = new StableField<>("field", Requirements.LIST_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsSetNotEmptyRejectsEmptySet() {
		StableField<Set<Object>> sf = new StableField<>("field", Requirements.SET_NOT_EMPTY);
		Set<Object> emptySet = Collections.emptySet();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptySet));
	}

	@Test
	void testRequirementsSetNotEmptyAcceptsNonEmptySet() {
		StableField<Set<Object>> sf = new StableField<>("field", Requirements.SET_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptyList() {
		StableField<Collection<Object>> sf = new StableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		List<Object> emptyCollection = Collections.emptyList();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptyCollection));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptyList() {
		StableField<Collection<Object>> sf = new StableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptySet() {
		StableField<Collection<Object>> sf = new StableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		Set<Object> emptyCollection = Collections.emptySet();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptyCollection));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptySet() {
		StableField<Collection<Object>> sf = new StableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsMapNotEmptyRejectsEmptyMap() {
		StableField<Map<Object, Object>> sf = new StableField<>("field", Requirements.MAP_NOT_EMPTY);
		Map<Object, Object> emptyMap = Collections.emptyMap();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptyMap));
	}

	@Test
	void testRequirementsMapNotEmptyAcceptsNonEmptyMap() {
		StableField<Map<Object, Object>> sf = new StableField<>("field", Requirements.MAP_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singletonMap(new Object(), new Object())));
	}

	@Test
	void testRequirementsCharNotNulRejectsNulChar() {
		StableField<Character> sf = new StableField<>("field", Requirements.CHAR_NOT_NUL);
		assertThrows(IllegalArgumentException.class, () -> sf.set('\0'));
	}

	@Test
	void testRequirementsCharNotNulAcceptsNonNulChar() {
		StableField<Character> sf = new StableField<>("field", Requirements.CHAR_NOT_NUL);
		assertDoesNotThrow(() -> sf.set('*'));
	}

	@Test
	void testRequirementsNotZeroRejectsZeroInt() {
		StableField<Integer> sf = new StableField<>("field", Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> sf.set(0));
	}

	@Test
	void testRequirementsNotZeroRejectsZeroDouble() {
		StableField<Double> sf = new StableField<>("field", Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> sf.set(0D));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZeroInt() {
		StableField<Integer> sf = new StableField<>("field", Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> sf.set(42));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZeroDouble() {
		StableField<Double> sf = new StableField<>("field", Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> sf.set(42D));
	}

	@Test
	void testRequirementsFloatNotNanRejectsFloatNan() {
		StableField<Float> sf = new StableField<>("field", Requirements.FLOAT_NOT_NAN);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Float.NaN));
	}

	@Test
	void testRequirementsFloatFiniteRejectsNegInf() {
		StableField<Float> sf = new StableField<>("field", Requirements.FLOAT_FINITE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Float.NEGATIVE_INFINITY));
	}

	@Test
	void testRequirementsFloatFiniteRejectsPosInf() {
		StableField<Float> sf = new StableField<>("field", Requirements.FLOAT_FINITE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Float.POSITIVE_INFINITY));
	}

	@Test
	void testRequirementsFloatNotNanFiniteAcceptRegularFloat() {
		StableField<Float> sf = new StableField<>("field", Requirements.FLOAT_NOT_NAN, Requirements.FLOAT_FINITE);
		assertDoesNotThrow(() -> sf.set(42F));
	}

	@Test
	void testRequirementsDoubleNotNanRejectsDoubleNan() {
		StableField<Double> sf = new StableField<>("field", Requirements.DOUBLE_NOT_NAN);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Double.NaN));
	}

	@Test
	void testRequirementsDoubleFiniteRejectsNegInf() {
		StableField<Double> sf = new StableField<>("field", Requirements.DOUBLE_FINITE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Double.NEGATIVE_INFINITY));
	}

	@Test
	void testRequirementsDoubleFiniteRejectsPosInf() {
		StableField<Double> sf = new StableField<>("field", Requirements.DOUBLE_FINITE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Double.POSITIVE_INFINITY));
	}

	@Test
	void testRequirementsDoubleNotNanFiniteAcceptRegularDouble() {
		StableField<Double> sf = new StableField<>("field", Requirements.DOUBLE_NOT_NAN, Requirements.DOUBLE_FINITE);
		assertDoesNotThrow(() -> sf.set(42D));
	}

	@Test
	void testRequirementsNotNegativeRejectsNegativeByte() {
		StableField<Byte> sf = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set((byte) 0x84));
	}

	@Test
	void testRequirementsNotNegativeAcceptsPositiveShort() {
		StableField<Short> sf = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> sf.set((short) 42));
	}

	@Test
	void testRequirementsNotNegativeAcceptsZeroLong() {
		StableField<Long> sf = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> sf.set(0L));
	}

	@Test
	void testRequirementsPositiveRejectsNegativeInt() {
		StableField<Integer> sf = new StableField<>("field", Requirements.POSITIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(-1));
	}

	@Test
	void testRequirementsPositiveRejectsZeroFloat() {
		StableField<Float> sf = new StableField<>("field", Requirements.POSITIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(0F));
	}

	@Test
	void testRequirementsPositiveAcceptsPositiveDouble() {
		StableField<Double> sf = new StableField<>("field", Requirements.POSITIVE);
		assertDoesNotThrow(() -> sf.set(42.5D));
	}

	@Test
	void testRequirementsInRangeByteRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange((byte) 5, (byte) 1));
	}

	@Test
	void testRequirementsInRangeByteRejectsByteBelowRange() {
		StableField<Byte> sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set((byte) 0));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteRangeMin() {
		StableField<Byte> sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> sf.set((byte) 1));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteInRange() {
		StableField<Byte> sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> sf.set((byte) 3));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteRangeMax() {
		StableField<Byte> sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> sf.set((byte) 5));
	}

	@Test
	void testRequirementsInRangeByteRejectsByteAboveRange() {
		StableField<Byte> sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set((byte) 7));
	}

	@Test
	void testRequirementsInRangeShortRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange((short) 5, (short) 1));
	}

	@Test
	void testRequirementsInRangeShortRejectsShortBelowRange() {
		StableField<Short> sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set((short) 0));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortRangeMin() {
		StableField<Short> sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> sf.set((short) 1));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortInRange() {
		StableField<Short> sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> sf.set((short) 3));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortRangeMax() {
		StableField<Short> sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> sf.set((short) 5));
	}

	@Test
	void testRequirementsInRangeShortRejectsShortAboveRange() {
		StableField<Short> sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set((short) 7));
	}

	@Test
	void testRequirementsInRangeIntRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(5, 1));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntBelowRange() {
		StableField<Integer> sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set(0));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMin() {
		StableField<Integer> sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(1));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntInRange() {
		StableField<Integer> sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(3));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMax() {
		StableField<Integer> sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(5));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntAboveRange() {
		StableField<Integer> sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set(7));
	}

	@Test
	void testRequirementsInRangeLongRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(5L, 1L));
	}

	@Test
	void testRequirementsInRangeLongRejectsLongBelowRange() {
		StableField<Long> sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertThrows(IllegalArgumentException.class, () -> sf.set(0L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongRangeMin() {
		StableField<Long> sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> sf.set(1L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongInRange() {
		StableField<Long> sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> sf.set(3L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongRangeMax() {
		StableField<Long> sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> sf.set(5L));
	}

	@Test
	void testRequirementsInRangeLongRejectsLongAboveRange() {
		StableField<Long> sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertThrows(IllegalArgumentException.class, () -> sf.set(7L));
	}

	@Test
	void testRequirementsInRangeFloatRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(1.75F, 1.25F));
	}

	@Test
	void testRequirementsInRangeFloatRejectsFloatBelowRange() {
		StableField<Float> sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertThrows(IllegalArgumentException.class, () -> sf.set(1F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatRangeMin() {
		StableField<Float> sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> sf.set(1.25F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatInRange() {
		StableField<Float> sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> sf.set(1.5F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatRangeMax() {
		StableField<Float> sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> sf.set(1.75F));
	}

	@Test
	void testRequirementsInRangeFloatRejectsFloatAboveRange() {
		StableField<Float> sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertThrows(IllegalArgumentException.class, () -> sf.set(2F));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(1.75, 1.25));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsDoubleBelowRange() {
		StableField<Double> sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertThrows(IllegalArgumentException.class, () -> sf.set(1D));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleRangeMin() {
		StableField<Double> sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> sf.set(1.25));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleInRange() {
		StableField<Double> sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> sf.set(1.5));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleRangeMax() {
		StableField<Double> sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> sf.set(1.75));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsDoubleAboveRange() {
		StableField<Double> sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertThrows(IllegalArgumentException.class, () -> sf.set(2D));
	}
}
