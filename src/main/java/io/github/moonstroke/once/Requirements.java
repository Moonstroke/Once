package io.github.moonstroke.once;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Defines a set of common requirements, for various types.
 */
public class Requirements {

	private Requirements() {
		/* Static-only utilities class */
	}


	/**
	 * A special requirement instance that alters the default behavior of class {@link StableField} by allowing
	 * setting the field to {@code null}.
	 */
	public static final Requirement<Object> ALLOW_NULL = o -> {
		/* Nothing to do; it is the presence of the object itself that matters */
	};

	/**
	 * Require a string to be non-empty.
	 */
	public static final Requirement<String> STRING_NOT_EMPTY = Requirement.fromPredicate(str -> !str.isEmpty(),
	                                                                                     "value cannot be an empty string");

	/**
	 * Require a string to contain at least one non-whitespace character.
	 */
	public static final Requirement<String> STRING_NOT_BLANK = Requirement.fromPredicate(str -> !str.isBlank(),
	                                                                                     "value cannot be a blank string");


	/**
	 * Require that a string match the given pattern.
	 *
	 * @param pattern The regular expression pattern to match
	 *
	 * @return A requirement instance accepting only strings matching the given regular expression pattern
	 *
	 * @throws NullPointerException if pattern is {@code null}
	 */
	public static Requirement<CharSequence> matches(Pattern pattern) {
		return Requirement.fromPredicate(s -> pattern.matcher(s).matches(), "value must match the pattern " + pattern);
	}

	/**
	 * Require that a string match the given pattern.
	 *
	 * @param regex The regular expression to match
	 *
	 * @return A requirement instance accepting only strings matching the given regular expression
	 *
	 * @throws NullPointerException   if regex is {@code null}
	 * @throws PatternSyntaxException if regex does not represent a valid regular expression
	 */
	public static Requirement<CharSequence> matches(String regex) {
		Objects.requireNonNull(regex);
		Pattern pattern = Pattern.compile(regex);
		return Requirement.fromPredicate(s -> pattern.matcher(s).matches(), "value must match the pattern " + regex);
	}


	/**
	 * Require that a list be non-empty.
	 */
	public static final Requirement<List<?>> LIST_NOT_EMPTY = Requirement.fromPredicate(list -> !list.isEmpty(),
	                                                                                    "value cannot be an empty list");

	/**
	 * Require that a set be not empty.
	 */
	public static final Requirement<Set<?>> SET_NOT_EMPTY = Requirement.fromPredicate(set -> !set.isEmpty(),
	                                                                                  "value cannot be an empty set");

	/**
	 * Require a collection to be not empty.
	 */
	public static final Requirement<Collection<?>> COLLECTION_NOT_EMPTY = Requirement.fromPredicate(coll -> !coll.isEmpty(),
	                                                                                                "value cannot be an empty collection");

	/**
	 * Require that a map be not empty.
	 */
	public static final Requirement<Map<?,
	                                    ?>> MAP_NOT_EMPTY = Requirement.fromPredicate(map -> !map.isEmpty(),
	                                                                                  "value cannot be an empty map");

	/**
	 * Require that a character be nonzero.
	 */
	public static final Requirement<Character> CHAR_NOT_ZERO = Requirement.fromPredicate(c -> c != '\0',
	                                                                                     "value cannot be zero");

	/**
	 * Require that a number be nonzero.
	 */
	public static final Requirement<Number> NOT_ZERO = Requirement.fromPredicate(n -> n.doubleValue() != 0D,
	                                                                             "value cannot be zero");

	/**
	 * Require that a floating-point number be an actual number (not NaN).
	 */
	public static final Requirement<Float> FLOAT_NOT_NAN = Requirement.fromPredicate(f -> !Float.isNaN(f),
	                                                                                 "value cannot be NaN");

	/**
	 * Require that a double-precision floating-point number be an actual number (not NaN).
	 */
	public static final Requirement<Double> DOUBLE_NOT_NAN = Requirement.fromPredicate(d -> !Double.isNaN(d),
	                                                                                   "value cannot be NaN");

	/**
	 * Require that a floating-point number be a finite number.
	 */
	public static final Requirement<Float> FLOAT_FINITE = Requirement.fromPredicate(Float::isFinite,
	                                                                                "value must not be an infinite");

	/**
	 * Require that a double-precision floating-point number be a finite number.
	 */
	public static final Requirement<Double> DOUBLE_FINITE = Requirement.fromPredicate(Double::isFinite,
	                                                                                  "value must not be an infinite");

