import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EventsService, Sector, ShowsService, SeatingPlansService, SectorPrice, 
  ShowWithoutId, LocationsService, LocationSearch, Location } from 'src/app/generated-sources/openapi';
import { faCircleQuestion } from "@fortawesome/free-solid-svg-icons";
import { CustomAuthService } from "../../services/custom-auth.service";
import { debounceTime, distinctUntilChanged, map, Observable, switchMap } from 'rxjs';

@Component({
  selector: 'app-create-show',
  templateUrl: './create-show.component.html',
  styleUrls: ['./create-show.component.scss']
})
export class CreateShowComponent implements OnInit {

  eventId: number;
  eventName: string;
  eventCategory: string;
  eventDuration: number;
  eventDescription: string;
  showWithoutId: ShowWithoutId = {
    date: "", event: 0,
    artists: [],
    seatingPlan: 0,
    sectorPrices: []
  };
  sectors: Sector[];
  showForm: any;
  sectorForm = this.formBuilder.group({
    ignore: ["", [Validators.required]]
  });
  sectorPrice: SectorPrice = { price: 0, sectorId: 0 };
  error = false;
  notFound = true;
  errorMessage = '';
  role = '';
  faCircleQuestion = faCircleQuestion;
  sectorString = "sector";
  gotFromSeatingPlan: number;
  locationSearchDto: LocationSearch = {};
  seatingPlans = [];

  constructor(private formBuilder: FormBuilder, private showService: ShowsService, private eventService: EventsService,
    private route: ActivatedRoute, private authService: CustomAuthService, private seatingPlansService: SeatingPlansService,
    private locationsService: LocationsService) { }

  get date() {
    return this.showForm.get("date");
  }

  get time() {
    return this.showForm.get("time");
  }

  get artists() {
    return this.showForm.get("artists");
  }

  get validForms() {
    return this.showForm.valid && this.sectorForm.valid;
  }

  get locationValid() {
    return this.showForm.get("location") && this.showForm.get("location").value.locationId;
  }

  get seatingPlan() {
    return this.showForm.get("seatingPlan");
  }

  ngOnInit(): void {
    this.showForm = this.formBuilder.group({
      date: ['', [Validators.required]],
      time: ['', [Validators.required]],
      event: [],
      location: ['', [Validators.required]],
      seatingPlan: ['', [Validators.required]],
      sectorPrices: []
    });
    this.route.params.subscribe(params => {
      this.eventId = params["id"];
      this.showForm.value.id = this.eventId;
      this.notFound = true;
      this.getDetails(this.eventId);
    });
    this.role = this.authService.getUserRole();
    this.showForm.get('location').valueChanges.subscribe(val => {
      if (val && val.locationId){
        this.getSeatingPlansOfLocation(val.locationId);
      }
    });
    this.showForm.get('seatingPlan').valueChanges.subscribe(val => {
      if (val && val.seatingPlanId){
        this.getSectorsOfSeatingPlan(val.seatingPlanId);
      }
    });
  }

  getDetails(id: number): void {
    this.eventService.eventsIdGet(this.eventId).subscribe({
      next: data => {
        console.log("Succesfully got event with id", id);
        this.eventName = data.name;
        this.eventCategory = data.category;
        this.eventDuration = data.duration;
        this.eventDescription = data.content;
        this.error = false;
        this.notFound = false;
      },
      error: error => {
        console.error('Error fetching event', error.message);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    });
  }

  createShow(): void {
    this.showForm.value.event = this.eventId;
    if (this.showForm.value.date.length !== 25) {
      this.showForm.value.date = this.showForm.value.date + "T" + this.showForm.value.time + ":00+00:00";
    }
    this.showWithoutId.date = this.showForm.value.date;
    this.showWithoutId.event = this.eventId;
    for (let i = 0; i < this.sectors.length; i++) {
      this.showWithoutId.sectorPrices[i] = { price: 0, sectorId: 0 };
      this.showWithoutId.sectorPrices[i].price = this.sectorForm.get("sector" + this.sectors[i].sectorId).value;
      this.showWithoutId.sectorPrices[i].sectorId = this.sectors[i].sectorId;
    }
    this.showWithoutId.artists = this.showForm.value.artists;
    this.showWithoutId.seatingPlan = this.showForm.value.seatingPlan;
    console.log("POST http://localhost:8080/shows " + JSON.stringify(this.showWithoutId));
    this.showService.showsPost(this.showWithoutId, 'response').subscribe({
      next: data => {
        console.log("Succesfully created show");
        console.log(data.headers.get('Location'));
        this.error = false;
        this.clearForm();
      },
      error: error => {
        console.log("Error creating event", error.message);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    });
  }

  secondsToHms(d): string {
    d = Number(d * 60);
    const h = Math.floor(d / 3600);
    const m = Math.floor(d % 3600 / 60);
    const s = Math.floor(d % 3600 % 60);
    const hDisplay = h > 0 ? h + (h === 1 ? " hour" : " hours") + (m > 0 || s > 0 ? ", " : "") : "";
    const mDisplay = m > 0 ? m + (m === 1 ? " minute" : " minutes") + (s > 0 ? ", " : "") : "";
    const sDisplay = s > 0 ? s + (s === 1 ? " second" : " seconds") : "";
    return hDisplay + mDisplay + sDisplay;
  }

  vanishError() {
    this.error = false;
  }

  clearForm() {
    this.showForm.reset();
    this.sectorForm.reset();
  }

  createGroup(sectors: Sector[]) {
    const group = this.formBuilder.group({});
    this.sectors.forEach(control => group.addControl("sector" + control.sectorId, this.formBuilder.control('', Validators.required)));
    return group;
  }

  getSectorsOfSeatingPlan(id: number) {
    if (id === null){
      return;
    }
    this.seatingPlansService.seatingPlansIdSectorsGet(id).subscribe({
      next: data => {
        console.log("Succesfully got sectors of seatingPlan with id", id);
        this.sectors = data;
        this.sectorForm = this.createGroup(data);
        this.error = false;
        this.gotFromSeatingPlan = id;
        this.showWithoutId.sectorPrices = [];
      },
      error: error => {
        console.log("Error getting sectors of seatingPlan with id", id);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    });
  }

  getSeatingPlansOfLocation(id: number) {
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
  )

  getLocationSearch(search: string) {
    this.locationSearchDto.name = search;
    return this.locationSearchDto;
  }

  locationFormatter(location: Location) {
    return location.name;
  }
}
