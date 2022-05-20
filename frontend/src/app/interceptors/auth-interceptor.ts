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
    const authUri = this.globals.backendUri + '/authentication';
    const signUpUri = this.globals.backendUri + '/users';
    const isLoggedIn = this.authService.isLoggedIn();

    // Do not intercept authentication / registration requests
    if ((req.url === authUri || req.url === signUpUri) && !isLoggedIn) {
      return next.handle(req);
    }

    const authReq = req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + this.authService.getToken())
    });

    return next.handle(authReq);
  }
}
