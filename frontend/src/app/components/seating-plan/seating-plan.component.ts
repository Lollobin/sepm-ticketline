import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { Application, Graphics, Rectangle, Text, TextStyle } from "pixi.js";
import { drawNoSeatArea, drawStandingArea, drawArea } from "./drawElements";
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
    for (let x = 0; x < 10; x++) {
      for (let y = 0; y < 30; y++) {
        const seat = drawArea(
          { x: x * 15+5, y: y * 15+5, w: 10, h: 10 },
          { baseColor: 0xf0f0f0, strokeColor:  y>15? 0xaa0000: 0xaaaa00 },
          4
        );
        this.addListeners(
          seat,
          () => {},
          () => {},
          () => {}
        );
        app.stage.addChild(seat);
      }
    }
    for (let x = 0; x < 10; x++) {
      for (let y = 0; y < 30; y++) {
        const seat = drawArea(
          { x: x * 15+455, y: y * 15+5, w: 10, h: 10 },
          { baseColor: 0xf0f0f0, strokeColor: y>15? 0xaa0000: 0xaaaa00 },
          4
        );
        this.addListeners(
          seat,
          () => {},
          () => {},
          () => {}
        );
        app.stage.addChild(seat);
      }
    }
    app.stage.addChild(
      drawNoSeatArea(
        { x: 200, y: 0, w: 200, h: 100 },
        { baseColor: 0xf0f0f0, strokeColor: 0x000000 },
        "STAGE, when there is more text there is more text and this is good"
      )
    );
    app.stage.addChild(
      drawStandingArea(
        { x: 200, y: 240, w: 200, h: 200 },
        { baseColor: 0xf0f0f0, strokeColor: 0x0000aa },
        15,
        10,
        "STANDING SECTOR 1"
      )
    );
    app.stage.addChild(
      drawStandingArea(
        { x: 200, y: 120, w: 200, h: 100 },
        { baseColor: 0xf0f0f0, strokeColor: 0x00aaaa },
        100,
        15,
        "STANDING SECTOR 2"
      )
    );
  }
  private updateStandingAreaSeats() {}

  private addListeners(
    graphics: Graphics,
    mouseOverCallback: () => void,
    mouseOutCallback: () => void,
    clickCallback: () => void
  ) {
    graphics.interactive = true;
    graphics.buttonMode = true;
    graphics.on("mouseover", () => {
      graphics.alpha = this.seatHoverAlpha;
      mouseOverCallback();
    });
    graphics.on("mouseout", () => {
      graphics.alpha = 1;
      mouseOutCallback();
    });
    graphics.on("click", () => {
      console.log("click");
      clickCallback();
    });
  }
  ngOnInit(): void {}
}
