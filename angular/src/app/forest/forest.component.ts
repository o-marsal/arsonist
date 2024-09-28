import { Component, HostListener, Inject, inject, Input } from '@angular/core';
import { Model, newModel } from '../model/model';
import { TreeComponent } from '../tree/tree.component';
import { DOCUMENT } from '@angular/common';

/**
 * This Angular Component manage the Forest grid.
 */
@Component({
  selector: 'forest',
  standalone: true,
  imports: [TreeComponent],
  templateUrl: './forest.component.html',
  styleUrl: './forest.component.css'
})
export class ForestComponent {

  @Input()
  model: Model = newModel();

  window: Window | null;

  rows: any[] = [];

  tableOrientationClass: string = "";

  aspectRatio: string = "1";


  //---------------------------------------------------------------------------
  // Constructors
  //---------------------------------------------------------------------------

  constructor(@Inject(DOCUMENT) private document: Document) {
      this.window = this.document.defaultView;
  }


  //---------------------------------------------------------------------------
  // Events
  //---------------------------------------------------------------------------

  // Angular event: the model is changed
  ngOnChanges() {
    try {
      let model = this.model; // copy model, to always use the same instance
      let rows: any[] = []; // don't build this.rows directly, or angular will render rows in a inconsistent state
      let row: any = {};
      let idx = 0;
      for (let y=0 ; y<model.height ; y++) {
        row = {
          id: y,
          str: model.grid.substring(idx, idx+model.width)
        };
        rows[y] = row;
        idx += model.width;
      }

      // Assign variables
      this.aspectRatio = model.width + "/" + model.height;
      this.tableOrientationClass = this.computeTableOrientationClass(model.width, model.height);
      this.rows = rows;
    } catch(e) {
      console.error(e);
    }
  }

  // DOM event: the window viewport is resied
  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.tableOrientationClass = this.computeTableOrientationClass(this.model.width, this.model.height);
  }


  //---------------------------------------------------------------------------
  // Public functions
  //---------------------------------------------------------------------------

  /**
   * Compute the field tableOrientationClass.
   * It contains the css class to use, to choose the display layout:
   * Landscape means full width, and the height is calculated from the width and aspectRatio.
   * Portrait means full height, and the width is calculated from the height and aspectRatio.
   */
  computeTableOrientationClass(width: number, height: number): string {
    if (this.window) {
      let aspectRatio = (width * 1.0) / (height * 1.0);
      let tableHeightIfLandscape = 0.9 * this.window.innerWidth / aspectRatio;
      let tableHeightIfPortrait = 0.8 * this.window.innerHeight;
      if (tableHeightIfLandscape < tableHeightIfPortrait) {  
        return "table-landscape";
      } else {
        return "table-portrait";
      }
    }
    return "";
  }

}
