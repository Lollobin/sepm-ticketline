import {Component} from "@angular/core";
import {CustomAuthService} from "../../services/custom-auth.service";
import {faEye, faCircleInfo, faUser} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"]
})
export class HeaderComponent {
  eye = faEye;
  info = faCircleInfo;
  user = faUser;
  adminView = false;

  constructor(public authService: CustomAuthService) {
  }

  toggleAdminView() {
    this.adminView = !this.adminView;
  }

}
