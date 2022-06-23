import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { Location, LocationsService, SeatingPlan } from "src/app/generated-sources/openapi";

@Component({
  selector: "app-location-seating-plans",
  templateUrl: "./location-seating-plans.component.html",
  styleUrls: ["./location-seating-plans.component.scss"],
})
export class LocationSeatingPlansComponent implements OnInit {
  locationId = 1;
  location: Location;
  seatingPlans: SeatingPlan[];
  error: Error;
  constructor(private route: ActivatedRoute, private locationsService: LocationsService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.locationId = params["id"];
      this.locationsService.locationsIdGet(this.locationId).subscribe({
        next: (location) => {
          this.location = location;
          this.locationsService.locationsIdSeatingPlansGet(this.locationId).subscribe({
            next: (seatingPlans) => {
              this.seatingPlans = seatingPlans;
            },
            error: (error) => {
              this.error = error;
              this.toastr.error(error.errorMessage);
            },
          });
        },
        error: (error) => {
          this.error = error;
          this.toastr.error(error.errorMessage);
        },
      });
    });
  }
}
