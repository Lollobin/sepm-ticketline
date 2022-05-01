import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Event } from 'src/app/generated-sources/openapi';
import { EventService } from 'src/app/services/event.service';

@Component({
  selector: 'app-create-event',
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent implements OnInit {

  constructor(private formBuilder: FormBuilder, private eventService: EventService) { }

  ngOnInit(): void {
  }

  categories: string[] = [
    "Classical", "Country", "EDM", "Jazz", "Oldies", "Pop", "Rap", "R&B", "Rock", "Techno"
  ];

  eventForm = this.formBuilder.group({
    name: [, Validators.required],
    category: [, Validators.required],
    duration: [120],
    description: []
  });

  secondsToHms(d): string {
    d = Number(d * 60);
    var h = Math.floor(d / 3600);
    var m = Math.floor(d % 3600 / 60);
    var s = Math.floor(d % 3600 % 60);
    var hDisplay = h > 0 ? h + (h == 1 ? " hour" : " hours") + (m > 0 || s > 0 ? ", " : "") : "";
    var mDisplay = m > 0 ? m + (m == 1 ? " minute" : " minutes") + (s > 0 ? ", " : "") : "";
    var sDisplay = s > 0 ? s + (s == 1 ? " second" : " seconds") : "";
    return hDisplay + mDisplay + sDisplay;
  };

  showForm(): string {
    var str = "name: " + this.eventForm.get("name").value + ",\n category: " + this.eventForm.get("category").value + ",\n duration: " +
      this.eventForm.get("duration").value + ",\n description: " + this.eventForm.get("description").value;
    console.log(str);
    return str;
  }

  createEvent(): void {
    this.eventService.createEvent(this.eventForm.value).subscribe(
      (res: HttpResponse<Event>) => {
        console.log(res.headers.get('Location'));
        console.log("Succesfully created event");
      },
      error => {
        console.log(error.message);
      }
    )
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

