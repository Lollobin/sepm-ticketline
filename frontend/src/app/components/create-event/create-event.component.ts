import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Event, EventsService } from 'src/app/generated-sources/openapi';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent implements OnInit {

  categories: string[];
  eventForm: any;
  error = false;
  errorMessage = '';
  role = '';

  constructor(private formBuilder: FormBuilder, private eventService: EventsService, private router: Router, 
    private authService: AuthService) { }

  get name() {
    return this.eventForm.get("name");
  }

  get category() {
    return this.eventForm.get("category");
  }

  get duration() {
    return this.eventForm.get("duration");
  }

  get description() {
    return this.eventForm.get("description");
  }

  ngOnInit(): void {
    this.categories = [
      "Classical", "Country", "EDM", "Jazz", "Oldies", "Pop", "Rap", "R&B", "Rock", "Techno"
    ];
    this.eventForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(255)]],
      category: ['', [Validators.required, Validators.maxLength(255)]],
      duration: [120, [Validators.min(10), Validators.max(360)]],
      description: ['']
    });
    this.role = this.authService.getUserRole();
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

  createEvent(): void {
    this.eventService.eventsPost(this.eventForm.value, 'response').subscribe(
      (res: HttpResponse<Event>) => {
        const location = res.headers.get('Location');
        console.log("Succesfully created event");
        console.log(location);
        const id = location.split("/").pop();
        this.router.navigateByUrl("/events/" + id + "/shows");
        this.error = false;
      },
      error => {
        console.log(error.message);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    );
  }

  vanishError() {
    this.error = false;
  }

  clearForm() {
    this.eventForm.reset();
  }
}

