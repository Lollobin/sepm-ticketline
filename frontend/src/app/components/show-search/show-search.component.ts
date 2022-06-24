import {Component, OnInit} from '@angular/core';
import {
  Location,
  LocationSearch,
  LocationsService,
  SeatingPlan,
  SeatingPlansService,
  ShowSearch,
  ShowSearchResult,
  ShowsService
} from "../../generated-sources/openapi";
import {debounceTime, distinctUntilChanged, map, Observable, switchMap} from "rxjs";
import {FormBuilder} from "@angular/forms";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-show-search',
  templateUrl: './show-search.component.html',
  styleUrls: ['./show-search.component.scss']
})
export class ShowSearchComponent implements OnInit {

  shows: ShowSearchResult;
  page = 1;
  pageSize = 10;

  showForm;

  offsetTime;
  seatingPlans: SeatingPlan[];
  locationSearchDto: LocationSearch = {};


  currentlyActiveFilters: string[];

  constructor(private showService: ShowsService, private seatingPlansService: SeatingPlansService,
              private locationsService: LocationsService, private formBuilder: FormBuilder,
              private toastr: ToastrService) {

  }


  get location() {
    return this.showForm.get("location");
  }

  get price() {
    return this.showForm.get("price");
  }

  get time() {
    return this.showForm.get("time");
  }

  get date() {
    return this.showForm.get("date");
  }

  get seatingPlan() {
    return this.showForm.get("seatingPlan");
  }

  ngOnInit(): void {
    this.showForm = this.formBuilder.group({
      date: [null],
      time: [null],
      location: [null],
      seatingPlan: [null],
      price: [null]
    });
    this.onSearch();
    this.showForm.get('location').valueChanges.subscribe(val => {
      if (val && val.locationId) {
        this.getSeatingPlansOfLocation(val.locationId);
      } else {
        this.resetFilterOnField("seatingPlan");
      }
    });
  }


  onSearch() {
    if (this.showForm.value.date) {
      this.offsetTime = this.showForm.value.date;

      if (this.showForm.value.time) {
        this.offsetTime += "T" + this.showForm.value.time + ":00+00:00";
      } else {
        this.offsetTime += "T" + '00:00' + ":00+00:00";
      }
    } else {
      this.offsetTime = null;
    }
    const search: ShowSearch = {

      date: this.offsetTime,
      location: this.location.value ? this.location.value.locationId : null,
      seatingPlan: this.seatingPlan.value ? this.seatingPlan.value.seatingPlanId : null,
      price: this.price.value!== null || this.price.value === 0 ? this.price.value : null,
    };

    console.log(search);
    this.showService.showsGet(search, this.pageSize, this.page - 1).subscribe({
          next: response => {
            this.shows = response;
            this.setCurrentlyActiveFilters();
            console.log(this.shows);
            if (this.shows.numberOfResults === 0) {
              this.toastr.info("There aren't any shows fitting your input!");
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
        }
    );

  }

  getSeatingPlansOfLocation(id: number) {
    console.log("i should get " + id);
    if (id === null) {
      return;
    }
    this.locationsService.locationsIdSeatingPlansGet(id).subscribe({
      next: data => {
        console.log("Succesfully got seating plans of location with id", id);
        this.seatingPlans = data;
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

  locationSearch = (text$: Observable<string>) => text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      switchMap((search: string) => this.locationsService.locationsGet(this.getLocationSearch(search)).pipe(
              map(locationResult => locationResult.locations)
          )
      )
  );

  getLocationSearch(search: string) {
    this.locationSearchDto.name = search;
    return this.locationSearchDto;
  }

  locationFormatter(location: Location) {
    return location.name;
  }

  handleEventPageEmit(number) {
    this.page = number;
    this.onSearch();
  }

  setCurrentlyActiveFilters() {
    this.currentlyActiveFilters = [];
    if (this.location.value) {
      this.currentlyActiveFilters.push("location");
    }
    if (this.time.value) {
      this.currentlyActiveFilters.push("time");
    }
    if (this.date.value) {
      this.currentlyActiveFilters.push("date");
    }
    if (this.seatingPlan.value) {
      this.currentlyActiveFilters.push("seatingPlan");
    }
    if (this.price.value) {
      this.currentlyActiveFilters.push("price");
    }
  }

  resetFilterOnField(field: string) {
    if (field === 'date') {
      this.showForm.get('time').setValue(null);
    }
    this.showForm.get(field).setValue(null);
    this.onSearch();
  }

  resetAll() {
    this.showForm.reset();
    this.onSearch();
  }
}
