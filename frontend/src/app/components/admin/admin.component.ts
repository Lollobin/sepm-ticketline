import {Component} from '@angular/core';
import {CustomAuthService} from "../../services/custom-auth.service";
import {
  faAngleRight,
  faCompactDisc,
  faFileLines,
  faMapLocation,
  faUsers
} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent {
  arrow = faAngleRight;
  event = faCompactDisc;
  article = faFileLines;
  users = faUsers;
  location = faMapLocation;

  constructor(public authService: CustomAuthService) {
  }
}
