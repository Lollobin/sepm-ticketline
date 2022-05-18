import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {faEye} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  eye = faEye;
  adminView=false;

  constructor(public authService: AuthService) {
  }

  ngOnInit() {
  }

  toggleAdminView(){
    this.adminView= !this.adminView;
  }
}
