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

@Component({
  selector: 'app-show-search',
  templateUrl: './show-search.component.html',
  styleUrls: ['./show-search.component.scss']
})
export class ShowSearchComponent implements OnInit {

  error;
  errorMessage;

  shows: ShowSearchResult;
  page = 1;
  pageSize = 10;

  showForm;


  seatingPlans: SeatingPlan[];
  locationSearchDto: LocationSearch = {};


  constructor(private showService: ShowsService, private seatingPlansService: SeatingPlansService,
              private locationsService: LocationsService, private formBuilder: FormBuilder) {

  }

  get f() {
    return this.showForm.controls;
  }


  ngOnInit(): void {
    this.showForm = this.formBuilder.group({
      date: [null],
      time: [null],
      location: [null],
      seatingPlan: [null],
      price: [null]
    });

    this.showForm.get('location').valueChanges.subscribe(val => {
      if (val && val.locationId) {
        this.getSeatingPlansOfLocation(val.locationId);
      }
    });
  }


  onSearch() {
    if (this.showForm.value.date.length !== 25) {
      this.showForm.value.date = this.showForm.value.date + "T" + this.showForm.value.time + ":00+00:00";
    }
    const search: ShowSearch = {
      date: this.f.date.value ? this.f.date.value : null,
      location: this.f.location.value ? this.f.location.value : null,
      seatingPlan: this.f.seatingPlan.value ? this.f.seatingPlan.value : null,
      price: this.f.price.value ? this.f.price.value : null,
    };

    this.showService.showsGet(search, this.pageSize, this.page - 1).subscribe({
          next: response => {
            this.shows = response;
          },
          error: err => this.error = err
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
        this.error = false;
      },
      error: error => {
        console.log("Error getting seating plans of location with id", id);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
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

  handleEventPageEmit(number){
    this.page=number;
    this.onSearch();
  }
}
