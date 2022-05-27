import {Component, OnInit} from '@angular/core';
import {
  Location,
  LocationSearch,
  LocationSearchResult,
  LocationsService, ShowsService
} from "../../generated-sources/openapi";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-location-search',
  templateUrl: './location-search.component.html',
  styleUrls: ['./location-search.component.scss']
})
export class LocationSearchComponent implements OnInit {

  page = 1;
  pageSize = 10;
  locationResult: LocationSearchResult;
  locationForm: FormGroup;
  err;
  locations: Location[];
  numberOfResult: number;

  constructor(private _formBuilder: FormBuilder, private locationService: LocationsService, private showService: ShowsService) {
    this.locationForm = this._formBuilder.group({
      name: [],
      city: [],
      country: [],
      street: [],
      zip: []
    });
  }


  get name() {
    return this.locationForm.get("name");
  }

  get city() {
    return this.locationForm.get("city");
  }

  get country() {
    return this.locationForm.get("country");
  }

  get street() {
    return this.locationForm.get("street");
  }

  get zip() {
    return this.locationForm.get("zip");
  }

  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.onSearch();
  }


  ngOnInit(): void {
  }

  onSearch() {
    const search: LocationSearch = {
      name: this.name.value,
      city: this.city.value,
      country: this.country.value,
      street: this.street.value,
      zipCode: this.zip.value
    };

    console.log(search);

    this.locationService.locationsGet(search, this.pageSize, this.page-1).subscribe({
      next: locationResult => {
        this.locationResult = locationResult;
        console.log(locationResult.numberOfResults);
        this.numberOfResult = locationResult.numberOfResults;
        this.locations = locationResult.locations;
      },
      error: err => {
        this.err = err;
      }
    });
  }

  loadShowsOfLocation(locationId: number) {

  }
}
