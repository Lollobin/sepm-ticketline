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
    return this.httpClient.get<Event[]>(this.eventServiceUri);
  }

  /**
   * Loads specific event from the backend
   *
   * @param id of event to load
   */
   getEventById(id: number): Observable<Event> {
    console.log('Load event details for ' + id);
    return this.httpClient.get<Event>(this.eventServiceUri + '/' + id);
  }

  /**
   * Persists event to the backend
   *
   * @param event to persist
   */
  createEvent(event: EventWithoutId): Observable<HttpResponse<Event>> {
    console.log('Create event with name ' + event.name);
    return this.httpClient.post<Event>(this.eventServiceUri, event, {observe: 'response'});
  }
}
