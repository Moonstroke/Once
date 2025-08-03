package io.github.moonstroke.once;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Defines a set of common requirements, for various types.
 */
public class Requirements {

	private Requirements() {
		/* Static-only utilities class */
	}


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
}
