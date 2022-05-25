import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnInit,
  Output,
  ViewChild,
  EventEmitter,
} from "@angular/core";
import { forEach } from "lodash";
import { Application, Container, Graphics } from "pixi.js";
import { addButtonListeners } from "src/app/shared_modules/seatingPlanEvents";
import {
  drawSeatingPlan,
  drawStandingArea,
  generateSeatId,
  generateStandingAreaId,
  Seat,
  SeatingPlan,
  SectorBuilder,
  SectorWithLocation,
  StaticElement,
} from "src/app/shared_modules/seatingPlanGraphics";
import { generateFromSectorBuilder, generateFromShowInfo } from "./generateSampleFromStructure";
type ClickElement =
  | {
      data: SectorWithLocation;
      type: "SectorWithLocation";
    }
  | { data: Seat; type: "Seat" }
  | { data: StaticElement; type: "StaticElement" };

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
  selectedElements: { [key: string]: { seatGraphics: Graphics; seatCover: Graphics } } = {};
  hoverInfo = undefined;
  @Input() set sectors(sectors: SectorBuilder[]) {
    this.seatingPlan = generateFromSectorBuilder(sectors);
    if (this.pixiApplication) {
      this.initializeSeatingPlan();
    }
  }

  @Output() clickElement = new EventEmitter<ClickElement>();

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
  applyLocation(element: ClickElement) {
    console.log(element);
    if (element.type === "Seat") {
      const { seatGraphics, seatCover } = this.getSeatGraphicsAndCover(element.data);
      if (!seatCover) {
        return;
      }
      const initializedSeatGraphics = seatGraphics as Graphics;
      const initializedSeatCover = seatGraphics as Graphics;

      initializedSeatGraphics.setTransform(element.data.location.x, element.data.location.y);
      initializedSeatGraphics.width = element.data.location.w;
      initializedSeatGraphics.height = element.data.location.h;
      initializedSeatCover.setTransform(element.data.location.x, element.data.location.y);
      initializedSeatCover.width = element.data.location.w;
      initializedSeatCover.height = element.data.location.h;
    }
    if (element.type === "SectorWithLocation") {
      const sectorGraphics = this.pixiApplication.stage.getChildByName(
        generateStandingAreaId(element.data.id)
      );
      if (!sectorGraphics) {
        return;
      }
      const initializedSectorGraphics = sectorGraphics as Graphics;

      drawStandingArea(
        element.data.location,
        { baseColor: 0xf0f0f0, strokeColor: element.data.color },
        0,
        15,
        element.data.description
      );

      sectorGraphics.setTransform(element.data.location.x, element.data.location.y);
      initializedSectorGraphics.width = element.data.location.w;
      initializedSectorGraphics.height = element.data.location.h;
    }
    if (element.type === "StaticElement") {
      //TODO: IMplement feature
    }
  }
  syncModelWithGraphics() {
    this.seatingPlan.seats.forEach((seat) => {
      const { seatGraphics, seatCover } = this.getSeatGraphicsAndCover(seat);
      if (!seatGraphics) {
        return;
      }
      seat.location.h = seatGraphics.getBounds().height;
      seat.location.w = seatGraphics.getBounds().width;
      seat.location.x = seatGraphics.getBounds().x;
      seat.location.y = seatGraphics.getBounds().y;
    });
    this.seatingPlan.sectors.forEach((sector) => {
      const sectorGraphics = this.pixiApplication.stage.getChildByName(
        generateStandingAreaId(sector.id)
      );
      if (!sectorGraphics) {
        return;
      }
      const standingSector = sector as SectorWithLocation;
      standingSector.location.h = sectorGraphics.getBounds().height;
      standingSector.location.w = sectorGraphics.getBounds().width;
      standingSector.location.x = sectorGraphics.getBounds().x;
      standingSector.location.y = sectorGraphics.getBounds().y;
    });
  }

  onDragStart(graphics: Graphics) {
    graphics.alpha = 0.5;
    this.dragging = graphics.name;
  }

  onDragEnd(graphics: Graphics) {
    graphics.alpha = 1;
    this.dragging = "";
    this.syncModelWithGraphics();
  }

  onDragMove(event, graphics: Graphics, cover?: Graphics) {
    if (this.dragging === graphics.name) {
      const newPosition = event.data.getLocalPosition(graphics.parent);
      forEach(this.selectedElements, ({ seatGraphics, seatCover }) => {
        if (graphics.name === seatGraphics.name) return;
        const xPositionDifference = seatGraphics.x - graphics.x;
        const yPositionDifference = seatGraphics.y - graphics.y;

        seatGraphics.x = newPosition.x + xPositionDifference;
        seatGraphics.y = newPosition.y + yPositionDifference;
        if (seatCover) {
          seatCover.x = newPosition.x + xPositionDifference;
          seatCover.y = newPosition.y + yPositionDifference;
        }
      });
      graphics.x = newPosition.x;
      graphics.y = newPosition.y;
      if (cover) {
        cover.x = newPosition.x;
        cover.y = newPosition.y;
      }
    }
  }
  getSeatGraphicsAndCover(seat: Seat) {
    const seatGraphics = this.pixiApplication.stage.getChildByName(generateSeatId(seat.id));
    const seatCover = this.pixiApplication.stage.getChildByName(`${generateSeatId(seat.id)}_cover`);
    return { seatGraphics, seatCover };
  }

  selectAllSeats() {
    this.seatingPlan.seats.forEach((seat) => {
      const { seatGraphics, seatCover } = this.getSeatGraphicsAndCover(seat);
      if (!seatGraphics) {
        return;
      }
      this.selectSeat(seatGraphics as Graphics, seatCover as Graphics);
    });
  }
  selectSeatInSector(sectorId: number) {
    this.seatingPlan.seats.forEach((seat) => {
      const { seatGraphics, seatCover } = this.getSeatGraphicsAndCover(seat);
      if (!seatGraphics) {
        return;
      }
      if (seat.sectorId === sectorId) {
        this.selectSeat(seatGraphics as Graphics, seatCover as Graphics);
      }
    });
  }

  selectSeat(seatGraphics: Graphics, seatCover: Graphics) {
    this.selectedElements[seatGraphics.name] = {
      seatGraphics: seatGraphics as Graphics,
      seatCover: seatCover as Graphics,
    };
    seatCover.visible = true;
  }
  deselectSeat(seatGraphics: Graphics, seatCover: Graphics) {
    this.selectedElements[seatGraphics.name].seatCover.visible = false;
    delete this.selectedElements[seatGraphics.name];
  }
  deselectAllSeats() {
    forEach(this.selectedElements, (graphics) => {
      graphics.seatCover.visible = false;
    });
    this.selectedElements = {};
  }
  addDragAndDrop(stage: Container, seatingPlan: SeatingPlan) {
    seatingPlan.seats.forEach((seat) => {
      const seatGraphics = stage.getChildByName(generateSeatId(seat.id));
      const seatCover = stage.getChildByName(`${generateSeatId(seat.id)}_cover`);
      if (!seatGraphics) {
        return;
      }
      seatGraphics.interactive = true;
      seatGraphics.buttonMode = true;
      addButtonListeners(seatGraphics as Graphics, {
        mouseover: () => {},
        mouseout: () => {},
        click: () => {},
      });
      seatGraphics.on("click", (event) => {
        this.clickElement.emit({ data: seat, type: "Seat" });
        if (event.data.originalEvent.ctrlKey) {
          !this.selectedElements[seatGraphics.name]
            ? this.selectSeat(seatGraphics as Graphics, seatCover as Graphics)
            : this.deselectSeat(seatGraphics as Graphics, seatCover as Graphics);
        } else if (this.selectedElements[seatGraphics.name] == undefined) {
          this.deselectAllSeats();
        }
      });
      seatGraphics
        .on("pointerdown", () => this.onDragStart(seatGraphics as Graphics))
        .on("pointerup", () => this.onDragEnd(seatGraphics as Graphics))
        .on("pointerupoutside", () => this.onDragEnd(seatGraphics as Graphics))
        .on("pointermove", (event) =>
          this.onDragMove(event, seatGraphics as Graphics, seatCover as Graphics)
        );
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
          this.clickElement.emit({
            data: sector as SectorWithLocation,
            type: "SectorWithLocation",
          });
        },
      });
      sectorGraphics
        .on("pointerdown", () => this.onDragStart(sectorGraphics as Graphics))
        .on("pointerup", () => this.onDragEnd(sectorGraphics as Graphics))
        .on("pointerupoutside", () => this.onDragEnd(sectorGraphics as Graphics))
        .on("pointermove", (event) => this.onDragMove(event, sectorGraphics as Graphics));
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
