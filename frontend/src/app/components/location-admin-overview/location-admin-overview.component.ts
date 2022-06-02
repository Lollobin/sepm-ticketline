import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Location, LocationsService } from "src/app/generated-sources/openapi";

@Component({
  selector: "app-location-admin-overview",
  templateUrl: "./location-admin-overview.component.html",
  styleUrls: ["./location-admin-overview.component.scss"],
})
export class LocationAdminOverviewComponent implements OnInit {
  constructor(private locationsService: LocationsService, private router: Router) {}
  locations: Location[];
  page = 1;
  pageSize = 10;
  numberOfResults = 0;
  ngOnInit(): void {
    this.searchLocations();
  }
  navigateToLocation(location: Location){
    this.router.navigate(["/", "locations", location.locationId])
  }
  searchLocations() {
    this.locationsService.locationsGet(undefined, this.pageSize, this.page - 1).subscribe({
      next: (locationSearchResult) => {
        this.locations = locationSearchResult.locations;
        this.numberOfResults = locationSearchResult.numberOfResults;
      },
      error: () => {
        //TODO: ADD ERROR HANDLING
      },
    });
  }
  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.searchLocations();
  }
}
