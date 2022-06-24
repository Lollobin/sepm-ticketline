import {Component} from "@angular/core";
import {CustomAuthService} from "../../services/custom-auth.service";
import {
  faEye, faCircleInfo, faUser, faNewspaper, faSearch, faBasketShopping,
  faUsers, faMapLocation, faCompactDisc, faFileLines, faDashboard
} from "@fortawesome/free-solid-svg-icons";
import {Router} from "@angular/router";

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

  currentRoute: string;
  adminRoutes = ["/admin", "/article/create", "/users", "/locations", "/events/create"];

  constructor(public authService: CustomAuthService,
              private router: Router) {
  }

  onAdminRoute(): boolean {
    console.log(this.router.url);
    const value = this.adminRoutes.some(route => this.router.url.startsWith(route));
    console.log(value);
    return value;
  }

}
