package oml.arsonist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import oml.arsonist.model.Model;

/**
 * Application Model, aloow to acces to the model.
 * The model is read only, the instance may change.
 * This class is a Spring singleton Component, a wrapper to access to the last version of the model.
 * This class is thread-safe.
 */
@Component
public class ArsonistModel {

	@Autowired
	private ArsonistSettings settings;

    private Model model;
    private Object modelLock;

    //-------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------

    public ArsonistModel() {
        Model.Builder modelBuilder = new Model.Builder();
        model = modelBuilder.build();
        modelLock = new Object();
    }

    @PostConstruct
    public void onPostConstruct() {
        Model.Builder modelBuilder = new Model.Builder(settings.getWidth(), settings.getHeight());
        Model newModel = modelBuilder.build();
        setModel(newModel);
    }


    //-------------------------------------------------------------------------
    // Getters & Setters
    //-------------------------------------------------------------------------

    public synchronized Model getModel() {
        return model;
    }

    public synchronized void setModel(Model model) {
        this.model = model;
        synchronized(modelLock) {
            modelLock.notifyAll();
        }
    }

    
    //-------------------------------------------------------------------------
    // Wait model management
    //-------------------------------------------------------------------------

    /**
     * Wait until the model is updated.
     * @return the new Model
     * @throws InterruptedException if the thread is interrupted
     */
    public Model waitModelUpdate() throws InterruptedException {
        synchronized(modelLock) {
            modelLock.wait();
        }
        return getModel();
    }

}
