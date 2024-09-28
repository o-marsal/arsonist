package oml.arsonist.model;

import java.util.NoSuchElementException;

/**
 * State of the simulation.
 */
public enum State {

    /**
     * State Simulation is Stopped.
     */
    STOPPED("STOPPED"),

    /**
     * State Simulation is Running.
     */
    RUNNING("RUNNING");


    //-------------------------------------------------------------------------
    // Enum class
    //-------------------------------------------------------------------------

    private String code;

    private State(String code) {
        this.code = code;
    }

    /**
     * @return the String representation of this State.
     */
    public String toString() {
        return code;
    }

    /**
     * Get the State from a given String.
     * Throw NoSuchElementException if the String does not correspond to a State.
     * @Param str String - the String State.
     * @Return the State
     * @Throws NoSuchElementException if the String does not correspond to a State.
     */
    public static State fromString(String str) {
        for (State state : State.values()) { 
            if (state.code.equals(str)) {
                return state;
            }
        }
        throw new NoSuchElementException("Illegal state " + str);
    }

}
