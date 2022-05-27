import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {CustomAuthService} from '../services/custom-auth.service';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: CustomAuthService, private globals: Globals) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isLoggedIn = this.authService.isLoggedIn();

    //auth and registration
    const authUri = this.globals.backendCustomUri + '/login';
    const signUpUri = this.globals.backendCustomUri + '/users';

    const listOfGetPublics = [
      this.globals.backendCustomUri + '/events',
      this.globals.backendCustomUri + '/artists',
      this.globals.backendCustomUri + '/shows',
      this.globals.backendCustomUri + '/seatingPlan'];


    const dontInterceptPublic = req.method === "GET" && listOfGetPublics.indexOf(req.url) !== -1;

    // Do not intercept authentication / registration requests
    if ((req.url === authUri || req.url === signUpUri || dontInterceptPublic) && !isLoggedIn) {

      return next.handle(req);
    }

    const authReq = req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + this.authService.getToken())
    });

    return next.handle(authReq);
  }
}
