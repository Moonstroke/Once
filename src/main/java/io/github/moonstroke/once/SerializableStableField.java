package io.github.moonstroke.once;

import java.io.Serializable;

public class SerializableStableField<T extends Serializable> extends StableField<T> implements Serializable {

	private static final long serialVersionUID = -7218396552684443807L;


	@SafeVarargs
	public SerializableStableField(String name, Requirement<? super T>... requirements) {
		super(name, requirements);
	}
}
