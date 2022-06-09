import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserManagementService } from 'src/app/generated-sources/openapi';
import { CustomAuthService } from '../../services/custom-auth.service';
import { faArrowsRotate } from "@fortawesome/free-solid-svg-icons";
import { AuthRequest } from 'src/app/dtos/auth-request';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {

  registrationForm: FormGroup;
  passwordForm: FormGroup;
  submitted = false;
  user = {
    firstName: "", lastName: "", email: "", gender: "",
    address: { houseNumber: "", street: "", zipCode: "", city: "", country: "" }
  }
  error = false;
  errorMessage = '';
  success = false;
  genders = [{ description: "Female", value: "female" }, {
    description: "Male",
    value: "male"
  }, { description: "Other", value: "other" }];
  faArrowsRotate = faArrowsRotate;
  display = "none";
  action = "none";

  constructor(private formBuilder: FormBuilder, private authService: CustomAuthService, 
    private userManagementService: UserManagementService, private router: Router) {
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
      gender: ["", [Validators.required]]
    }
    );
    this.passwordForm = this.formBuilder.group({
      password: ["", [Validators.required, Validators.minLength(8)]]
    })
  }

  get f() {
    return this.registrationForm.controls;
  }

  get p() {
    return this.passwordForm.controls;
  }

  ngOnInit(): void {
    this.getUserInfo();
  }

  getUserInfo() {
    this.userManagementService.userInfoGet().subscribe({
      next: (next) => {
        this.user = next;
        console.log("Succesfully got user with id " + next.userId);
        this.error = false;
        this.registrationForm.controls['firstName'].setValue(this.user.firstName);
        this.registrationForm.controls['lastName'].setValue(this.user.lastName);
        this.registrationForm.controls['gender'].setValue(this.user.gender);
        this.registrationForm.controls['email'].setValue(this.user.email);
        const address = {
          houseNumber: this.user.address.houseNumber,
          street: this.user.address.street,
          zipCode: this.user.address.zipCode,
          city: this.user.address.city,
          country: this.user.address.country
        };
        this.registrationForm.controls['address'].patchValue(address);
        this.error = false;
      },
      error: (error) => {
        console.error("Error getting user from authentication token");
        this.error = true;
        this.success = false;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    });
  }

  putUser() {
    let user = {
      firstName: this.registrationForm.value.firstName,
      lastName: this.registrationForm.value.lastName,
      gender: this.registrationForm.value.gender,
      email: this.registrationForm.value.email,
      address: {
        houseNumber: this.registrationForm.value.address.houseNumber,
        street: this.registrationForm.value.address.street,
        zipCode: this.registrationForm.value.address.zipCode,
        city: this.registrationForm.value.address.city,
        country: this.registrationForm.value.address.country
      },
      password: this.passwordForm.value.password
    }
    this.userManagementService.usersPut(user).subscribe({
      next: (next) => {
        console.log("Succesfully updated user information");
        this.reloadToken();
        this.error = false;
        this.success = true;
      }, 
      error: (error) => {
        console.error("Error putting user from authentication token");
        this.error = true;
        this.success = false;
        if (error.error != null && typeof error.error === 'object') {
          console.log(error.error);
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    })
  }

  openModal() {
    this.display = "block";
  }

  onCloseHandled() {
    this.display = "none";
  }

  vanishError() {
    this.error = false;
  }

  vanishSuccess() {
    this.success = false;
  }

  clearForm() {
    this.registrationForm.reset();
  }

  setAction(text: string) {
    if (text == "edit" || text == "delete") {
      this.action = text;
    } else {
      this.action = "none";
    }
  }

  confirm() {
    if (this.action === "edit"){
      this.putUser();
    } else if (this.action === "delete") {

    }
  }

   confirmUser() {
    this.submitted = true;
    if (this.passwordForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(this.user.email, this.passwordForm.controls.password.value);
      console.log(this.user.email);
      this.authenticateUser(authRequest);
    } else {
      console.log('Invalid input');
    }
  }

  authenticateUser(authRequest: AuthRequest) {
    console.log('Try to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        this.confirm();
        console.log('Successfully confirmed user: ' + authRequest.email);
      },
      error: error => {
        console.log('Could not log in due to:');
        console.log(error);
        this.error = true;
        this.success = false;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    });
  }

  reloadToken() {
    const authRequest: AuthRequest = new AuthRequest(this.registrationForm.controls.email.value, this.passwordForm.controls.password.value);
    this.passwordForm.reset();
    console.log('Try to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        console.log('Successfully confirmed user: ' + authRequest.email);
        this.getUserInfo();
      },
      error: error => {
        console.log('Could not log in due to:');
        console.log(error);
        this.error = true;
        this.success = false;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    });
  }
}
