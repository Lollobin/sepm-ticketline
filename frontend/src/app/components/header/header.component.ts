import {Component} from "@angular/core";
import {CustomAuthService} from "../../services/custom-auth.service";
import {
  faBasketShopping,
  faCircleInfo,
  faCompactDisc,
  faDashboard,
  faEye,
  faFileLines,
  faMapLocation,
  faNewspaper,
  faSearch,
  faUser,
  faUsers
} from "@fortawesome/free-solid-svg-icons";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"]
})
export class HeaderComponent {
  eye = faEye;
  info = faCircleInfo;
  user = faUser;
  newspaper = faNewspaper;
  search = faSearch;
  basket = faBasketShopping;
  users = faUsers;
  location = faMapLocation;
  event = faCompactDisc;
  article = faFileLines;
  admin = faDashboard;

  showAdminMenu = false;

  currentRoute: string;
  adminRoutesRegEx = [
    /^\/events\/[0-9]*\/shows\/create$/g,
    /^\/article\/create$/g,
    /^\/admin$/g,
    /^\/users$/g,
    /^\/users\/create$/g,
    /^\/locations$/g,
    /^\/locations\/create$/g,
    /^\/locations\/[0-9]*$/g,
    /^\/locations\/[0-9]*\/seatingPlans\/create$/g,
    /^\/events\/create$/g];

  constructor(public authService: CustomAuthService,
              private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showAdminMenu = this.onAdminRoute();
      }
    });
  }


  onAdminRoute(): boolean {
    const url = this.router.url;
    const value = this.adminRoutesRegEx.some(route => url.match(route) != null);
    console.log(url + " Admin menu: " + value);
    return value;
  }

}
