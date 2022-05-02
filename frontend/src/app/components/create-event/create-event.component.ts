import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Event } from 'src/app/generated-sources/openapi';
import { EventService } from 'src/app/services/event.service';


@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent implements OnInit {

  categories: string[];
  eventForm: any;
  
  constructor(private formBuilder: FormBuilder, private eventService: EventService, private router: Router) { }

  ngOnInit(): void {
    this.categories = [
      "Classical", "Country", "EDM", "Jazz", "Oldies", "Pop", "Rap", "R&B", "Rock", "Techno"
    ];
    this.eventForm = this.formBuilder.group({
      name: ['',[Validators.required]],
      category: ['',[Validators.required]],
      duration: [120],
      description: []
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

  createEvent(): void {
    this.eventService.createEvent(this.eventForm.value).subscribe(
      (res: HttpResponse<Event>) => {
        const location = res.headers.get('Location');
        console.log("Succesfully created event");
        console.log(location);
        const id = location.split("/").pop();
        this.router.navigateByUrl("/events/"+id+"/shows");
      },
      error => {
        console.log(error.message);
      }
    );
  }

  get name(){
  return this.eventForm.get("name");
}

  get category(){
  return this.eventForm.get("category");
}

  get duration(){
  return this.eventForm.get("duration");
}

  get description(){
  return this.eventForm.get("description");
}
}

