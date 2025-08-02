package io.github.moonstroke.once;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Defines a set of common requirements, for various types.
 */
public class Requirements {

	private Requirements() {
		/* Static-only utilities class */
	}


	public static final Requirement<String> STRING_NOT_EMPTY = Requirement.fromPredicate(str -> !str.isEmpty(),
	                                                                                     "value cannot be an empty string");

	public static final Requirement<String> STRING_NOT_BLANK = Requirement.fromPredicate(str -> !str.isBlank(),
	                                                                                     "value cannot be a blank string");

	public static final Requirement<List<?>> LIST_NOT_EMPTY = Requirement.fromPredicate(list -> !list.isEmpty(),
	                                                                                    "value cannot be an empty list");

	public static final Requirement<Set<?>> SET_NOT_EMPTY = Requirement.fromPredicate(set -> !set.isEmpty(),
	                                                                                  "value cannot be an empty set");

	public static final Requirement<Collection<?>> COLLECTION_NOT_EMPTY = Requirement.fromPredicate(coll -> !coll.isEmpty(),
	                                                                                                "value cannot be an empty collection");
}
