import {Component} from "@angular/core";
import {CustomAuthService} from "../../services/custom-auth.service";
import {faEye, faCircleInfo, faUser, faNewspaper, faSearch, faBasketShopping, 
  faUsers, faMapLocation, faCompactDisc , faFileLines} from "@fortawesome/free-solid-svg-icons";

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
  adminView = false;

  constructor(public authService: CustomAuthService) {
  }

  toggleAdminView() {
    this.adminView = !this.adminView;
  }

}
