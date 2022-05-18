import {Injectable} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService,
              private router: Router) {
  }

  canActivate(
      next: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const callingUrl: string = state.url;
    return this.isUserAuthorized(next, callingUrl);
  }

  isUserAuthorized(route: ActivatedRouteSnapshot, callingUrl: any): boolean {
    if (this.authService.isLoggedIn()) {
      // user is logged in, now check if he has the correct rights,
      // if not, he will be redirected to homepage
      const providedRoles = this.authService.getUserRole();
      const requiredRoles = route.data.role;
      if (requiredRoles && requiredRoles.indexOf(providedRoles) === -1) {// -1 means not found
        this.router.navigate(['/']);
        return false;
      }
      return true;
    }
    // user is not even logged in -> redirect to login page
    // after successful login user will be redirected to the requested resource
    this.router.navigate(['/login'], {queryParams: {redirectUrl: callingUrl}});
    return false;
  }
}
