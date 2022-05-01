import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Show, ShowWithoutId } from '../generated-sources/openapi';
import { Globals } from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ShowService {

  private showServiceUri: string = this.globals.backendUri + '/shows';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Persists event to the backend
   *
   * @param event to persist
   */
   createEvent(show: ShowWithoutId): Observable<HttpResponse<Show>> {
    console.log('POST '+ this.showServiceUri + " " + show);
    return this.httpClient.post<Show>(this.showServiceUri, show, {observe: 'response'});
  }
}
