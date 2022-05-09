import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EventsService, Show, ShowsService } from 'src/app/generated-sources/openapi';

@Component({
  selector: 'app-create-show',
  templateUrl: './create-show.component.html',
  styleUrls: ['./create-show.component.scss']
})
export class CreateShowComponent implements OnInit {

  id: number;
  eventName: string;
  showForm: any;
  error = false;
  errorMessage = '';

  constructor(private formBuilder: FormBuilder, private showService: ShowsService, private eventService: EventsService,
    private route: ActivatedRoute) { }

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
      this.getDetails(this.id);
    });
  }

  getDetails(id: number): void {
    this.eventService.eventsIdGet(this.id).subscribe({
      next: data => {
        console.log("got event with id ", id);
        this.eventName = data.name;
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
    this.showForm.value.date = this.showForm.value.date + "T" + this.showForm.value.time + ":00+00:00";
    this.showService.showsPost(this.showForm.value, 'response').subscribe(
      (res: HttpResponse<Show>) => {
        console.log("Succesfully created show");
        console.log(res.headers.get('Location'));
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

  vanishError() {
    this.error = false;
  }

  clearForm() {
    this.showForm.reset();
  }
}
