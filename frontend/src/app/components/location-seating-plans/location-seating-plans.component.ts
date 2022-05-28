import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Location, LocationsService } from "src/app/generated-sources/openapi";

@Component({
  selector: "app-location-seating-plans",
  templateUrl: "./location-seating-plans.component.html",
  styleUrls: ["./location-seating-plans.component.scss"],
})
export class LocationSeatingPlansComponent implements OnInit {
  locationId = 1;
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
}
