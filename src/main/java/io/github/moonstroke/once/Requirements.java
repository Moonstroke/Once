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
}
