import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";
import {PasswordUpdate, UserManagementService} from "../../generated-sources/openapi";
import {passwordMatchValidator} from "../registration/passwords-match-validator";
import { ToastrService } from 'ngx-toastr';

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
              private userManagementService: UserManagementService, private router: Router,
              private toastr: ToastrService) {
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
    const passwordUpdate: PasswordUpdate = {
      newPassword: this.f.password.value,
      token: this.token
    };

    console.log(passwordUpdate);
    this.userManagementService.passwordUpdatePost(passwordUpdate).subscribe(
        {
          next: () => {
            this.submitted = true;
            this.success = "Successfully saved new password!";
            this.toastr.success(this.success);
          },
          error: (err) => {
            this.handleError(err);
            console.log(err);
            this.toastr.error(this.error);
          }
        }
    );
  }

  handleError(error){
    if (error?.status===422){
      this.error="Your password reset link seems to be invalid. Please request a new one.";
    } else {
      this.error="An error occurred processing your password reset, please try again later.";
    }
  }
}
