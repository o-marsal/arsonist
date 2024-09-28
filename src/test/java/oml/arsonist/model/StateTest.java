package oml.arsonist.model;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class StateTest {

	@Test
	void testToString() {
        Assertions.assertEquals("STOPPED", State.STOPPED.toString());
        Assertions.assertEquals("RUNNING", State.RUNNING.toString());
	}

	@Test
	void testFromString() {
        Assertions.assertEquals(State.STOPPED, State.fromString("STOPPED"));
        Assertions.assertEquals(State.RUNNING, State.fromString("RUNNING"));
	}

	@Test
	void testFromStringNotFound() {
        Assertions.assertThrows(NoSuchElementException.class, ()->{
            State.fromString("UNDEFINED");
        });
        Assertions.assertThrows(NoSuchElementException.class, ()->{
            State.fromString("");
        });
        Assertions.assertThrows(NoSuchElementException.class, ()->{
            State.fromString(null);
        });
	}

}
