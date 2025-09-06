package io.github.moonstroke.once.test;

import io.github.moonstroke.once.Requirement;
import io.github.moonstroke.once.SharedStableField;
import io.github.moonstroke.once.StableField;

class RequirementsSharedTest extends RequirementsTest {

	protected <T> StableField<T> getTestInstance(String name, Requirement<? super T> requirement) {
		return new SharedStableField<T>(name, requirement);
	}

	protected <T> StableField<T> getTestInstance(String name, Requirement<? super T> requirement1,
	                                             Requirement<? super T> requirement2) {
		return new SharedStableField<T>(name, requirement1, requirement2);
	}
}
