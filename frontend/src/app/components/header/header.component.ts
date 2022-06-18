import {Component, OnInit} from '@angular/core';
import {CustomAuthService} from '../../services/custom-auth.service';
import {faEye, faCircleInfo,faUser} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  eye = faEye;
  info= faCircleInfo;
  user = faUser;
  adminView=false;

  constructor(public authService: CustomAuthService) { }

  ngOnInit() {
  }

  toggleAdminView(){
    this.adminView= !this.adminView;
  }
  
}
