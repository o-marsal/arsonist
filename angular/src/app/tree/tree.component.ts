import { Component, Input } from '@angular/core';

/**
 * This Angular Component manage a single Tree of the Forest.
 */
@Component({
  selector: 'tree',
  standalone: true,
  imports: [],
  templateUrl: './tree.component.html',
  styleUrl: './tree.component.css'
})
export class TreeComponent {

  @Input()
  model: string = "";

  /**
   * @returns the css class to use, accodring to the tree state model.
   */
  getClass() : string {
    switch (this.model) {
      case "T": return "tree-alive";
      case "*": return "tree-burning";
      case "X": return "tree-burned";
    }
    return "";
  }

}
