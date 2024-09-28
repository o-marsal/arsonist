import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http'; 
import { Model } from './model/model'; 
import { AppComponent } from './app.component'; 

/**
 * Application Service:
 * - call the web services
 * - manage long polling of the state Model
 */
@Injectable({
  providedIn: 'root'
})
export class AppService {

  appComponent: AppComponent | null;
  getStateRunning: boolean;
  stateTimerId: any;


  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  constructor(private http: HttpClient) {
    this.appComponent = null;
    this.getStateRunning = false;
    this.stateTimerId = null;
  }


  //---------------------------------------------------------------------------
  // Timer
  //---------------------------------------------------------------------------

  /**
   * Start the long-polling timer of the state Model.
   * The appComponent must be provided, as it can't be inject (loop dependency).
   * @param appComponent 
   */
  startStateTimer(appComponent: AppComponent) {
    console.log("startStateTimer");
    this.appComponent = appComponent;
    if (!this.stateTimerId) {
      // Check every 100ms if a new getState should be asked.
      // Do not use a recursive call of setTimeout (infinite stack call => memory crash).
      this.stateTimerId = setInterval(this.onStateTimerTick.bind(this), 100);
    }
  }

  /**
   * Stop the long-polling timer of the state Model.
   */
  stopStateTimer() {
    console.log("stopStateTimer");
    if (this.stateTimerId) {
      clearInterval(this.stateTimerId);
      this.stateTimerId = null;
    }
  }


  //---------------------------------------------------------------------------
  // Events
  //---------------------------------------------------------------------------

  // Internal event: from the timer
  onStateTimerTick() {
    if (!this.stateTimerId) return;
    if (this.getStateRunning) return;
    try {
      this.callGetState();
    } catch(e) {
      console.error("error calling callGetState", e);
    }
  }


  //---------------------------------------------------------------------------
  // Web services
  //---------------------------------------------------------------------------

  /**
   * Call the web service GET /start
   * This will ask the backend to start the simulation.
   */
  callStart() {
    console.log("Calling /start");
    const httpOptions = {
      responseType: 'text' as const,
    };
    this.http.get("/start", httpOptions).subscribe();
  }

  /**
   * Call the web service GET /state
   * This will ask the backend the last state Model.
   * The call is a long-polling, and the result may be deferred.
   * This will call onCallGetStateDone() at the end.
   * @returns 
   */
  callGetState() {
    if (!this.appComponent) return; // should never happen
    const appService = this;
    this.getStateRunning = true;

    // Build parameters
    let model = this.appComponent.getModel();
    let params = new HttpParams();
    if (model && model.time > 0) {
      params = params.set("time", model.time);
    }
    let options = { params: params };
    console.log("Calling /state ", params.toString(), "...");

    // Exec http query
    this.http.get<Model>("/state", options).subscribe(model => {
      try {
        if (model) {
          console.log("Calling /state : Success");
          if (this.appComponent) {
            this.appComponent.onModelUpdated(model);
          }
        } else {
          console.log("Calling /state : empty model (long polling timeout)");
        }
      } finally {
        this.getStateRunning = false;
      }
    });
  }

}
