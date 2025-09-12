package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.Requirement;
import io.github.moonstroke.once.SerializableStableField;
import io.github.moonstroke.once.StableField;

public class SerializableTest {

	@Test
	void testSerializingNotSerializableFieldUnsetFails() {
		var nssf = new StableField<>("not serializable field");
		try (var oos = new ObjectOutputStream(OutputStream.nullOutputStream())) {
			assertThrows(NotSerializableException.class, () -> oos.writeObject(nssf));
		} catch (IOException e) {
			fail(e);
		}
	}

	@Test
	void testSerializingNotSerializableFieldSetFails() {
		var nssf = new StableField<>("not serializable field");
		nssf.set(new Object());
		try (var oos = new ObjectOutputStream(OutputStream.nullOutputStream())) {
			assertThrows(NotSerializableException.class, () -> oos.writeObject(nssf));
		} catch (IOException e) {
			fail(e);
		}
	}

	@Test
	void testSerializingSerializableFieldUnsetSucceeds() {
		/* Just any standard serializable class will do */
		var ssf = new SerializableStableField<String>("serializable field");
		try (var oos = new ObjectOutputStream(OutputStream.nullOutputStream())) {
			assertDoesNotThrow(() -> oos.writeObject(ssf));
		} catch (IOException e) {
			fail(e);
		}
	}

	@Test
	void testSerializingSerializableFieldSetSucceeds() {
		/* Just any standard serializable class will do */
		var ssf = new SerializableStableField<String>("serializable field");
		ssf.set("serializable value");
		try (var oos = new ObjectOutputStream(OutputStream.nullOutputStream())) {
			assertDoesNotThrow(() -> oos.writeObject(ssf));
		} catch (IOException e) {
			fail(e);
		}
	}


	private static class NotSerializableBase {

		static final Requirement<NotSerializableBase> REQ = o -> {
		};
	}

	private static class SerializableChild extends NotSerializableBase implements Serializable {

		private static final long serialVersionUID = -5030636312664271004L;

		static final Requirement<SerializableChild> REQ = o -> {
		};
	}


	@Test
	void testSerializingSerializableObjParentNotSerializableNoRequirementsSucceeds() {
		var ssf = new SerializableStableField<>("serializable field");
		try (var oos = new ObjectOutputStream(OutputStream.nullOutputStream())) {
			assertDoesNotThrow(() -> oos.writeObject(ssf));
		} catch (IOException e) {
			fail(e);
		}
	}

	@Test
	void testSerializingSerializableObjParentNotSerializableWithRequirementsSucceeds() {
		var ssf = new SerializableStableField<>("serializable field", NotSerializableBase.REQ, SerializableChild.REQ);
		try (var oos = new ObjectOutputStream(OutputStream.nullOutputStream())) {
			assertDoesNotThrow(() -> oos.writeObject(ssf));
		} catch (IOException e) {
			fail(e);
		}
	}
}
