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
import { Application, Container, Graphics, Rectangle } from "pixi.js";
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

const getLargerX = (element: { location: Location }, largestX: number) =>
  largestX < element.location.x + element.location.w
    ? element.location.x + element.location.w
    : largestX;
const getLargerY = (element: { location: Location }, largestY: number) =>
  largestY < element.location.y + element.location.h
    ? element.location.y + element.location.h
    : largestY;

@Component({
  selector: "app-seating-plan-editor",
  templateUrl: "./seating-plan-editor.component.html",
  styleUrls: ["./seating-plan-editor.component.scss"],
})
export class SeatingPlanEditorComponent implements AfterViewInit {
  @ViewChild("pixiContainer") pixiContainer: ElementRef<HTMLDivElement>;
  @Output() clickElement = new EventEmitter<ClickElement>();

  pixiApplication: Application;
  seatingPlan: SeatingPlan;
  selectedElements: { [key: string]: { seatGraphics: Graphics; seatCover: Graphics } } = {};
  dragging = "";
  dragStartEventData = null;

  constructor() {}

  @Input() set sectors(sectors: SectorBuilder[]) {
    this.seatingPlan = generateFromSectorBuilder(sectors);
    if (this.pixiApplication) {
      this.initializeSeatingPlan();
      this.syncModelWithGraphics();
    }
  }

  initializeSeatingPlan() {
    this.pixiApplication.stage.removeChildren();
    this.pixiApplication.view.width = this.seatingPlan.general.width;
    this.pixiApplication.view.height = this.seatingPlan.general.height;
    this.pixiContainer.nativeElement.appendChild(this.pixiApplication.view);
    drawSeatingPlan(this.pixiApplication.stage, this.seatingPlan);
    this.addDragAndDrop(this.pixiApplication.stage, this.seatingPlan);
    forEach(this.selectedElements, (selectedElement, key) => {
      const seatGraphics = this.pixiApplication.stage.getChildByName(key) as Graphics | undefined;
      const seatCover = this.pixiApplication.stage.getChildByName(`${key}_cover`) as
        | Graphics
        | undefined;
      if (seatGraphics) {
        this.selectedElements[key] = { seatGraphics, seatCover };
        seatCover.visible = true;
      } else {
        delete this.selectedElements[key];
      }
    });
  }
  changeLocation(graphics: Graphics, location: Location) {
    graphics.setTransform(location.x, location.y);
    graphics.width = location.w;
    graphics.height = location.h;
  }
  syncModelWithGraphics() {
    let largestX = 0;
    let largestY = 0;
    this.seatingPlan.seats.forEach((element: Seat) => {
      if (!element.location) {
        return;
      }
      element.location = this.syncElement(
        element as { location: Location },
        generateSeatId(element.id)
      );
      largestX = getLargerX(element as { location: Location }, largestX);
      largestY = getLargerY(element as { location: Location }, largestY);
    });
    this.seatingPlan.sectors.forEach((element: SectorWithLocation) => {
      if (!element.location) {
        return;
      }
      element.location = this.syncElement(element, generateStandingAreaId(element.id));
      largestX = getLargerX(element as { location: Location }, largestX);
      largestY = getLargerY(element as { location: Location }, largestY);
    });
    this.seatingPlan.staticElements.forEach((element) => {
      element.location = this.syncElement(element, generateStaticAreaId(element.id));
      largestX = getLargerX(element as { location: Location }, largestX);
      largestY = getLargerY(element as { location: Location }, largestY);
    });
    this.seatingPlan.general.width = largestX;
    this.seatingPlan.general.height = largestY;
    this.initializeSeatingPlan();
  }
  syncElement<T extends { location: Location }>(element: T, id: string) {
    const graphics = this.pixiApplication.stage.getChildByName(id) as Graphics | undefined;
    if (!graphics) {
      return;
    }
    return this.parseRectangleToLocation(graphics.getBounds());
  }
  parseRectangleToLocation(bounds: Rectangle): Location {
    return {
      h: bounds.height,
      w: bounds.width,
      x: bounds.x,
      y: bounds.y,
    };
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
    if (this.dragging !== graphics.name) {
      return;
    }
    const newPosition = event.data.getLocalPosition(graphics.parent);
    forEach(this.selectedElements, ({ seatGraphics, seatCover }) => {
      //Object that has been dragged should be moved last
      if (graphics.name === seatGraphics.name) {
        return;
      }
      const xPositionDifference = seatGraphics.x - graphics.x;
      const yPositionDifference = seatGraphics.y - graphics.y;
      const newX = newPosition.x + xPositionDifference;
      const newY = newPosition.y + yPositionDifference;
      this.setPosition(seatGraphics, newX, newY);
      this.setPosition(seatCover, newX, newY);
    });
    this.setPosition(graphics, newPosition.x, newPosition.y);
    this.setPosition(cover, newPosition.x, newPosition.y);

    this.pixiApplication.renderer.resize(
      this.pixiApplication.view.width < newPosition.x + graphics.width
        ? newPosition.x + graphics.width
        : this.pixiApplication.view.width,
      this.pixiApplication.view.height < newPosition.y + graphics.height
        ? newPosition.y + graphics.height
        : this.pixiApplication.view.height
    );
    this.seatingPlan.general.height =
      this.pixiApplication.view.height < newPosition.y + graphics.height
        ? newPosition.y + graphics.height
        : this.pixiApplication.view.height;
    this.seatingPlan.general.width =
      this.pixiApplication.view.width < newPosition.x + graphics.width
        ? newPosition.x + graphics.width
        : this.pixiApplication.view.width;
  }

