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
import {
  LocationsService,
  SeatingPlanWithoutId,
  Location,
} from "src/app/generated-sources/openapi";
import { ActivatedRoute } from "@angular/router";

type ClickElement =
  | {
      data: SectorWithLocation;
      type: "SectorWithLocation";
    }
  | { data: Seat; type: "Seat" }
  | { data: StaticElement; type: "StaticElement" };

@Component({
  selector: "app-create-seating-plan",
  templateUrl: "./create-seating-plan.component.html",
  styleUrls: ["./create-seating-plan.component.scss"],
})
export class CreateSeatingPlanComponent implements OnInit {
  @ViewChild(SeatingPlanEditorComponent) seatingPlanEditor: SeatingPlanEditorComponent;

  locationId: number;
  page: number;
  faXmark = faXmark;
  sectors: SectorBuilder[] = [];
  chosenElement: ClickElement;
  selectedSector: number;
  seatInformation: { [provisionalId: number]: { rowNumber: number; seatNumber: number } } = {};
  seatingPlanName = "";
  location: Location = {
    //TODO: DElete test data when location get interface works
    locationId: 1,
    name: "Capital Arena",
    address: {
      houseNumber: "12",
      street: "Imaginary street",
      city: "City Capital",
      country: "Imaginarium",
      zipCode: "1212",
    },
  };

  constructor(private route: ActivatedRoute, private locationsService: LocationsService) {}

  ngOnInit(): void {
    this.page = 1;
    this.route.params.subscribe((params) => {
      this.locationId = params["id"];
      this.locationsService.locationsIdGet(this.locationId).subscribe({
        next: (location) => {
          //TODO: Add location get interface
          console.log(location);
        },
        error: () => {},
      });
    });
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
      sectors: [],
      seats: [],
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
