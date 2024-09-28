package oml.arsonist.model;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;
import com.google.gson.GsonBuilder;

/**
 * Model is the main application model.
 * This object is immutable (no setters) to ensure the consistency (thread-safe) like a database transaction.
 * You have to use the Builder to create or update something, as a new model.
 * This means this is NOT a singleton, the instance may change.
 * Use ArsonistModel.getModel() to get the last version of the Model.
 */
public class Model implements JsonSerializer<Model> {

    private State state;
    private int width;
    private int height;
    private long time; // timestamp java (ms)
    private int step;
    private Forest forest;


    //-------------------------------------------------------------------------
    // Construtor & init
    //-------------------------------------------------------------------------

    private Model() {
        state = State.STOPPED;
        width = 0;
        height = 0;
        time = 0;
        step = 0;
        forest = null;
    }


    //-------------------------------------------------------------------------
    // Setters & Getters
    //-------------------------------------------------------------------------

    public State getState() {
        return state;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getTime() {
        return time;
    }

    public int getStep() {
        return step;
    }

    /**
     * Get the ForestState at (x,y).
     * If the cell is outside the grid, returns null.
     * @param x - the x coornidate
     * @param y - the y coornidate
     * @return the ForestState, or null
     */
    public ForestState getForestStateAt(int x, int y) {
        return forest.getForestStateAt(x, y);
    }

    /**
     * @return true if at least one tree is burning.
     */
    public boolean remainsABurningTree() {
        return forest.remainsABurningTree();
    }


    //-------------------------------------------------------------------------
    // JSON
    // use a custom serializer to manage the grid array as String
    //-------------------------------------------------------------------------

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Model.class, this);
        return gsonBuilder.create().toJson(this);
    }

    public JsonElement serialize(Model model, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("state", state.toString());
        obj.addProperty("width", width);
        obj.addProperty("height", height);
        obj.addProperty("time", time);
        obj.addProperty("step", step);
        obj.addProperty("grid", forest.toString());
        return obj;
    }


    //-------------------------------------------------------------------------
    // Builder
    //-------------------------------------------------------------------------

    public Builder duplicate() {
        Model duplicated = new Model();
        duplicated.state = state;
        duplicated.width = width;
        duplicated.height = height;
        duplicated.time = time;
        duplicated.step = step;
        duplicated.forest = forest.duplicate();
        return new Builder(duplicated);
    }

    /**
     * A Builder of a Model.
     */
    public static class Builder {

        private Model model;

        // Constructors

        public Builder() {
            model = new Model();
            // keep time 0 for the default empty (not initialized) builder
        }

        public Builder(int width, int height) {
            model = new Model();
            model.width = width;
            model.height = height;
            model.forest = new Forest(width, height);
            updateTime();
        }

        private Builder(Model model) {
            this.model = model;
            updateTime();
        }

        // Getters & Setters

        public State getState() {
            return model.state;
        }

        public void setState(State state) {
            model.state = state;
        }
    
        public int getWidth() {
            return model.width;
        }

        public void setWidth(int width) {
            model.width = width;
        }

        public int getHeight() {
            return model.height;
        }

        public void setHeight(int height) {
            model.height = height;
        }

        public long getTime() {
            return model.time;
        }

        public void setTime(long time) {
            model.time = time;
        }

        public void updateTime() {
            model.time = System.currentTimeMillis();
        }

        public int getStep() {
            return model.step;
        }

        public void resetStep() {
            model.step = 0;
        }

        public void incrementStep() {
            model.step++;
        }

        public void setForestStateAt(int x, int y, ForestState forestState) {
            model.forest.setForestStateAt(x, y, forestState);
        }

        public boolean remainsABurningTree() {
            return model.forest.remainsABurningTree();
        }
    
        // Build

        public Model build() {
            return model;
        }
    
    }

}
