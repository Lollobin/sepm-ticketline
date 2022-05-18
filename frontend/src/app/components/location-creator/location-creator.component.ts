import { Component, OnInit } from "@angular/core";
import { Show, ShowInformation } from "src/app/generated-sources/openapi";
import { Sector } from "src/app/shared_modules/seatingPlanGraphics";
import { faXmark } from "@fortawesome/free-solid-svg-icons";

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
  sectors: {
    price: number;
    color: string;
    standingSector: boolean;
    description: string;
    seatCount: number;
  }[] = [];
  ngOnInit(): void {
    this.page = 1;
    this.sectors.push({
      price: 12.5,
      color: "#aaaaaa",
      standingSector: true,
      description: "",
      seatCount: 0,
    });
  }
  removeSector(index: number) {
    this.sectors.splice(index, 1);
  }
  addSector() {
    this.sectors.push({
      price: 0,
      color: "#aaaaaa",
      standingSector: true,
      description: "",
      seatCount: 0,
    });
  }
  convertToCurrency(value: number) {
    return value.toLocaleString(undefined, { style: "currency", currency: "EUR" });
  }
}
