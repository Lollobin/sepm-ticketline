import { Component, OnInit, ViewChild } from "@angular/core";
import { Show, ShowInformation } from "src/app/generated-sources/openapi";
import {
  Seat,
  Sector,
  SectorBuilder,
  SectorWithLocation,
  StaticElement,
} from "src/app/shared_modules/seatingPlanGraphics";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import { SeatingPlanEditorComponent } from "../seating-plan-editor/seating-plan-editor.component";

type ClickElement =
  | {
      data: SectorWithLocation;
      type: "SectorWithLocation";
    }
  | { data: Seat; type: "Seat" }
  | { data: StaticElement; type: "StaticElement" };

@Component({
  selector: "app-location-creator",
  templateUrl: "./location-creator.component.html",
  styleUrls: ["./location-creator.component.scss"],
})
export class LocationCreatorComponent implements OnInit {
  page: number;
  constructor() {}
  faXmark = faXmark;
  showInformation: ShowInformation;
  sectors: SectorBuilder[] = [];
  chosenElement: ClickElement;
  selectedSector: number;
  seatInformation: { [provisionalId: number]: { rowNumber: number; seatNumber: number } } = {};
  @ViewChild(SeatingPlanEditorComponent) seatingPlanEditor: SeatingPlanEditorComponent;
  ngOnInit(): void {
    this.page = 1;
    this.sectors.push(
      {
        color: "#0000AA",
        standingSector: true,
        description: "",
        seatCount: 15,
      },
      {
        color: "#AAAA00",
        standingSector: true,
        description: "",
        seatCount: 80,
      },
      {
        color: "#00AA00",
        standingSector: false,
        description: "",
        seatCount: 100,
      },
      {
        color: "#AA0000",
        standingSector: false,
        description: "",
        seatCount: 100,
      }
    );
  }
  removeSector(index: number) {
    this.sectors.splice(index, 1);
  }
  numberToCssColorString(color: number) {
    return `#${color.toString(16).padStart(6, "0")}`;
  }
  parseColor(color: string): number {
    return Number.parseInt(color.substring(1), 16);
  }
  addSector() {
    this.sectors.push({
      color: "#000000",
      standingSector: true,
      description: "",
      seatCount: 0,
    });
  }
  nextPage() {
    this.page++;
  }
  convertToCurrency(value: number) {
    return value.toLocaleString(undefined, { style: "currency", currency: "EUR" });
  }
  handleChosenElement(element: ClickElement) {
    if (element.type === "Seat" && this.seatInformation[element.data.id] === undefined) {
      this.seatInformation[element.data.id] = { rowNumber: 0, seatNumber: 0 };
    }
    this.chosenElement = element;
  }
}
