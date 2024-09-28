package oml.arsonist.model;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class ForestStateTest {

	@Test
	void testToString() {
        Assertions.assertEquals("T", ForestState.ALIVE.toString());
        Assertions.assertEquals("*", ForestState.BURNING.toString());
        Assertions.assertEquals("X", ForestState.BURNED.toString());
	}

	@Test
	void testFromString() {
        Assertions.assertEquals(ForestState.ALIVE, ForestState.fromString("T"));
        Assertions.assertEquals(ForestState.BURNING, ForestState.fromString("*"));
        Assertions.assertEquals(ForestState.BURNED, ForestState.fromString("X"));
	}

	@Test
	void testFromStringNotFound() {
        Assertions.assertThrows(NoSuchElementException.class, ()->{
            ForestState.fromString("UNDEFINED");
        });
        Assertions.assertThrows(NoSuchElementException.class, ()->{
            ForestState.fromString("");
        });
        Assertions.assertThrows(NoSuchElementException.class, ()->{
            ForestState.fromString(null);
        });
	}

}
