package io.github.moonstroke.once.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

import io.github.moonstroke.once.SerializableStableField;
import io.github.moonstroke.once.StableField;

public class SerializableTest {

	@Test
	void testSerializingNotSerializableObjFails() {
		var nssf = new StableField<>("not serializable field");
		try (var oos = new ObjectOutputStream(OutputStream.nullOutputStream())) {
			assertThrows(NotSerializableException.class, () -> oos.writeObject(nssf));
		} catch (IOException e) {
			fail(e);
		}
	}

	@Test
	void testSerializingSerializableObjSucceeds() {
		/* Just any standard serializable class will do */
		var ssf = new SerializableStableField<String>("serializable field");
		try (var oos = new ObjectOutputStream(OutputStream.nullOutputStream())) {
			assertDoesNotThrow(() -> oos.writeObject(ssf));
		} catch (IOException e) {
			fail(e);
		}
	}
}
