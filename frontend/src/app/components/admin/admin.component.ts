import { Component, OnInit } from '@angular/core';
import {CustomAuthService} from "../../services/custom-auth.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  constructor(public authService: CustomAuthService) { }

  ngOnInit(): void {
  }

}
