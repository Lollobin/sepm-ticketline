import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Address, PasswordReset, UserManagementService, UserWithPassword } from 'src/app/generated-sources/openapi';
import { faUserShield } from '@fortawesome/free-solid-svg-icons';
import { CustomAuthService } from 'src/app/services/custom-auth.service';
import { AuthRequest } from 'src/app/dtos/auth-request';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent implements OnInit {

  registrationForm: FormGroup;
  passwordForm: FormGroup;
  submitted = false;
  faUserShield = faUserShield;
  error = false;
  success = false;
  successUser = "";
  errorMessage = '';
  genders = [{ description: "Female", value: "female" }, {
    description: "Male",
    value: "male"
  }, { description: "Other", value: "other" }];
  userOrAdmin = "user";
  createdAccount = "";
  display = "none";
  adminMail = "";
  authenticated: boolean;
  clientUrl = 'http://' + window.location.host + '#/passwordUpdate';

  constructor(private formBuilder: FormBuilder, private userManagementService: UserManagementService, 
    private router: Router, private authService: CustomAuthService) {
      
    this.getAdminInfo();
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
      isAdmin: []
    });

    this.passwordForm = this.formBuilder.group({
      password: ["", [Validators.required, Validators.minLength(8)]]
    });

    this.f['isAdmin'].valueChanges.subscribe(val => {
      if (val) {
        this.userOrAdmin = "admin";
      } else {
        this.userOrAdmin = "user";
      }
    });
  }

  get f() {
    return this.registrationForm.controls;
  }

  get p() {
    return this.passwordForm.controls;
  }

  ngOnInit(): void {
  }

  signUpUser() {
    if (this.registrationForm.valid) {
      const uuid = require("uuid");
      const id = uuid.v4();
      const userAddress: Address = {
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
        password: id
      };
      if (this.f['isAdmin'].value) {
        this.registerAdmin(userWithPassword);
      } else {
        this.registerUser(userWithPassword);
      }
    }
  }

  registerUser(userWithPassword: UserWithPassword) {
    this.userManagementService.usersPost(userWithPassword).subscribe({
      next: () => {
        console.log("success!");
        this.success = true;
        this.successUser = this.f['email'].value;
        this.sendPasswordReset(this.f['email'].value);
        this.error = false;
        this.clearForm();
        this.registrationForm.markAsPristine();
        this.registrationForm.markAsUntouched();
        this.submitted = false;
        this.createdAccount = "User";
      },
      error: error => {
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

  registerAdmin(userWithPassword: UserWithPassword) {
    this.userManagementService.administrativeUsersPost(userWithPassword).subscribe({
      next: () => {
        console.log("success!");
        this.success = true;
        this.successUser = this.f['email'].value;
        this.sendPasswordReset(this.f['email'].value);
        this.error = false;
        this.clearForm();
        this.registrationForm.markAsPristine();
        this.registrationForm.markAsUntouched();
        this.submitted = false;
        this.createdAccount = "Admin";
      },
      error: error => {
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

  getAdminInfo() {
    this.userManagementService.userInfoGet().subscribe({
      next: (next) => {
        this.adminMail = next.email;
        console.log("Succesfully got admin with id " + next.userId);
        this.error = false;
      },
      error: (error) => {
        console.error("Error getting admin from authentication token");
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

  sendPasswordReset(email: string) {

    const passwordReset: PasswordReset = {
      email,
      clientURI: this.clientUrl
    };
    console.log("sending");
    this.userManagementService.passwordResetPost(passwordReset, 'body', true).subscribe(
        {
          next: (response) => {
            console.log("succesfully reset password for created account");
            this.success = true;
          },
          error: (err) => {
            this.error = err;
            console.log("failed to reset password for created account");
            this.success = false;
          }
        }
    );
  }

  confirmUser() {
    if (this.authenticated) {
      this.signUpUser();
    } else if (this.passwordForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(this.adminMail, this.passwordForm.controls.password.value);
      console.log(this.adminMail);
      this.authenticateAdmin(authRequest);
      this.onCloseHandled();
    } else {
      console.log('Invalid input');
    }
  }

  authenticateAdmin(authRequest: AuthRequest) {
    console.log('Try to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        this.signUpUser();
        this.authenticated = true;
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
        this.passwordForm.reset();
      }
    });
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

  openModal() {
    this.submitted = true;
    if (this.registrationForm.valid) {
      this.display = "block";
    } else {
      console.log('Invalid input');
    }
  }

  onCloseHandled() {
    this.display = "none";
  }
}
