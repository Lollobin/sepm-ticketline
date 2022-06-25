import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { Location, LocationsService } from "src/app/generated-sources/openapi";

@Component({
  selector: "app-location-admin-overview",
  templateUrl: "./location-admin-overview.component.html",
  styleUrls: ["./location-admin-overview.component.scss"],
})
export class LocationAdminOverviewComponent implements OnInit {
  locations: Location[];
  page = 1;
  pageSize = 10;
  numberOfResults = 0;
  constructor(private locationsService: LocationsService, private router: Router, 
    private toastr: ToastrService) {}

  ngOnInit(): void {
    this.searchLocations();
  }
  navigateToLocation(location: Location) {
    this.router.navigate(["/", "locations", location.locationId]);
  }
  searchLocations() {
    this.locationsService.locationsGet(undefined, this.pageSize, this.page - 1).subscribe({
      next: (locationSearchResult) => {
        this.locations = locationSearchResult.locations;
        this.numberOfResults = locationSearchResult.numberOfResults;
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
  }
  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.searchLocations();
  }
}
