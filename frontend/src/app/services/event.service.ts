import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {EventWithoutId} from 'src/app/generated-sources/openapi/';
import {Globals} from '../global/globals';
import { Observable } from 'rxjs';
import { Event } from '../generated-sources/openapi/model/event';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventServiceUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all events from the backend
   */
   getEvent(): Observable<Event[]> {
    console.log('GET ' + this.eventServiceUri);
    return this.httpClient.get<Event[]>(this.eventServiceUri);
  }

  /**
   * Loads specific event from the backend
   *
   * @param id of event to be loaded
   */
   getEventById(id: number): Observable<Event> {
    console.log('GET ' + this.eventServiceUri + "/" + id);
    return this.httpClient.get<Event>(this.eventServiceUri + '/' + id);
  }

  /**
   * Creates event in backend
   *
   * @param event to be created
   */
  createEvent(event: EventWithoutId): Observable<HttpResponse<Event>> {
    console.log('POST ' + this.eventServiceUri + " " + JSON.stringify(event));
    return this.httpClient.post<Event>(this.eventServiceUri, event, {observe: 'response'});
  }
}
