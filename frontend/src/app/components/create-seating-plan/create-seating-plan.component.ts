import { Component, OnInit, ViewChild } from "@angular/core";
import {
  Seat,
  SeatingPlan,
  Sector,
  SectorBuilder,
  SectorWithLocation,
  StaticElement,
} from "src/app/shared_modules/seatingPlanGraphics";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import { SeatingPlanEditorComponent } from "../seating-plan-editor/seating-plan-editor.component";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { LocationWithoutId, SeatingPlanWithoutId } from "src/app/generated-sources/openapi";

type ClickElement =
  | {
      data: SectorWithLocation;
      type: "SectorWithLocation";
    }
  | { data: Seat; type: "Seat" }
  | { data: StaticElement; type: "StaticElement" };

@Component({
  selector: 'app-create-seating-plan',
  templateUrl: './create-seating-plan.component.html',
  styleUrls: ['./create-seating-plan.component.scss'],
})
export class CreateSeatingPlanComponent implements OnInit {
  page: number;
  faXmark = faXmark;
  sectors: SectorBuilder[] = [];
  chosenElement: ClickElement;
  selectedSector: number;
  seatInformation: { [provisionalId: number]: { rowNumber: number; seatNumber: number } } = {};
  @ViewChild(SeatingPlanEditorComponent) seatingPlanEditor: SeatingPlanEditorComponent;

  constructor() {
  }

  ngOnInit(): void {
    this.page = 1;
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
      seatCount: 1,
    });
  }
  nextPage() {
    this.page++;
  }
  previousPage() {
    this.page--;
  }
  finish() {
    //const seatingPlanLayout: SeatingPlan = {}

    const seatingPlan: SeatingPlanWithoutId = {
      name: "",
      //TODO: Get locationId after saving location
      locationId: 0,
      //TODO: Get locationId after saving location
      seatingPlanLayoutId: 0,
      sectors: [],
      seats: []
    };
    //TODO: Send locationWithout ID
    //Get back ID of location
    //Send seating plan with location ID
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