  setPosition(graphics: Graphics | undefined, x: number, y: number) {
    if (!graphics) {
      return;
    }
    graphics.x = x;
    graphics.y = y;
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
      description: "",
    });
    this.initializeSeatingPlan();
    this.syncModelWithGraphics();
  }

  addGenericDragAndDrop(
    graphics: Graphics,
    clickCallback = (event) => {},
    pointermoveCallback = (event) => this.onDragMove(event, graphics)
  ) {
    graphics.interactive = true;
    graphics.buttonMode = true;
    addButtonListeners(graphics, {
      mouseover: () => {},
      mouseout: () => {},
      click: clickCallback,
    });
    graphics
      .on("pointerdown", () => this.onDragStart(graphics))
      .on("pointerup", () => this.onDragEnd(graphics))
      .on("pointerupoutside", () => this.onDragEnd(graphics))
      .on("pointermove", (event) => pointermoveCallback(event));
  }
  addSeatDragAndDrop(stage: Container, seat: Seat) {
    const seatGraphics = stage.getChildByName(generateSeatId(seat.id));
    const seatCover = stage.getChildByName(`${generateSeatId(seat.id)}_cover`);
    if (!seatGraphics) {
      return;
    }

    const clickEvent = (event) => {
      this.clickElement.emit({ data: seat, type: "Seat" });
      if (event.data.originalEvent.ctrlKey) {
        (!this.selectedElements[seatGraphics.name] ? this.selectSeat : this.deselectSeat)(
          seatGraphics as Graphics,
          seatCover as Graphics
        );
      } else if (!this.selectedElements[seatGraphics.name]) {
        this.deselectAllSeats();
      }
    };

    this.addGenericDragAndDrop(seatGraphics as Graphics, clickEvent, (event) =>
      this.onDragMove(event, seatGraphics as Graphics, seatCover as Graphics)
    );
  }
  addDragAndDrop(stage: Container, seatingPlan: SeatingPlan) {
    seatingPlan.seats.forEach((seat) => this.addSeatDragAndDrop(stage, seat));
    seatingPlan.sectors.forEach((sector) => {
      const sectorGraphics = stage.getChildByName(generateStandingAreaId(sector.id));
      if (!sectorGraphics) {
        return;
      }
      this.addGenericDragAndDrop(sectorGraphics as Graphics, () => {
        this.clickElement.emit({
          data: sector as SectorWithLocation,
          type: "SectorWithLocation",
        });
      });
    });
    seatingPlan.staticElements.forEach((element) => {
      const staticGraphics = stage.getChildByName(generateStaticAreaId(element.id));
      if (!staticGraphics) {
        return;
      }
      this.addGenericDragAndDrop(staticGraphics as Graphics, () => {
        this.clickElement.emit({
          data: element as StaticElement,
          type: "StaticElement",
        });
      });
    });
  }
  ngAfterViewInit() {
    this.pixiApplication = new Application({
      antialias: true,
      backgroundAlpha: 0,
    });
    if (this.seatingPlan) {
      this.initializeSeatingPlan();
      this.syncModelWithGraphics();
    }
  }
}