	/**
	 * Require that a number be non-negative (i.e. positive or zero).
	 */
	public static final Requirement<Number> NOT_NEGATIVE = Requirement.fromPredicate(n -> n.doubleValue() >= 0,
	                                                                                 "value must not be negative");

	/**
	 * Require that a number be positive (not negative, nor zero). This requirement is a shortcut for specifying both
	 * {@link #NOT_NEGATIVE} and {@link #NOT_ZERO}.
	 */
	public static final Requirement<Number> POSITIVE = Requirement.fromPredicate(n -> n.doubleValue() > 0,
	                                                                             "value must be positive");


	/**
	 * Require that a byte value be in a specified range.
	 *
	 * @param min The lower boundary of the range
	 * @param max The upper boundary of the range
	 *
	 * @return A requirement instance accepting only bytes inside the range described by the given boundaries, included
	 *
	 * @throws IllegalArgumentException if min is greater than max
	 */
	public static Requirement<Byte> inRange(byte min, byte max) {
		if (min > max) {
			throw new IllegalArgumentException("invalid range: " + min + " > " + max);
		}
		return Requirement.fromPredicate(b -> min <= b && b <= max,
		                                 "value must be between " + min + " and " + max + " inclusive");
	}


	/**
	 * Require that a short integer be in a specified range.
	 *
	 * @param min The lower boundary of the range
	 * @param max The upper boundary of the range
	 *
	 * @return A requirement instance accepting only shorts inside the range described by the given boundaries, included
	 *
	 * @throws IllegalArgumentException if min is greater than max
	 */
	public static Requirement<Short> inRange(short min, short max) {
		if (min > max) {
			throw new IllegalArgumentException("invalid range: " + min + " > " + max);
		}
		return Requirement.fromPredicate(s -> min <= s && s <= max,
		                                 "value must be between " + min + " and " + max + " inclusive");
	}


	/**
	 * Require that an integral number be in a specified range.
	 *
	 * @param min The lower boundary of the range
	 * @param max The upper boundary of the range
	 *
	 * @return A requirement instance accepting only ints inside the range described by the given boundaries, included
	 *
	 * @throws IllegalArgumentException if min is greater than max
	 */
	public static Requirement<Integer> inRange(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("invalid range: " + min + " > " + max);
		}
		return Requirement.fromPredicate(i -> min <= i && i <= max,
		                                 "value must be between " + min + " and " + max + " inclusive");
	}


	/**
	 * Require that a long integer be in a specified range.
	 *
	 * @param min The lower boundary of the range
	 * @param max The upper boundary of the range
	 *
	 * @return A requirement instance accepting only longs inside the range described by the given boundaries, included
	 *
	 * @throws IllegalArgumentException if min is greater than max
	 */
	public static Requirement<Long> inRange(long min, long max) {
		if (min > max) {
			throw new IllegalArgumentException("invalid range: " + min + " > " + max);
		}
		return Requirement.fromPredicate(l -> min <= l && l <= max,
		                                 "value must be between " + min + " and " + max + " inclusive");
	}

	/**
	 * Require that a floating-point number be in a specified range.
	 *
	 * @param min The lower boundary of the range
	 * @param max The upper boundary of the range
	 *
	 * @return A requirement instance accepting only floats inside the range described by the given boundaries, included
	 *
	 * @throws IllegalArgumentException if min is greater than max
	 */
	public static Requirement<Float> inRange(float min, float max) {
		if (min > max) {
			throw new IllegalArgumentException("invalid range: " + min + " > " + max);
		}
		return Requirement.fromPredicate(f -> min <= f && f <= max,
		                                 "value must be between " + min + " and " + max + " inclusive");
	}

	/**
	 * Require that a double-precision floating-point number be in a specified range.
	 *
	 * @param min The lower boundary of the range
	 * @param max The upper boundary of the range
	 *
	 * @return A requirement instance accepting only doubles inside the range described by the given boundaries,
	 *         included
	 *
	 * @throws IllegalArgumentException if min is greater than max
	 */
	public static Requirement<Double> inRange(double min, double max) {
		if (min > max) {
			throw new IllegalArgumentException("invalid range: " + min + " > " + max);
		}
		return Requirement.fromPredicate(d -> min <= d && d <= max,
		                                 "value must be between " + min + " and " + max + " inclusive");
	}
}
