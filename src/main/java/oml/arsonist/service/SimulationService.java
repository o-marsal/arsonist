
package oml.arsonist.service;

import oml.arsonist.model.Model;
import oml.arsonist.model.ForestState;
import oml.arsonist.model.State;
import oml.arsonist.ArsonistModel;
import oml.arsonist.ArsonistSettings;

import java.util.logging.Logger;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimulationService {

    private static final Logger LOG = Logger.getLogger("SimulationService");

    @Autowired
	  private ArsonistModel appModel;

	@Autowired
	private ArsonistSettings settings;

    private Timer timer;
    private TimerTask timerTask;
    private Random random;


    //-------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------

    public SimulationService() {
        random = new Random();
        timer = null;
    }


    //-------------------------------------------------------------------------
    // Public methods
    //-------------------------------------------------------------------------

    /**
     * Start the simulation task
     * @throws SimulationAlreadyRunningException if the simulation is already started
     */
    public void startSimulation() throws SimulationAlreadyRunningException {
        LOG.info("startSimulation");
        if (timer != null) throw new SimulationAlreadyRunningException();
        
        // Reset model
        Model.Builder newModelBuilder = appModel.getModel().duplicate();
        newModelBuilder.setState(State.RUNNING);
        newModelBuilder.resetStep();
        appModel.setModel(newModelBuilder.build());

        // Create timer
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                onTimerTick();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 50, settings.getStepDurationMs());
    }

    /**
     * Stop the simulation task.
     * Does nothing is the simulation is not started.
     */
    public void stopSimulation() {
        LOG.info("stopSimulation");
        if (timer == null) return; // already stopped, nothing to do

        timer.cancel();
        timer = null;
    }


    //-------------------------------------------------------------------------
    // Internal methods
    //-------------------------------------------------------------------------

    private void onTimerTick() {
        // Update model
        Model prevModel = appModel.getModel();
        Model.Builder newModelBuilder = prevModel.duplicate();
        newModelBuilder.incrementStep();
        if (newModelBuilder.getStep() == 1) {
            computeFirstStep(newModelBuilder);
        } else {
            computeNextStep(prevModel, newModelBuilder);
        }

        // Need to stop simulation ?
        if (!newModelBuilder.remainsABurningTree()) {
            stopSimulation();
            newModelBuilder.setState(State.STOPPED);
        }

        Model model = newModelBuilder.build();
        appModel.setModel(model);
        LOG.info("Next step: " + model.toJson());
    }

    private void computeFirstStep(Model.Builder newModelBuilder) {
        int width = newModelBuilder.getWidth();
        int height = newModelBuilder.getHeight();
        for (int x=0 ; x<width ; x++) {
            for (int y=0 ; y<height ; y++) {
                if (randFireInitial()) {
                    newModelBuilder.setForestStateAt(x, y, ForestState.BURNING);
                } else {
                    newModelBuilder.setForestStateAt(x, y, ForestState.ALIVE);
                }
            }
        }
    }

    private void computeNextStep(Model prevModel, Model.Builder newModelBuilder) {
        ForestState prevForestState;
        int width = newModelBuilder.getWidth();
        int height = newModelBuilder.getHeight();

        // Loop the grid cells of the new model
        for (int x=0 ; x<width ; x++) {
            for (int y=0 ; y<height ; y++) {
                prevForestState = prevModel.getForestStateAt(x, y);
                switch(prevForestState) {
                    case ALIVE:
                        int nbBurningNearbyTrees = countBurningNearbyTrees(x, y, prevModel);
                        if (randFirePropagate(nbBurningNearbyTrees)) {
                            newModelBuilder.setForestStateAt(x, y, ForestState.BURNING);
                        }
                        break;

                    case BURNING:
                    case BURNED:
                        newModelBuilder.setForestStateAt(x, y, ForestState.BURNED);
                        break;
                }
            }
        }
    }

    private int countBurningNearbyTrees(int x, int y, Model model) {
        int nb = 0;        
        if (model.getForestStateAt(x+1, y) == ForestState.BURNING) nb++;
        if (model.getForestStateAt(x-1, y) == ForestState.BURNING) nb++;
        if (model.getForestStateAt(x, y+1) == ForestState.BURNING) nb++;
        if (model.getForestStateAt(x, y-1) == ForestState.BURNING) nb++;
        return nb;
    }

    // true means burning
    private boolean randFirePropagate(int nbBurningNearbyTrees) {
        float probability = settings.getFireExpansionProbability();
        for (int i=0 ; i<nbBurningNearbyTrees ; i++) {
            float value = random.nextFloat();
            if (value <= probability) return true;
        }
        return false;
    }

    // true means burning
    private boolean randFireInitial() {
        float probability = settings.getFireInitialProbability();
        float value = random.nextFloat();
        return value <= probability;
    }

}
