import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EventsService, Show, ShowsService } from 'src/app/generated-sources/openapi';
import { faCircleQuestion } from "@fortawesome/free-solid-svg-icons";
import {CustomAuthService} from "../../services/custom-auth.service";

@Component({
  selector: 'app-create-show',
  templateUrl: './create-show.component.html',
  styleUrls: ['./create-show.component.scss']
})
export class CreateShowComponent implements OnInit {

  id: number;
  eventName: string;
  eventCategory: string;
  eventDuration: number;
  eventDescription: string;
  showForm: any;
  error = false;
  notFound = true;
  errorMessage = '';
  role = '';
  faCircleQuestion = faCircleQuestion;

  constructor(private formBuilder: FormBuilder, private showService: ShowsService, private eventService: EventsService,
    private route: ActivatedRoute, private authService: CustomAuthService) { }

  get date() {
    return this.showForm.get("date");
  }

  get time() {
    return this.showForm.get("time");
  }

  get artists() {
    return this.showForm.get("artists");
  }

  ngOnInit(): void {
    this.showForm = this.formBuilder.group({
      date: ['', [Validators.required]],
      time: ['', [Validators.required]],
      event: []

    });
    this.route.params.subscribe(params => {
      this.id = params["id"];
      this.showForm.value.id = this.id;
      console.log(this.id);
      this.notFound = true;
      this.getDetails(this.id);
    });
    this.role = this.authService.getUserRole();
  }

  getDetails(id: number): void {
    this.eventService.eventsIdGet(this.id).subscribe({
      next: data => {
        console.log("got event with id ", id);
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
    this.showForm.value.event = this.id;
    if (this.showForm.value.date.length !== 25){
      this.showForm.value.date = this.showForm.value.date + "T" + this.showForm.value.time + ":00+00:00";
    }
    console.log("POST http://localhost:8080/shows " + JSON.stringify(this.showForm.value));
    this.showService.showsPost(this.showForm.value, 'response').subscribe(
      (res: HttpResponse<Show>) => {
        console.log("Succesfully created show");
        console.log(res.headers.get('Location'));
        this.error = false;
      },
      error => {
        console.log("Error creating event", error.message);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    );
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
}
