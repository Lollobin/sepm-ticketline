import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {PasswordReset, UserManagementService} from "../../generated-sources/openapi";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent implements OnInit {

  clientUrl = 'http://' + window.location.host + '#/passwordUpdate';
  error;
  successMessage: string;
  email;
  submitted=false;

  constructor(private router: Router, private userManagementService: UserManagementService) {
  }

  ngOnInit(): void {

  }

  sendRequest(formvalue) {

    const passwordReset: PasswordReset = {
      email: formvalue.email,
      clientURI: this.clientUrl
    };
    console.log("sending");
    this.userManagementService.passwordResetPost(passwordReset, 'body', true).subscribe(
        {
          next: (response) => {
            this.submitted=true;
            console.log(response);
            this.successMessage = response;
          },
          error: (err) => this.error = err
        }
    );
  }
  vanishSuccess(){
    this.successMessage=null;
  }
  vanishError(){
    this.error=null;
  }
}
