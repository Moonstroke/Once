package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.StableField;
import io.github.moonstroke.once.Requirements;

class RequirementsTest {

	@Test
	void testRequirementsAllowNullAcceptsNullObject() {
		var sf = new StableField<>("optional field", Requirements.ALLOW_NULL);
		assertDoesNotThrow(() -> sf.set(null));
	}

	@Test
	void testRequirementsAllowNullDoesNotAllowSetTwice() {
		var sf = new StableField<>("optional field", Requirements.ALLOW_NULL);
		sf.set(null);
		assertThrows(IllegalStateException.class, () -> sf.set(null));
	}

	@Test
	void testRequirementsAllowNullDoesNotAllowSetZeroTimesBeforeGet() {
		var sf = new StableField<>("optional field", Requirements.ALLOW_NULL);
		assertThrows(NoSuchElementException.class, sf::get);
	}

	@Test
	void testRequirementsStringNotEmptyRejectsEmptyString() {
		var sf = new StableField<>("field", Requirements.STRING_NOT_EMPTY);
		assertThrows(IllegalArgumentException.class, () -> sf.set(""));
	}

	@Test
	void testRequirementsStringNotEmptyAcceptsNonEmptyString() {
		var sf = new StableField<>("field", Requirements.STRING_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set("not empty"));
	}

	@Test
	void testRequirementsStringNotBlankRejectsBlankString() {
		var sf = new StableField<>("field", Requirements.STRING_NOT_BLANK);
		assertThrows(IllegalArgumentException.class, () -> sf.set(" \t\n\f\r"));
	}

	@Test
	void testRequirementsStringNotBlankAcceptsNonBlankString() {
		var sf = new StableField<>("field", Requirements.STRING_NOT_BLANK);
		assertDoesNotThrow(() -> sf.set("not blank"));
	}

	@Test
	void testRequirementsMatchesPatternRejectsNull() {
		assertThrows(NullPointerException.class, () -> Requirements.matches((Pattern) null));
	}

	@Test
	void testRequirementsMatchesPatternAcceptsMatchingString() {
		var sf = new StableField<>("field", Requirements.matches(Pattern.compile("foo(?:bar)?baz")));
		assertDoesNotThrow(() -> sf.set("foobaz"));
	}

	@Test
	void testRequirementsMatchesPatternRejectsNonMatchingString() {
		var sf = new StableField<>("field", Requirements.matches(Pattern.compile("foo(?:bar)?baz")));
		assertThrows(IllegalArgumentException.class, () -> sf.set("foobar"));
	}

	@Test
	void testRequirementsMatchesStringRejectsNull() {
		assertThrows(NullPointerException.class, () -> Requirements.matches((String) null));
	}

	@Test
	void testRequirementsMatchesStringAcceptsMatchingString() {
		var sf = new StableField<>("field", Requirements.matches(Pattern.compile("a+b")));
		assertDoesNotThrow(() -> sf.set("aab"));
	}

	@Test
	void testRequirementsMatchesStringRejectsNonMatchingString() {
		var sf = new StableField<>("field", Requirements.matches("a+b"));
		assertThrows(IllegalArgumentException.class, () -> sf.set("aaa"));
	}

