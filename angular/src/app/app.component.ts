import { Component, HostListener, InjectionToken } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ForestComponent } from './forest/forest.component';
import { AppService } from './app.service';
import { Model, newModel } from './model/model';

/**
 * This Angular Component is the main component of the application.
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ForestComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  model: Model;

  //---------------------------------------------------------------------------
  // Constructor
  //---------------------------------------------------------------------------

  constructor(private appService: AppService) {
    this.model = newModel();
  }


  //---------------------------------------------------------------------------
  // Getters & Setters
  //---------------------------------------------------------------------------

  getModel(): Model {
    return this.model;
  }


  //---------------------------------------------------------------------------
  // Events
  //---------------------------------------------------------------------------

  // Angular event
  ngOnInit() {
    console.log("Starting application Arsonist");
  }

  // Angular event
  ngOnDestroy() {
    this.appService.stopStateTimer();
  }

  // DOM event
  // Don't use Angular event like `ngAfterViewInit`, this will block the build `ng build`.
  @HostListener ("document:DOMContentLoaded", ['$event'])
  onDOMContentLoaded(event: Event) {
    console.log("onDOMContentLoaded");
    this.appService.startStateTimer(this);
  }

  // UI event: from user
  onStartButtonClick() {
    console.log("onStartButtonClick");
    this.appService.callStart();
  }

  // Internal event: from appService
  onModelUpdated(model: Model) {
    console.log("onModelUpdated", model);
    if (model) {
      this.model = model;
    }
  }

}