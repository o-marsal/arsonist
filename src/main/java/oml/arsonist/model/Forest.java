package oml.arsonist.model;

/**
 * Forest is the 2D grid of trees.
 */
public class Forest {

    private int width;
    private int height;
    private ForestState[][] grid;


    //-------------------------------------------------------------------------
    // Constructors
    //-------------------------------------------------------------------------

    public Forest(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new ForestState[width][height];
        for (int x=0 ; x<width ; x++) {
            for (int y=0 ; y<height ; y++) {
                grid[x][y] = ForestState.ALIVE;
            }
        }
    }

    private Forest(int width, int height, ForestState[][] gridToCopy) {
        this.width = width;
        this.height = height;
        grid = new ForestState[width][height];
        for (int x=0 ; x<width ; x++) {
            for (int y=0 ; y<height ; y++) {
                grid[x][y] = gridToCopy[x][y];
            }
        }
    }

    public Forest duplicate() {
        return new Forest(width, height, grid);
    }


    //-------------------------------------------------------------------------
    // Setters & Getters
    //-------------------------------------------------------------------------

    /**
     * Get the ForestState at (x,y).
     * If the cell is outside the grid, returns null.
     * @param x - the x coornidate
     * @param y - the y coornidate
     * @return the ForestState, or null
     */
    public ForestState getForestStateAt(int x, int y) {
        try {
            return grid[x][y];
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void setForestStateAt(int x, int y, ForestState forestState) {
        grid[x][y] = forestState;
    }

    public boolean remainsABurningTree() {
        for (int x=0 ; x<width ; x++) {
            for (int y=0 ; y<height ; y++) {
                if (grid[x][y] == ForestState.BURNING) return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int y=0 ; y<height ; y++) {
            for (int x=0 ; x<width ; x++) {
                str.append(grid[x][y].toString());
            }
        }
        return str.toString();
    }

}
