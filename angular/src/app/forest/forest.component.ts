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

  rows : string[] = [];

  tableOrientationClass : string = "";

  aspectRatio = "1";


  //---------------------------------------------------------------------------
  // Constructors
  //---------------------------------------------------------------------------

  constructor(@Inject(DOCUMENT) private document: Document) {
      this.window = this.document.defaultView;
  }


  //---------------------------------------------------------------------------
  // Events
  //---------------------------------------------------------------------------

  // Angular event
  ngOnInit() {
    this.computeTableOrientationClass();
  }

  // Angular event: the model is changed
  ngOnChanges() {
    this.rows = [];
    let idx = 0;
    let row : string = "";
    for (let y=0 ; y<this.model.height ; y++) {
      row = this.model.grid.substring(idx, idx+this.model.width);
      this.rows[y] = row;
      idx += this.model.width;
    }
    this.aspectRatio = this.model.width + "/" + this.model.height;
    this.computeTableOrientationClass();
  }

  // DOM event: the window viewport is resied
  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.computeTableOrientationClass();
  }


  //---------------------------------------------------------------------------
  // Public functions
  //---------------------------------------------------------------------------

  /**
   * Compute the fields modelRatio and tableOrientationClass.
   * - modelRatio is the ratio from model (width / height), given as a constraint for the grid.
   * - tableOrientationClass contains the css class to use, to choose the display layout:
   *   Landscape means full width, and the height is calculated from the width and modelRatio.
   *   Portrait means full height, and the width is calculated from the height and modelRatio.
   */
  computeTableOrientationClass() {
    if (this.window) {
      let modelRatio = (this.model.width * 1.0) / (this.model.height * 1.0);
      let tableHeightIfLandscape = 0.9 * this.window.innerWidth / modelRatio;
      let tableHeightIfPortrait = 0.8 * this.window.innerHeight;
      if (tableHeightIfLandscape < tableHeightIfPortrait) {  
        this.tableOrientationClass = "table-landscape";
      } else {
        this.tableOrientationClass = "table-portrait";
      }
    }
  }

}
