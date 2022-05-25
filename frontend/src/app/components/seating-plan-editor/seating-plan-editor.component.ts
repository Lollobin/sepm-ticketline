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
import { forEach, uniqueId } from "lodash";
import { Application, Container, Graphics } from "pixi.js";
import { addButtonListeners } from "src/app/shared_modules/seatingPlanEvents";
import {
  drawSeatingPlan,
  generateSeatId,
  generateStandingAreaId,
  generateStaticAreaId,
  Location,
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
  pixiApplication: Application;
  seatingPlan: SeatingPlan;
  selectedElements: { [key: string]: { seatGraphics: Graphics; seatCover: Graphics } } = {};
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
    this.pixiContainer.nativeElement.appendChild(this.pixiApplication.view);
    drawSeatingPlan(this.pixiApplication.stage, this.seatingPlan);
    this.addDragAndDrop(this.pixiApplication.stage, this.seatingPlan);
  }
  changeLocation(graphics: Graphics, location: Location) {
    graphics.setTransform(location.x, location.y);
    graphics.width = location.w;
    graphics.height = location.h;
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
    this.seatingPlan.staticElements.forEach((element) => {
      const staticGraphics = this.pixiApplication.stage.getChildByName(
        generateStaticAreaId(element.id)
      );
      if (!staticGraphics) {
        return;
      }
      element.location.h = staticGraphics.getBounds().height;
      element.location.w = staticGraphics.getBounds().width;
      element.location.x = staticGraphics.getBounds().x;
      element.location.y = staticGraphics.getBounds().y;
    });
    this.initializeSeatingPlan();
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
  addStaticArea() {
    this.seatingPlan.staticElements.push({
      id: +uniqueId(),
      color: 0,
      location: {
        x: 10,
        y: 10,
        w: 100,
        h: 100,
      },
      description: ""
    });
    this.syncModelWithGraphics();
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
    seatingPlan.staticElements.forEach((element) => {
      const staticGraphics = stage.getChildByName(generateStaticAreaId(element.id));
      if (!staticGraphics) {
        return;
      }
      staticGraphics.interactive = true;
      staticGraphics.buttonMode = true;
      addButtonListeners(staticGraphics as Graphics, {
        mouseover: () => {},
        mouseout: () => {},
        click: () => {
          this.clickElement.emit({
            data: element as StaticElement,
            type: "StaticElement",
          });
        },
      });
      staticGraphics
        .on("pointerdown", () => this.onDragStart(staticGraphics as Graphics))
        .on("pointerup", () => this.onDragEnd(staticGraphics as Graphics))
        .on("pointerupoutside", () => this.onDragEnd(staticGraphics as Graphics))
        .on("pointermove", (event) => this.onDragMove(event, staticGraphics as Graphics));
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
