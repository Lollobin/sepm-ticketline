import {Component} from '@angular/core';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss']
})
export class EventsComponent{

  data: any;

  artistSearch = true;
  locationSearch: boolean;
  eventSearch: boolean;
  showSearch: boolean;


  setAllFalseExcept(given: string) {
    this.artistSearch = 'artist' === given;
    this.eventSearch = 'event' === given;
    this.locationSearch = 'location' === given;
    this.showSearch = 'show' === given;
  }
}
