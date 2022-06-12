import {Component, OnInit} from '@angular/core';
import {CustomAuthService} from '../../services/custom-auth.service';
import {faEye, faUser} from "@fortawesome/free-solid-svg-icons";
import { TicketsService } from 'src/app/generated-sources/openapi';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  eye = faEye;
  user = faUser;
  adminView=false;

  constructor(public authService: CustomAuthService) { }

  ngOnInit() {
  }

  toggleAdminView(){
    this.adminView= !this.adminView;
  }
  
}
