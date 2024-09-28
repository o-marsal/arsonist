package oml.arsonist.model;

import java.util.NoSuchElementException;

/**
 * State of a tree.
 */
public enum ForestState {

    /**
     * The tree is alive: not nurning, not burned
     */
    ALIVE("T"),

    /**
     * The tree is burning
     */
    BURNING("*"),

    /**
     * The tree is burned
     */
    BURNED("X");


    //-------------------------------------------------------------------------
    // Enum class
    //-------------------------------------------------------------------------

    private String code;

    private ForestState(String code) {
        this.code = code;
    }

    /**
     * @return the String representation of this ForestState.
     */
    public String toString() {
        return code;
    }

    /**
     * Get the State from a given String.
     * Throw NoSuchElementException if the String does not correspond to a ForestState.
     * @Param str String - the String forestState.
     * @Return the ForestState
     * @Throws NoSuchElementException if the String does not correspond to a ForestState.
     */
    public static ForestState fromString(String str) {
        for (ForestState forestState : ForestState.values()) { 
            if (forestState.code.equals(str)) {
                return forestState;
            }
        }
        throw new NoSuchElementException("Illegal ForestState " + str);
    }

}
