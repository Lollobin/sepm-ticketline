import { Component, OnInit, ViewChild } from "@angular/core";
import {
  Seat,
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
  selector: "app-location-creator",
  templateUrl: "./location-creator.component.html",
  styleUrls: ["./location-creator.component.scss"],
})
export class LocationCreatorComponent implements OnInit {
  page: number;
  registrationForm: FormGroup;
  submitted = true
  faXmark = faXmark;
  sectors: SectorBuilder[] = [];
  location: LocationWithoutId
  seatingPlan: SeatingPlanWithoutId
  chosenElement: ClickElement;
  selectedSector: number;
  seatInformation: { [provisionalId: number]: { rowNumber: number; seatNumber: number } } = {};
  @ViewChild(SeatingPlanEditorComponent) seatingPlanEditor: SeatingPlanEditorComponent;

  constructor(private formBuilder: FormBuilder) {
    this.registrationForm = this.formBuilder.group({
          name: ['', [Validators.required]],
          address: this.formBuilder.group({
            houseNumber: ['', [Validators.required]],
            street: ['', [Validators.required]],
            zipCode: ['', [Validators.required]],
            city: ['', [Validators.required]],
            country: ['', [Validators.required]]
          }),
        }
    );
  }

  ngOnInit(): void {
    this.page = 0;
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
  previousPage() {
    this.page--;
  }
  finish(){
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
