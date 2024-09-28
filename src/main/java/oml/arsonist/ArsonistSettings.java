package oml.arsonist;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ArsonistSettings {

	@Value("${app.width}")
	private int width;

	@Value("${app.height}")
	private int height;

	@Value("${app.stepDurationMs}")
	private int stepDurationMs;

	@Value("${app.fireExpansionProbability}")
    private float fireExpansionProbability;

    @Value("${app.fireInitialProbability}")
    private float fireInitialProbability;

    //-------------------------------------------------------------------------
    // Getters
    //-------------------------------------------------------------------------

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStepDurationMs() {
        return stepDurationMs;
    }

    public float getFireExpansionProbability() {
        return fireExpansionProbability;
    }

    public float getFireInitialProbability() {
        return fireInitialProbability;
    }

}
