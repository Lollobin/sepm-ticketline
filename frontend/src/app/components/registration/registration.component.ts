import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Address, UserManagementService, UserWithPassword} from "../../generated-sources/openapi";
import {passwordMatchValidator} from "./passwords-match-validator";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {

  registrationForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';
  genders = [{description: "Female", value: "female"}, {
    description: "Male",
    value: "male"
  }, {description: "Other", value: "other"}];

  constructor(private formBuilder: FormBuilder, private userManagementService: UserManagementService, private router: Router) {
    this.registrationForm = this.formBuilder.group({
          firstName: ['', [Validators.required]],
          lastName: ['', [Validators.required]],
          email: ['', [Validators.required, Validators.email]],
          address: this.formBuilder.group({
            houseNumber: ['', [Validators.required]],
            street: ['', [Validators.required]],
            zipCode: ['', [Validators.required]],
            city: ['', [Validators.required]],
            country: ['', [Validators.required]]
          }),
          gender: ['', [Validators.required]],
          password: ['', [Validators.required, Validators.minLength(8)]],
          confirmPassword: ['', [Validators.required, Validators.minLength(8)]],

        }, {
          validators: passwordMatchValidator
        }
    );
  }

  get f() {
    return this.registrationForm.controls;
  }


  signUpUser() {
    this.submitted = true;
    if (this.registrationForm.valid) {
      const userAddress: Address= {
        houseNumber: this.f['address'].value.houseNumber,
        street: this.f['address'].value.street,
        zipCode: this.f['address'].value.zipCode,
        city: this.f['address'].value.city,
        country: this.f['address'].value.country
      };
      const userWithPassword: UserWithPassword = {
        firstName: this.f.firstName.value,
        lastName: this.f.lastName.value,
        email: this.f.email.value,
        gender: this.f.gender.value,
        address: userAddress,
        password: this.f.password.value
      };
      this.registerUser(userWithPassword);
    } else {
      console.log('Invalid input');


    }
  }

  registerUser(userWithPassword: UserWithPassword) {
    this.userManagementService.usersPost(userWithPassword).subscribe({
      next: () => {
        console.log("success!");
        this.router.navigate(['/login']);
      },
      error: error => {
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    });
  }

  vanishError() {
    this.error = false;
  }


}
