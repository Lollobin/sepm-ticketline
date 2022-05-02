import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventsService } from 'src/app/generated-sources/openapi';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.scss']
})
export class EventDetailsComponent implements OnInit {
  eventId: number;
  name: string;
  duration: number;
  category: string;
  content: string;

  constructor(private router: Router, private route: ActivatedRoute, private service: EventsService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.eventId = params["id"];
      this.getDetails(this.eventId);
    });
  }

  getDetails(id: number): void {
    this.service.eventsIdGet(this.eventId).subscribe({
      next: data => {
        console.log("got event with id ", id);
        this.eventId = id;
        this.name = data.name;
        this.duration = data.duration;
        this.category = data.category;
        this.content = data.content;
      },
      error: error => {
        console.error('Error fetching event', error.message);
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
  };

  addShows(id: number): void {
    this.router.navigateByUrl("/events/"+this.eventId+"/shows");
  }
}
