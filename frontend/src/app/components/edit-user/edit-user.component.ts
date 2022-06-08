import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Address, User, UserManagementService, UserWithPassword } from 'src/app/generated-sources/openapi';
import { passwordMatchValidator } from '../registration/passwords-match-validator';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {

  registrationForm: FormGroup;
  submitted=false;
  user: User;

  error = false;
  errorMessage = '';
  genders = [{description: "Female", value: "female"}, {
    description: "Male",
    value: "male"
  }, {description: "Other", value: "other"}];

  ngOnInit(): void {
    this.userManagementService.userInfoGet().subscribe({
      next: (next) => {
        this.user = next;
        console.log("Succesfully got user with id " + next.userId);
        this.error = false;
        this.registrationForm.value.firstName = this.user.firstName;
        this.registrationForm.value.lastName = this.user.lastName;
        this.registrationForm.value.email = this.user.email;
        this.registrationForm.value.address.houseNumber = this.user.address.houseNumber;
        this.registrationForm.value.address.street = this.user.address.street;
        this.registrationForm.value.address.zipCode = this.user.address.zipCode;
        this.registrationForm.value.address.city = this.user.address.city;
        this.registrationForm.value.address.country = this.user.address.country;
        this.registrationForm.value.gender = this.user.gender;
      },
      error: (error) => {
        console.error("Error getting user from authentication token", error);
        this.errorMessage = error;
        this.error = true;
      }
    });
  }

  constructor(private formBuilder: FormBuilder, private userManagementService: UserManagementService, private router: Router, userService: UserManagementService) {
    this.registrationForm = this.formBuilder.group({
          firstName: ["", [Validators.required]],
          lastName: ["", [Validators.required]],
          email: ["", [Validators.required, Validators.email]],
          address: this.formBuilder.group({
            houseNumber: ["", [Validators.required]],
            street: ["", [Validators.required]],
            zipCode: ["", [Validators.required]],
            city: ["", [Validators.required]],
            country: ["", [Validators.required]]
          }),
          gender: ["", [Validators.required]],
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
    this.submitted=true;
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
    this.userManagementService.usersPost(userWithPassword, ).subscribe({
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


  clearForm() {
    this.registrationForm.reset();
  }
}
