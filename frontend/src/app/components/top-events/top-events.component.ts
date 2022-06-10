import { Component, OnInit } from '@angular/core';
import {EventsService} from "../../generated-sources/openapi";

@Component({
  selector: 'app-top-events',
  templateUrl: './top-events.component.html',
  styleUrls: ['./top-events.component.scss']
})
export class TopEventsComponent implements OnInit {

  constructor(private eventsService: EventsService) { }

  ngOnInit(): void {
  }

}
