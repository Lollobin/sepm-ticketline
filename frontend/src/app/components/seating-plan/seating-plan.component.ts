import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { countBy, mapValues } from "lodash";
import {
  Application,
  Container,
  Graphics,
  Rectangle,
  Text,
  TextStyle,
} from "pixi.js";
import {
  SeatingPlan,
  drawSeatingPlan,
} from "./drawElements";
import sample from "./sampleStructure.json";
@Component({
  selector: "app-seating-plan",
  templateUrl: "./seating-plan.component.html",
  styleUrls: ["./seating-plan.component.scss"],
})
export class SeatingPlanComponent implements OnInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  private seatHoverAlpha = 0.7;
  constructor() {}
  ngAfterViewInit() {
    const app = new Application({
      resizeTo: this.pixiContainer.nativeElement,
      antialias: true,
      backgroundAlpha: 0,
    });
    this.pixiContainer.nativeElement.appendChild(app.view);
    drawSeatingPlan(app.stage, <SeatingPlan>sample);
    console.log(app.stage.getChildByName("seat3"))
  }

  ngOnInit(): void {}
}
