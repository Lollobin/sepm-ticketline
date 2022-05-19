import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from "@angular/core";
import { Application, Container, Graphics } from "pixi.js";
import { Show, ShowInformation } from "src/app/generated-sources/openapi";
import { addButtonListeners } from "src/app/shared_modules/seatingPlanEvents";
import {
  drawSeatingPlan,
  generateSeatId,
  generateStandingAreaId,
  SeatingPlan,
  SectorBuilder,
} from "src/app/shared_modules/seatingPlanGraphics";
import { applyShowInformation } from "../seating-plan/seatingPlanEvents";
import { generateFromSectorBuilder, generateFromShowInfo } from "./generateSampleFromStructure";

@Component({
  selector: "app-seating-plan-editor",
  templateUrl: "./seating-plan-editor.component.html",
  styleUrls: ["./seating-plan-editor.component.scss"],
})
export class SeatingPlanEditorComponent implements AfterViewInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  @ViewChild("infoOverlay") infoOverlay: ElementRef<HTMLDivElement>;
  pixiApplication: Application;
  seatingPlan: SeatingPlan;
  hoverInfo = undefined;
  @Input() set sectors(sectors: SectorBuilder[]) {
    this.seatingPlan = generateFromSectorBuilder(sectors);
    if (this.pixiApplication) {
      this.initializeSeatingPlan();
    }
  }
  dragging = "";
  dragStartEventData = null;
  constructor() {}
  initializeSeatingPlan() {
    this.pixiApplication.stage.removeChildren();
    this.pixiApplication.view.width = this.seatingPlan.general.width;
    this.pixiApplication.view.height = this.seatingPlan.general.height;
    document.addEventListener("mousemove", (event) => {
      this.infoOverlay.nativeElement.style.left = event.x + 20 + "px";
      this.infoOverlay.nativeElement.style.top = event.y + "px";
      return event;
    });
    this.pixiContainer.nativeElement.appendChild(this.pixiApplication.view);
    drawSeatingPlan(this.pixiApplication.stage, this.seatingPlan);
    this.addDragAndDrop(this.pixiApplication.stage, this.seatingPlan);
  }

  onDragStart(event, graphics: Graphics) {
    this.dragStartEventData = event.data;
    graphics.alpha = 0.5;
    this.dragging = graphics.name;
  }

  onDragEnd(graphics: Graphics) {
    graphics.alpha = 1;
    this.dragging = "";
    this.dragStartEventData = null;
  }

  onDragMove(graphics: Graphics) {
    if (this.dragging === graphics.name) {
      const newPosition = this.dragStartEventData.getLocalPosition(graphics.parent);
      graphics.x = newPosition.x;
      graphics.y = newPosition.y;
    }
  }

  addDragAndDrop(stage: Container, seatingPlan: SeatingPlan) {
    seatingPlan.seats.forEach((seat) => {
      const seatGraphics = stage.getChildByName(generateSeatId(seat.id));
      if (!seatGraphics) {
        return;
      }
      seatGraphics.interactive = true;
      seatGraphics.buttonMode = true;
      addButtonListeners(seatGraphics as Graphics, {
        mouseover: () => {},
        mouseout: () => {},
        click: () => {
          console.log(seat.id);
        },
      });
      seatGraphics
        .on("pointerdown", (event) => this.onDragStart(event, seatGraphics as Graphics))
        .on("pointerup", () => this.onDragEnd(seatGraphics as Graphics))
        .on("pointerupoutside", () => this.onDragEnd(seatGraphics as Graphics))
        .on("pointermove", () => this.onDragMove(seatGraphics as Graphics));
    });
    seatingPlan.sectors.forEach((sector) => {
      const sectorGraphics = stage.getChildByName(generateStandingAreaId(sector.id));
      if (!sectorGraphics) {
        return;
      }
      sectorGraphics.interactive = true;
      sectorGraphics.buttonMode = true;
      addButtonListeners(sectorGraphics as Graphics, {
        mouseover: () => {},
        mouseout: () => {},
        click: () => {
          console.log(sector.id);
        },
      });
      sectorGraphics
      .on("pointerdown", (event) => this.onDragStart(event, sectorGraphics as Graphics))
      .on("pointerup", () => this.onDragEnd(sectorGraphics as Graphics))
      .on("pointerupoutside", () => this.onDragEnd(sectorGraphics as Graphics))
      .on("pointermove", () => this.onDragMove(sectorGraphics as Graphics));
    });
  }
  ngAfterViewInit() {
    this.pixiApplication = new Application({
      antialias: true,
      backgroundAlpha: 0,
    });
    if (this.seatingPlan) {
      this.initializeSeatingPlan();
    }
  }
}
