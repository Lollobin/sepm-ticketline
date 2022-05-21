import { Component, OnInit, ViewChild } from "@angular/core";
import { Show, ShowInformation } from "src/app/generated-sources/openapi";
import { Sector, SectorBuilder } from "src/app/shared_modules/seatingPlanGraphics";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import { SeatingPlanEditorComponent } from "../seating-plan-editor/seating-plan-editor.component";

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
  selectedSector:number
  @ViewChild(SeatingPlanEditorComponent) seatingPlanEditor: SeatingPlanEditorComponent;
  ngOnInit(): void {
    this.page = 1;
    this.sectors.push({
      color: "#0000AA",
      standingSector: true,
      description: "",
      seatCount: 15,
    },{
      color: "#AAAA00",
      standingSector: true,
      description: "",
      seatCount: 80,
    }, {
      color: "#00AA00",
      standingSector: false,
      description: "",
      seatCount: 100,
    }, {
      color: "#AA0000",
      standingSector: false,
      description: "",
      seatCount: 100,
    });
  }
  removeSector(index: number) {
    this.sectors.splice(index, 1);
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
    this.page++
  }
  convertToCurrency(value: number) {
    return value.toLocaleString(undefined, { style: "currency", currency: "EUR" });
  }
}