	@Test
	void testRequirementsListNotEmptyRejectsEmptyList() {
		var sf = new StableField<>("field", Requirements.LIST_NOT_EMPTY);
		List<Object> emptyList = Collections.emptyList();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptyList));
	}

	@Test
	void testRequirementsListNotEmptyAcceptsNonEmptyList() {
		var sf = new StableField<>("field", Requirements.LIST_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsSetNotEmptyRejectsEmptySet() {
		var sf = new StableField<>("field", Requirements.SET_NOT_EMPTY);
		Set<Object> emptySet = Collections.emptySet();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptySet));
	}

	@Test
	void testRequirementsSetNotEmptyAcceptsNonEmptySet() {
		var sf = new StableField<>("field", Requirements.SET_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptyList() {
		var sf = new StableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		List<Object> emptyCollection = Collections.emptyList();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptyCollection));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptyList() {
		var sf = new StableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singletonList(new Object())));
	}

	@Test
	void testRequirementsCollectionNotEmptyRejectsEmptySet() {
		var sf = new StableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		Set<Object> emptyCollection = Collections.emptySet();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptyCollection));
	}

	@Test
	void testRequirementsCollectionNotEmptyAcceptsNonEmptySet() {
		var sf = new StableField<>("field", Requirements.COLLECTION_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singleton(new Object())));
	}

	@Test
	void testRequirementsMapNotEmptyRejectsEmptyMap() {
		var sf = new StableField<>("field", Requirements.MAP_NOT_EMPTY);
		Map<Object, Object> emptyMap = Collections.emptyMap();
		assertThrows(IllegalArgumentException.class, () -> sf.set(emptyMap));
	}

	@Test
	void testRequirementsMapNotEmptyAcceptsNonEmptyMap() {
		var sf = new StableField<>("field", Requirements.MAP_NOT_EMPTY);
		assertDoesNotThrow(() -> sf.set(Collections.singletonMap(new Object(), new Object())));
	}

	@Test
	void testRequirementsCharNotNulRejectsNulChar() {
		var sf = new StableField<>("field", Requirements.CHAR_NOT_NUL);
		assertThrows(IllegalArgumentException.class, () -> sf.set('\0'));
	}

	@Test
	void testRequirementsCharNotNulAcceptsNonNulChar() {
		var sf = new StableField<>("field", Requirements.CHAR_NOT_NUL);
		assertDoesNotThrow(() -> sf.set('*'));
	}

	@Test
	void testRequirementsNotZeroRejectsZeroInt() {
		var sf = new StableField<>("field", Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> sf.set(0));
	}

	@Test
	void testRequirementsNotZeroRejectsZeroDouble() {
		var sf = new StableField<>("field", Requirements.NOT_ZERO);
		assertThrows(IllegalArgumentException.class, () -> sf.set(0D));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZeroInt() {
		var sf = new StableField<>("field", Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> sf.set(42));
	}

	@Test
	void testRequirementsNotZeroAcceptsNonZeroDouble() {
		var sf = new StableField<>("field", Requirements.NOT_ZERO);
		assertDoesNotThrow(() -> sf.set(42D));
	}

	@Test
	void testRequirementsFloatNotNanRejectsFloatNan() {
		var sf = new StableField<>("field", Requirements.FLOAT_NOT_NAN);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Float.NaN));
	}

	@Test
	void testRequirementsFloatFiniteRejectsNegInf() {
		var sf = new StableField<>("field", Requirements.FLOAT_FINITE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Float.NEGATIVE_INFINITY));
	}

	@Test
	void testRequirementsFloatFiniteRejectsPosInf() {
		var sf = new StableField<>("field", Requirements.FLOAT_FINITE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Float.POSITIVE_INFINITY));
	}

	@Test
	void testRequirementsFloatNotNanFiniteAcceptRegularFloat() {
		var sf = new StableField<>("field", Requirements.FLOAT_NOT_NAN, Requirements.FLOAT_FINITE);
		assertDoesNotThrow(() -> sf.set(42F));
	}

	@Test
	void testRequirementsDoubleNotNanRejectsDoubleNan() {
		var sf = new StableField<>("field", Requirements.DOUBLE_NOT_NAN);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Double.NaN));
	}

	@Test
	void testRequirementsDoubleFiniteRejectsNegInf() {
		var sf = new StableField<>("field", Requirements.DOUBLE_FINITE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Double.NEGATIVE_INFINITY));
	}

	@Test
	void testRequirementsDoubleFiniteRejectsPosInf() {
		var sf = new StableField<>("field", Requirements.DOUBLE_FINITE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(Double.POSITIVE_INFINITY));
	}

	@Test
	void testRequirementsDoubleNotNanFiniteAcceptRegularDouble() {
		var sf = new StableField<>("field", Requirements.DOUBLE_NOT_NAN, Requirements.DOUBLE_FINITE);
		assertDoesNotThrow(() -> sf.set(42D));
	}

	@Test
	void testRequirementsNotNegativeRejectsNegativeByte() {
		var sf = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set((byte) 0x84));
	}

	@Test
	void testRequirementsNotNegativeAcceptsPositiveShort() {
		var sf = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> sf.set((short) 42));
	}

	@Test
	void testRequirementsNotNegativeAcceptsZeroLong() {
		var sf = new StableField<>("field", Requirements.NOT_NEGATIVE);
		assertDoesNotThrow(() -> sf.set(0L));
	}

	@Test
	void testRequirementsPositiveRejectsNegativeInt() {
		var sf = new StableField<>("field", Requirements.POSITIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(-1));
	}

	@Test
	void testRequirementsPositiveRejectsZeroFloat() {
		var sf = new StableField<>("field", Requirements.POSITIVE);
		assertThrows(IllegalArgumentException.class, () -> sf.set(0F));
	}

	@Test
	void testRequirementsPositiveAcceptsPositiveDouble() {
		var sf = new StableField<>("field", Requirements.POSITIVE);
		assertDoesNotThrow(() -> sf.set(42.5D));
	}

	@Test
	void testRequirementsInRangeByteRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange((byte) 5, (byte) 1));
	}

	@Test
	void testRequirementsInRangeByteRejectsByteBelowRange() {
		var sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set((byte) 0));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteRangeMin() {
		var sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> sf.set((byte) 1));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteInRange() {
		var sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> sf.set((byte) 3));
	}

	@Test
	void testRequirementsInRangeByteAcceptsByteRangeMax() {
		var sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertDoesNotThrow(() -> sf.set((byte) 5));
	}

	@Test
	void testRequirementsInRangeByteRejectsByteAboveRange() {
		var sf = new StableField<>("field", Requirements.inRange((byte) 1, (byte) 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set((byte) 7));
	}

	@Test
	void testRequirementsInRangeShortRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange((short) 5, (short) 1));
	}

	@Test
	void testRequirementsInRangeShortRejectsShortBelowRange() {
		var sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set((short) 0));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortRangeMin() {
		var sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> sf.set((short) 1));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortInRange() {
		var sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> sf.set((short) 3));
	}

	@Test
	void testRequirementsInRangeShortAcceptsShortRangeMax() {
		var sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertDoesNotThrow(() -> sf.set((short) 5));
	}

	@Test
	void testRequirementsInRangeShortRejectsShortAboveRange() {
		var sf = new StableField<>("field", Requirements.inRange((short) 1, (short) 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set((short) 7));
	}

	@Test
	void testRequirementsInRangeIntRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(5, 1));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntBelowRange() {
		var sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set(0));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMin() {
		var sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(1));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntInRange() {
		var sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(3));
	}

	@Test
	void testRequirementsInRangeIntAcceptsIntRangeMax() {
		var sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertDoesNotThrow(() -> sf.set(5));
	}

	@Test
	void testRequirementsInRangeIntRejectsIntAboveRange() {
		var sf = new StableField<>("field", Requirements.inRange(1, 5));
		assertThrows(IllegalArgumentException.class, () -> sf.set(7));
	}

	@Test
	void testRequirementsInRangeLongRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(5L, 1L));
	}

	@Test
	void testRequirementsInRangeLongRejectsLongBelowRange() {
		var sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertThrows(IllegalArgumentException.class, () -> sf.set(0L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongRangeMin() {
		var sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> sf.set(1L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongInRange() {
		var sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> sf.set(3L));
	}

	@Test
	void testRequirementsInRangeLongAcceptsLongRangeMax() {
		var sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertDoesNotThrow(() -> sf.set(5L));
	}

	@Test
	void testRequirementsInRangeLongRejectsLongAboveRange() {
		var sf = new StableField<>("field", Requirements.inRange(1L, 5L));
		assertThrows(IllegalArgumentException.class, () -> sf.set(7L));
	}

	@Test
	void testRequirementsInRangeFloatRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(1.75F, 1.25F));
	}

	@Test
	void testRequirementsInRangeFloatRejectsFloatBelowRange() {
		var sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertThrows(IllegalArgumentException.class, () -> sf.set(1F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatRangeMin() {
		var sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> sf.set(1.25F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatInRange() {
		var sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> sf.set(1.5F));
	}

	@Test
	void testRequirementsInRangeFloatAcceptsFloatRangeMax() {
		var sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertDoesNotThrow(() -> sf.set(1.75F));
	}

	@Test
	void testRequirementsInRangeFloatRejectsFloatAboveRange() {
		var sf = new StableField<>("field", Requirements.inRange(1.25F, 1.75F));
		assertThrows(IllegalArgumentException.class, () -> sf.set(2F));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsRangeMinGreaterThanMax() {
		assertThrows(IllegalArgumentException.class, () -> Requirements.inRange(1.75, 1.25));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsDoubleBelowRange() {
		var sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertThrows(IllegalArgumentException.class, () -> sf.set(1D));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleRangeMin() {
		var sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> sf.set(1.25));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleInRange() {
		var sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> sf.set(1.5));
	}

	@Test
	void testRequirementsInRangeDoubleAcceptsDoubleRangeMax() {
		var sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertDoesNotThrow(() -> sf.set(1.75));
	}

	@Test
	void testRequirementsInRangeDoubleRejectsDoubleAboveRange() {
		var sf = new StableField<>("field", Requirements.inRange(1.25, 1.75));
		assertThrows(IllegalArgumentException.class, () -> sf.set(2D));
	}
}
