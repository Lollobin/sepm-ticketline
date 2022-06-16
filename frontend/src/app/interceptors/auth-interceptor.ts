import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {CustomAuthService} from "../services/custom-auth.service";
import {Observable} from "rxjs";
import {Globals} from "../global/globals";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: CustomAuthService, private globals: Globals) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isLoggedIn = this.authService.isLoggedIn();

    // auth and registration
    const authUri = this.globals.backendCustomUri + "/login";
    const signUpUri = this.globals.backendCustomUri + "/users";
    const passwordResetUri = this.globals.backendCustomUri + "/passwordReset";
    const passwordUpdateUri = this.globals.backendCustomUri + "/passwordUpdate";
    const listOfGetPublics = [
      this.globals.backendCustomUri + "/events",
      this.globals.backendCustomUri + "/artists",
      this.globals.backendCustomUri + "/locations",
      this.globals.backendCustomUri + "/shows",
      this.globals.backendCustomUri + "/seatingPlan",
      this.globals.backendCustomUri + "/topEvents",
      this.globals.backendCustomUri + '/articles',
      this.globals.backendCustomUri + '/images'
    ];

    const isget = req.method === "GET";
    let publicResource;
    let i = 0;
    while (!publicResource && i < listOfGetPublics.length) {
      publicResource = req.url.startsWith(listOfGetPublics[i]);
      i++;
    }
    const dontInterceptPublic = isget && publicResource;

    // Do not intercept authentication / registration requests
    if ((req.url === authUri || req.url === signUpUri || dontInterceptPublic
            || req.url === passwordResetUri || req.url === passwordUpdateUri)
        && !isLoggedIn) {

      return next.handle(req);
    }

    const authReq = req.clone({
      headers: req.headers.set("Authorization", "Bearer " + this.authService.getToken())
    });

    return next.handle(authReq);
  }
}
