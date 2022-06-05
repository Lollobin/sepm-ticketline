import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserManagementService} from "../../generated-sources/openapi";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent implements OnInit {
  token;
  clientUrl = window.location.host + '/passwordUpdate';
  error;
  successMessage: string;
  email;

  constructor(private router: Router, private userManagementService: UserManagementService) {
  }

  ngOnInit(): void {

    console.log(this.clientUrl);

  }

  sendRequest(formvalue) {
    console.log("sendwasclicked with val " + formvalue.email);
    console.log(this.clientUrl);
    const passwordReset = {
      email: formvalue.email,
      clientUrl: this.clientUrl
    };

    //this.userManagementService.passwordResetPost(passwordReset).subscribe(
    // {next: (response) => {this.successMessage=response.getBody()},
    // error: (err) => this.error=err}
//}
    // );
  }
}
