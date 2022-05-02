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
   * Adds show to event in backend
   *
   * @param show to be created
   */
   createEvent(show: ShowWithoutId): Observable<HttpResponse<Show>> {
    console.log('POST '+ this.showServiceUri + " " + JSON.stringify(show));
    return this.httpClient.post<Show>(this.showServiceUri, show, {observe: 'response'});
  }
}
