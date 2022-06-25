import {Component, OnInit} from '@angular/core';
import {
  Location,
  LocationSearch,
  LocationSearchResult,
  LocationsService, ShowSearch, ShowSearchResult, ShowsService
} from "../../generated-sources/openapi";
import {FormBuilder, FormGroup} from "@angular/forms";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-location-search',
  templateUrl: './location-search.component.html',
  styleUrls: ['./location-search.component.scss']
})
export class LocationSearchComponent implements OnInit {

  page = 1;
  pageSize = 9;
  locationResult: LocationSearchResult = null;
  locationForm: FormGroup;
  locations: Location[];
  numberOfResult: number;
  showOfClickedLocation: ShowSearchResult = null;
  clickedLocation: Location = null;
  showPage: 1;

  currentlyActiveFilters: string[];

  constructor(private _formBuilder: FormBuilder, private locationService: LocationsService, private showService: ShowsService, 
    private toastr: ToastrService) {
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
    this.onSearch();

  }


  onSearch() {

    this.showOfClickedLocation = null;
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
        console.log(locationResult);
        this.locationResult = locationResult;
        this.numberOfResult = locationResult.numberOfResults;
        this.locations = locationResult.locations;
        this.setCurrentlyActiveFilters();
        if (!this.locationResult?.numberOfResults) {
          this.toastr.info("There are no locations fitting your input!");
        }
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

  loadShowsOfLocation(location: Location, pageNum: number) {
    this.clickedLocation = location;
    const search: ShowSearch = {
      location: location.locationId,
    };
      this.showService.showsGet(search, 10,pageNum-1).subscribe({
        next: value => {
          this.showOfClickedLocation = value;
          console.log(value);
          if (!this.showOfClickedLocation?.numberOfResults) {
            this.toastr.info("There are no shows at " + this.clickedLocation.name + "!");
          }
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

  handleEventPageEmit(number) {
    this.showPage = number;
    this.loadShowsOfLocation(this.clickedLocation, this.showPage);
  }

  scroll(el: HTMLElement) {
    el.scrollIntoView({behavior: "smooth"});
    
  }

  setCurrentlyActiveFilters(){
    this.currentlyActiveFilters=[];
    if(this.name.value){
      this.currentlyActiveFilters.push("name");
    }
    if(this.street.value){
      this.currentlyActiveFilters.push("street");
    }
    if(this.country.value){
      this.currentlyActiveFilters.push("country");
    }
    if(this.city.value){
      this.currentlyActiveFilters.push("city");
    }
    if(this.zip.value){
      this.currentlyActiveFilters.push("zip");
    }
  }

  resetFilterOnField(field: string){
    this.locationForm.get(field).setValue(null);
    this.onSearch();
  }

  resetAll(){
    this.locationForm.reset();
    this.onSearch();
  }
}
