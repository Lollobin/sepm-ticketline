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
              console.log(error);
              if (error.status === 0 || error.status === 500) {
                this.toastr.error(error.message);
              } else {
                this.toastr.warning(error.error);
              }
            }
          });
        },
        error: (error) => {
          console.log(error);
          if (error.status === 0 || error.status === 500) {
            this.toastr.error(error.message);
          } else {
            this.toastr.warning(error.error);
          }
        }
      });
    });
  }
}
