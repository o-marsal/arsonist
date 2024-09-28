package oml.arsonist.rest;

import oml.arsonist.ArsonistModel;
import oml.arsonist.model.Model;
import oml.arsonist.service.SimulationAlreadyRunningException;
import oml.arsonist.service.SimulationService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Web-Services Endpoints
 */
@RestController
public class ArsonistEndpoints {

  private static final long GET_STATE_TIMEOUT_MS = 60000; // 1 min

  private static final Logger LOG = Logger.getLogger("ArsonistEndpoints");

  @Autowired
  private ArsonistModel appModel;

  @Autowired
  private SimulationService simulationService;

  private ExecutorService executors = Executors.newFixedThreadPool(20);


  //---------------------------------------------------------------------------
  // Endpoints
  //---------------------------------------------------------------------------

  /**
   * Handle GET /state
   * Returns the state model, as 200 OK.
   * If time is provided, waits a model newer than the given time (long polling).
   * In case of long polling timeout, returns a 204 NO CONTENT.
   * @param timestr String (optionnal) - the time of the last known model
   * @return the asynchonous DeferredResult of the ResponseEntity(model).
   */
  @GetMapping("/state")
  public DeferredResult<ResponseEntity<String>> getState(@RequestParam(value = "time", defaultValue = "0") String timestr) {
    LOG.info("GET /state ; time=" + timestr + " ...");
    DeferredResult<ResponseEntity<String>> result = new DeferredResult<>(GET_STATE_TIMEOUT_MS);
    Future<?> task = executors.submit(() -> {
      try {

        long time = 0;
        try {
          time = Long.parseLong(timestr);
        } catch (NumberFormatException e) {} // keep 0

        Model model = appModel.getModel();
        while (model.getTime() <= time) {
          model = appModel.waitModelUpdate();
        }

        result.setResult(ResponseEntity.ok(model.toJson()));
        LOG.info("GET /state ; time=" + timestr + " : Done");
  
      } catch (InterruptedException e) {
        LOG.info("GET /state ; time=" + timestr + " : Interrupted");
        // noContent result already sent
      } catch(Exception e) {
        e.printStackTrace();
        result.setErrorResult(e.getClass().getName() + " " + e.getMessage());
      }

    });
    result.onTimeout(() -> {
      LOG.info("GET /state ; time=" + timestr + " : Timeout");
      result.setResult(ResponseEntity.noContent().build());
      task.cancel(true);
    });

    return result;
  }

  /**
   * Handle GET /start
   * Start the simulation, and returns 200 OK with message "OK".
   * If the simulation is already started, return 200 OK with message "ALREADY_RUNNING".
   * @return the ResponseEntity
   */
  @GetMapping("/start")
  public ResponseEntity<String> start() {
    LOG.info("GET /start");
    try {

      simulationService.startSimulation();
      return ResponseEntity.ok("OK");

    } catch (SimulationAlreadyRunningException e) {
      return ResponseEntity.ok("ALREADY_RUNNING");
    } catch(Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().build();
    }
  }

}
