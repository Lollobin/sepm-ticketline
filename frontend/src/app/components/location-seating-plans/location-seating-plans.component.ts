import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Location, LocationsService, SeatingPlan } from "src/app/generated-sources/openapi";

@Component({
  selector: "app-location-seating-plans",
  templateUrl: "./location-seating-plans.component.html",
  styleUrls: ["./location-seating-plans.component.scss"],
})
export class LocationSeatingPlansComponent implements OnInit {
  locationId = 1;
  location: Location;
  seatingPlans: SeatingPlan[]
  constructor(private route: ActivatedRoute, private locationsService: LocationsService) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.locationId = params["id"];
      this.locationsService.locationsIdGet(this.locationId).subscribe({
        next: (location) => {
          this.location = location
          this.locationsService.locationsIdSeatingPlansGet(this.locationId).subscribe({
            next: (seatingPlans)=>{
              this.seatingPlans = seatingPlans
            }, 
            error: ()=>{
              //TODO: Add error handling
            }
          })
        },
        error: () => {},
      });
    });
  }
}
