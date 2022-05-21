import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EventsService, Sector, ShowsService, SeatingPlansService, SectorPrice, ShowWithoutId } from 'src/app/generated-sources/openapi';
import { faCircleQuestion } from "@fortawesome/free-solid-svg-icons";
import { CustomAuthService } from "../../services/custom-auth.service";

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

  constructor(private formBuilder: FormBuilder, private showService: ShowsService, private eventService: EventsService,
    private route: ActivatedRoute, private authService: CustomAuthService, private seatingPlansService: SeatingPlansService) { }

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

  ngOnInit(): void {
    this.showForm = this.formBuilder.group({
      date: ['', [Validators.required]],
      time: ['', [Validators.required]],
      event: [],
      seatingPlan: [1],
      sectorPrices: []
    });
    this.route.params.subscribe(params => {
      this.eventId = params["id"];
      this.showForm.value.id = this.eventId;
      this.notFound = true;
      this.getDetails(this.eventId);
    });
    this.role = this.authService.getUserRole();
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
    console.log(this.showForm.artists);
    console.log(this.showForm.seatingPlan);
    console.log("POST http://localhost:8080/shows " + JSON.stringify(this.showWithoutId));
    this.showService.showsPost(this.showWithoutId, 'response').subscribe({
      next: data => {
        console.log("Succesfully created show");
        console.log(data.headers.get('Location'));
        this.error = false;
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
  }

  createGroup(sectors: Sector[]) {
    const group = this.formBuilder.group({});
    console.log(JSON.stringify(this.sectors));
    this.sectors.forEach(control => group.addControl("sector" + control.sectorId, this.formBuilder.control('', Validators.required)));
    return group;
  }

  getSectorsOfSeatingPlan(id: number) {
    this.seatingPlansService.seatingPlansIdSectorsGet(id).subscribe({
      next: data => {
        console.log("Succesfully got sectors of seatingPlan with id", id);
        this.sectors = data;
        this.sectorForm = this.createGroup(data);
        this.error = false;
        this.gotFromSeatingPlan = id;
        console.log(JSON.stringify(this.sectors));
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

  fillFormSeatingPlan() {
    if (this.showForm.value.seatingPlan === 2) {
      this.showForm.value.seatingPlan = 1;
    } else {
      this.showForm.value.seatingPlan = 2;
    }
  }
}
