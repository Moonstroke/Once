package io.github.moonstroke.once;

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
}
