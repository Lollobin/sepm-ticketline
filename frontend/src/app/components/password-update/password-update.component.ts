import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";
import {UserManagementService} from "../../generated-sources/openapi";
import {passwordMatchValidator} from "../registration/passwords-match-validator";

@Component({
  selector: 'app-password-update',
  templateUrl: './password-update.component.html',
  styleUrls: ['./password-update.component.scss']
})
export class PasswordUpdateComponent implements OnInit {
  token;
  error;
  passwordUpdateForm;
  submitted = false;
  user;
  success;

  constructor(private route: ActivatedRoute, private formBuilder: FormBuilder,
              private userManagementService: UserManagementService, private router: Router) {
    this.passwordUpdateForm = this.formBuilder.group({
          password: ['', [Validators.required, Validators.minLength(8)]],
          confirmPassword: ['', [Validators.required, Validators.minLength(8)]],

        }, {
          validators: passwordMatchValidator
        }
    );
  }

  get f() {
    return this.passwordUpdateForm.controls;
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(
        params => {
          console.log(params);
          this.token = params.token;
        });
  }

  updatePassword() {
    const passwordUpdate = {
      newPassword: this.f.password,
      token: this.token
    };

    console.log("passwordupdate was created");
/*    this.userManagementService.passwordResetIdPost(passwordUpdate).subscribe(
        {
          next: (success) => this.success = success,
          error: (err) => this.error = err
        }
    );*/
  }
}
