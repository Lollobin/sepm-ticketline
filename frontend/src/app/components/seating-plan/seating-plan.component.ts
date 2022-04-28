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
  drawNoSeatArea,
  drawStandingArea,
  drawArea,
  SeatingPlan,
  SectorWithLocation,
  Sector,
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
    this.drawSeatingPlan(app.stage, <SeatingPlan>sample);
    console.log(app.stage.getChildByName("seat3"))
  }
  private drawSeatingPlan(stage: Container, seatingPlan: SeatingPlan) {
    this.drawSeats(stage, seatingPlan);
    this.drawStandingAreas(stage, seatingPlan)
    this.drawStaticAreas(stage, seatingPlan)
  }
  private drawSeats(stage: Container, seatingPlan: SeatingPlan) {
    const sectorMap: { [id: number]: Sector | SectorWithLocation } = {};
    for (const sector of seatingPlan.sectors) {
      sectorMap[sector.id] = sector;
    }
    for (const seat of seatingPlan.seats) {
      if (sectorMap[seat.sectorId].noSeats === false) {
        const seatGraphics = drawArea(
          seat.location,
          { baseColor: 0xf0f0f0, strokeColor: sectorMap[seat.sectorId].color },
          4
        );
        seatGraphics.name = `seat${seat.id}`
        this.addListeners(
          seatGraphics,
          () => {},
          () => {},
          () => {}
        );
        stage.addChild(seatGraphics);
      }
    }
  }
  private drawStandingAreas(stage: Container, seatingPlan: SeatingPlan) {
    const standingAreas = <Array<SectorWithLocation>>(
      seatingPlan.sectors.filter((sector) => sector.noSeats)
    );
    const seatCounts = countBy(seatingPlan.seats, "sectorId");
    for (const standingArea of standingAreas) {
      const numberOfAvailableSeats = seatCounts[standingArea.id];
      const standingAreaGraphics = drawStandingArea(
        standingArea.location,
        { baseColor: 0xf0f0f0, strokeColor: standingArea.color },
        numberOfAvailableSeats,
        0,
        standingArea.description ? standingArea.description : ""
      );
      standingAreaGraphics.name = `standingArea${standingArea.id}`
      stage.addChild(standingAreaGraphics);

    }
  }
  private drawStaticAreas(stage: Container, seatingPlan: SeatingPlan){
    for(const staticArea of seatingPlan.staticElements){
      const staticGraphics = drawNoSeatArea(
        staticArea.location,
        { baseColor: 0xf0f0f0, strokeColor: staticArea.color },
        staticArea.description ? staticArea.description : ""
      )
      stage.addChild(staticGraphics)
    }
  }
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
